package com.appgestion.gestionempresa.ui.registro.empresa

data class RegistroEmpresaState (

    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",
    val passwordConfirm: String = "",

    val error: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val passwordConfirmError: String? = null,
    val nameError: String? = null,
    val phoneError: String? = null,
    val passDiffError: String? = null
)
