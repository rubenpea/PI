package com.appgestion.gestionempresa.ui.perfiles.empresa.candidaturas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.CurriculumEntity
import com.appgestion.gestionempresa.domain.usecase.trabajador.GetCVByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CvDetailViewModel @Inject constructor(
    private val getCvById: GetCVByIdUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<Response<CurriculumEntity>>(Response.Loading)
    val uiState: StateFlow<Response<CurriculumEntity>> = _uiState

    fun loadCv(cvId: String) = viewModelScope.launch {
        _uiState.value = Response.Loading
        val cv = getCvById(cvId)
        if (cv != null) {
            _uiState.value = Response.Success(cv)
        } else {
            _uiState.value = Response.Failure(Exception("CV no encontrado"))
        }
    }
}


