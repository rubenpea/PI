package com.appgestion.gestionempresa.data.model

import com.appgestion.gestionempresa.domain.model.EmpresaEntity
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class EmpresaDto(
    @DocumentId
    val id: String = "",

    @PropertyName("nombre")
    val nombre: String = "",

    @PropertyName("descripcion")
    val descripcion: String = "",

    @PropertyName("fechaCreacion")
    val fechaCreacion: Long = 0L,

    @PropertyName("listaTrabajadores")
    val listaTrabajadores: List<String> = emptyList()
)

fun EmpresaDto.toDomain(): EmpresaEntity {
    return EmpresaEntity(
        id = id,
        nombre = nombre,
        descripcion = descripcion,
        fechaCreacion = fechaCreacion,
        listaTrabajadores = listaTrabajadores
    )
}

fun EmpresaEntity.toDto(): EmpresaDto {
    return EmpresaDto(
        id = id,
        nombre = nombre,
        descripcion = descripcion,
        fechaCreacion = fechaCreacion,
        listaTrabajadores = listaTrabajadores
    )
}