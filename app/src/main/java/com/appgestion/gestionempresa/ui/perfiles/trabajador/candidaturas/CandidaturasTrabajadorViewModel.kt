package com.appgestion.gestionempresa.ui.perfiles.trabajador.candidaturas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.repository.OfertaRepository
import com.appgestion.gestionempresa.domain.usecase.trabajador.GetMisCandidaturasUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CandidaturasTrabajadorViewModel @Inject constructor(
    private val candidaturasTrabajador: GetMisCandidaturasUseCase,
    private val ofertaRepo: OfertaRepository
) : ViewModel(){

    private val _uiState = MutableStateFlow(CandidaturasTrabajadorState())
    val uiState: StateFlow<CandidaturasTrabajadorState> = _uiState.asStateFlow()

    fun loadCandidaturas(trabajadorId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(response = Response.Loading) }
            val res = candidaturasTrabajador(trabajadorId)
            _uiState.update { it.copy(response = res) }
        }
    }


}

