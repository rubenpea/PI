package com.appgestion.gestionempresa.ui.perfiles.trabajador.ofertas

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.OfertaEntity
import com.appgestion.gestionempresa.domain.usecase.empresa.GetAllOfertasUseCase
import com.appgestion.gestionempresa.domain.usecase.empresa.GetEmpresaByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OffersViewModel @Inject constructor(
    private val getAllOfertas: GetAllOfertasUseCase,
    private val getEmpresaById: GetEmpresaByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OffersState())
    val uiState: StateFlow<OffersState> = _uiState.asStateFlow()

    val empresaNames = mutableStateMapOf<String, String>()

    init { loadAll() }

    private fun loadAll() = viewModelScope.launch {
        _uiState.value = OffersState(Response.Loading)
        when (val resp = getAllOfertas()) {
            is Response.Success -> {
                val list = resp.data
                list.map { it.idEmpresa }.distinct().forEach { id ->
                    launch {
                        when(val e = getEmpresaById(id)) {
                            is Response.Success -> empresaNames[id] = e.data.nombre
                            else               -> empresaNames[id] = "—"
                        }
                    }
                }
                _uiState.value = OffersState(Response.Success(list))
            }
            is Response.Failure -> _uiState.value = OffersState(Response.Failure(resp.exception))
            else                -> Unit
        }
    }

    fun filter(query: String) {
        val filtered = (_uiState.value.response as? Response.Success<List<OfertaEntity>>)
            ?.data
            ?.filter {
                it.titulo.contains(query, true) ||
                        it.requisitos.contains(query, true)
            } ?: emptyList()
        _uiState.value = OffersState(Response.Success(filtered))
    }
}



