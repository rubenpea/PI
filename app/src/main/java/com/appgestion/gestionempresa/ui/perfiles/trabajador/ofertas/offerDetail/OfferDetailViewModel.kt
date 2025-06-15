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

    /**
     * Carga la oferta con id [ofertaId] y vuelca directamente el Response en el estado.
     */
    fun loadOferta(ofertaId: String) {
        viewModelScope.launch {
            // Avisamos que estamos cargando
            _uiState.update { it.copy(response = Response.Loading) }

            // Ejecutamos el UseCase (que ahora devuelve Response<OfertaEntity>)
            val resp: Response<OfertaEntity> = getByIdUseCase(ofertaId)

            // Volcamos la respuesta (Success o Failure) en el estado
            _uiState.update { it.copy(response = resp) }
        }
    }
}
