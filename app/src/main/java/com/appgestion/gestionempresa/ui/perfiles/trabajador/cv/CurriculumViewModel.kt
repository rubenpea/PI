package com.appgestion.gestionempresa.ui.perfiles.trabajador.cv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.usecase.trabajador.GetMyCVsUseCase
import com.appgestion.gestionempresa.domain.usecase.trabajador.SendCVUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurriculumViewModel @Inject constructor(
    private val getMyCVs: GetMyCVsUseCase,
    private val sendCV: SendCVUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(CurriculumState())
    val uiState: StateFlow<CurriculumState> = _uiState.asStateFlow()

    private val _applyResult = MutableSharedFlow<Response<Boolean>>()
    val applyResult = _applyResult.asSharedFlow()

    fun loadMyCVs(workerId: String) = viewModelScope.launch {
        _uiState.update { it.copy(response = Response.Loading) }
        _uiState.update { it.copy(response = getMyCVs(workerId)) }
    }

    fun applyToOffer(workerId: String, ofertaId: String, cvId: String) = viewModelScope.launch {
        val res = sendCV(workerId, ofertaId, cvId)
        _applyResult.emit(res)
    }
}