package com.appgestion.gestionempresa.ui.perfiles.empresa.candidaturas

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.CandidaturaEntity
import com.appgestion.gestionempresa.domain.model.Role
import com.appgestion.gestionempresa.domain.model.UsuarioEntity
import com.appgestion.gestionempresa.domain.usecase.trabajador.GetCandidaturasPorOfertaUseCase
import com.appgestion.gestionempresa.domain.usecase.trabajador.GetUsuarioByIdUseCase
import com.appgestion.gestionempresa.domain.usecase.trabajador.UpdateCandidaturaStatusUseCase
import com.appgestion.gestionempresa.domain.usecase.user.GetUserByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.*



@HiltViewModel
class CandidaturasEmpresaViewModel @Inject constructor(
    private val getByOferta: GetCandidaturasPorOfertaUseCase,
    private val updateStatus: UpdateCandidaturaStatusUseCase,
    private val getUserById: GetUserByIdUseCase
) : ViewModel() {

    val usuarios = mutableStateMapOf<String, UsuarioEntity?>()

    private val _uiState = MutableStateFlow(CandidaturasEmpresaState())
    val uiState: StateFlow<CandidaturasEmpresaState> = _uiState

    fun loadCandidaturas(ofertaId: String) = viewModelScope.launch {
        _uiState.value = CandidaturasEmpresaState(Response.Loading)
        when(val r = getByOferta(ofertaId)) {
            is Response.Success -> _uiState.value = CandidaturasEmpresaState(r)
            is Response.Failure -> _uiState.value = CandidaturasEmpresaState(r)
            else -> {}
        }
    }

    fun updateStatus(candId: String, newStatus: String, ofertaId: String) {
        viewModelScope.launch {
            when(val r = updateStatus(candId, newStatus)) {
                is Response.Success -> loadCandidaturas(ofertaId)
                is Response.Failure -> _uiState.update { it.copy(response = Response.Failure(r.exception)) }
                else -> {}
            }
        }
    }

    fun fetchUsuario(trabajadorId: String) = viewModelScope.launch {
        usuarios[trabajadorId] = null
        when(val r = getUserById(trabajadorId)) {
            is Response.Success -> usuarios[trabajadorId] = r.data
            is Response.Failure -> usuarios[trabajadorId] = UsuarioEntity(
                id       = trabajadorId,
                email    = "—",
                rol      = Role.TRABAJADOR,
                nombre  = "—",
                telefono = "—"
            )

            Response.Loading -> TODO()
        }
    }
}

