package com.appgestion.gestionempresa.ui.perfiles.trabajador.vacaciones

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.VacacionesEntity
import com.appgestion.gestionempresa.domain.usecase.trabajador.SubmitVacacionesUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@HiltViewModel
class VacacionesViewModel @Inject constructor(
    private val submitUseCase: SubmitVacacionesUseCase,
    private val firestore: FirebaseFirestore
): ViewModel() {
    private val _state = MutableStateFlow(VacacionesState())
    val state: StateFlow<VacacionesState> = _state.asStateFlow()

    fun onDatesChanged(start: Long, end: Long) {
        val days = ((end - start) / (1000 * 60 * 60 * 24) + 1).toInt()
        _state.update { it.copy(startDate = start, endDate = end, days = days) }
    }

    fun submit(workerId: String) = viewModelScope.launch {
        try {
            // 1) Obtén la empresaId real del trabajador
            val snap = firestore
                .collection("usuarios")
                .document(workerId)
                .get()
                .await()
            val empresaId = snap.getString("empresaId")
                ?: throw IllegalStateException("Este trabajador no está en ninguna empresa")

            // 2) Crea la petición con el campo empresaId correcto
            val s = state.value
            val req = VacacionesEntity(
                id        = "",
                workerId  = workerId,
                empresaId = empresaId,          // ← **UID de la empresa**, no del trabajador
                startDate = s.startDate!!,
                endDate   = s.endDate!!,
                days      = s.days
            )

            // 3) Envía
            submitUseCase(req)
            // aquí notificar éxito/error al usuario…

        } catch (e: Exception) {
            // manejar error (por ejemplo, toast)
        }
    }
}