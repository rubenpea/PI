package com.appgestion.gestionempresa.domain.model


    enum class EstadoTarea {
        PENDIENTE,
        EN_PROCESO,
        COMPLETADA
    }
    data class TareaEntity(
        val id: String = "",
        val titulo: String,
        val descripcion: String,
        val fechaCreacion: Long,
        val estado: EstadoTarea,
        val trabajadorId: String,
        val empresaId: String
    )
