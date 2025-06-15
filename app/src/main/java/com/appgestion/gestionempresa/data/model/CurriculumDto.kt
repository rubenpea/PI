package com.appgestion.gestionempresa.data.model

import com.appgestion.gestionempresa.domain.model.CurriculumEntity


data class CurriculumDto(
    val id: String = "",
    val workerId: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val summary: String = "",
    val skills: List<String> = emptyList()
)

fun CurriculumDto.toDomain() = CurriculumEntity(
    id = id, workerId = workerId, name = name,
    email = email, phone = phone,
    summary = summary, skills = skills
)

fun CurriculumEntity.toDto() = CurriculumDto(
    id = id, workerId = workerId, name = name,
    email = email, phone = phone,
    summary = summary, skills = skills
)