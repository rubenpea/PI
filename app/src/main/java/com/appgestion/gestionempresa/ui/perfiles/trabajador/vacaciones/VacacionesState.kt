package com.appgestion.gestionempresa.ui.perfiles.trabajador.vacaciones


data class VacacionesState(
    val startDate: Long? = null,
    val endDate: Long? = null,
    val days: Int = 0
)