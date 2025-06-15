package com.appgestion.gestionempresa.ui.perfiles.trabajador.cv

data class CVFormState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val summary: String = "",
    val skillsText: String = "",
    val isValid: Boolean = false,
    val nameError: String? = null,
    val emailError: String? = null,
    val phoneError: String? = null,
    val summaryError: String? = null,
    val skillsError: String? = null,

)
