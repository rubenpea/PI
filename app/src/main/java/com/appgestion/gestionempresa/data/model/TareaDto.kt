package com.appgestion.gestionempresa.data.model

import com.appgestion.gestionempresa.domain.model.EstadoTarea
import com.appgestion.gestionempresa.domain.model.TareaEntity
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class TareaDto(
    @DocumentId val id: String = "",
    @PropertyName("titulo") val titulo: String = "",
    @PropertyName("descripcion") val descripcion: String = "",
    @PropertyName("fechaCreacion") val fechaCreacion: Long = 0,
    @PropertyName("estado") val estado: String = "PENDIENTE",
    @PropertyName("trabajadorId") val trabajadorId: String = "",
    @PropertyName("empresaId") val empresaId: String = ""
)

fun TareaDto.toDomain(): TareaEntity = TareaEntity(
    id, titulo, descripcion, fechaCreacion,
    estado = EstadoTarea.valueOf(estado),
    trabajadorId, empresaId
)

fun TareaEntity.toDto(): TareaDto = TareaDto(
    id, titulo, descripcion, fechaCreacion,
    estado = estado.name,
    trabajadorId, empresaId
)