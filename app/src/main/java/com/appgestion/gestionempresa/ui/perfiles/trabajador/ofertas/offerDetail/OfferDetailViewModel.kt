package com.appgestion.gestionempresa.ui.perfiles.trabajador.ofertas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.OfertaEntity
import com.appgestion.gestionempresa.domain.usecase.empresa.GetOfertaByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfferDetailViewModel @Inject constructor(
    private val getByIdUseCase: GetOfertaByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OfferDetailState())
    val uiState: StateFlow<OfferDetailState> = _uiState.asStateFlow()


    fun loadOferta(ofertaId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(response = Response.Loading) }

            val resp: Response<OfertaEntity> = getByIdUseCase(ofertaId)

            _uiState.update { it.copy(response = resp) }
        }
    }
}
