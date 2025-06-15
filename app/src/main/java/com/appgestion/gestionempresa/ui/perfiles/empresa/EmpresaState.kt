package com.appgestion.gestionempresa.ui.perfiles.empresa

import com.appgestion.gestionempresa.domain.model.EmpresaEntity

data class EmpresaUiState(
    val empresa: EmpresaEntity? = null,
    val loading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)