package com.appgestion.gestionempresa.ui.registro.trabajador

data class RegistroTrabajadorState (

    val name: String = "",
    val phone: String = "",
    val email: String = "",
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
