package com.appgestion.gestionempresa.domain.model

data class CurriculumEntity(
    val id: String = "",
    val workerId: String,
    val name: String,
    val email: String,
    val phone: String,
    val summary: String,
    val skills: List<String>
)