package com.appgestion.gestionempresa.ui.perfiles.trabajador.vacaciones

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.usecase.trabajador.GetMisVacacionesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VacacionesSolicitadasViewModel @Inject constructor(
    private val getMisVacaciones: GetMisVacacionesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(VacacionesSolicitadasState())
    val uiState: StateFlow<VacacionesSolicitadasState> = _uiState.asStateFlow()

    fun loadRequests(workerId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(response = Response.Loading) }
            val res = getMisVacaciones(workerId)
            _uiState.update { it.copy(response = res) }
        }
    }
}