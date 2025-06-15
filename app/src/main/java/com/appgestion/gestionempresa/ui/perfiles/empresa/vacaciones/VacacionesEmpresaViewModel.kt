package com.appgestion.gestionempresa.ui.perfiles.empresa.vacaciones

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.usecase.trabajador.GetVacacionesPorEmpresaUseCase
import com.appgestion.gestionempresa.domain.usecase.trabajador.UpdateVacacionesStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VacacionesEmpresaViewModel @Inject constructor(
    private val getByEmpresa: GetVacacionesPorEmpresaUseCase,
    private val updateStatus: UpdateVacacionesStatusUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(VacacionesEmpresaState())
    val uiState: StateFlow<VacacionesEmpresaState> = _uiState.asStateFlow()

    fun loadRequests(empresaId: String) = viewModelScope.launch {
        _uiState.value = VacacionesEmpresaState(response = Response.Loading)
        when(val res = getByEmpresa(empresaId)) {
            is Response.Success ->
                _uiState.value = VacacionesEmpresaState(response = res)
            is Response.Failure ->
                _uiState.value = VacacionesEmpresaState(response = res)
            else -> Unit
        }
    }


    fun updateRequestStatus(id: String, newStatus: String, empresaId: String) {
        viewModelScope.launch {
            when (val r = updateStatus(id, newStatus)) {
                is Response.Success -> loadRequests(empresaId)
                is Response.Failure -> _uiState.update { it.copy(response = Response.Failure(r.exception)) }
                else -> {}
            }
        }
    }
}
