package com.appgestion.gestionempresa.domain.model



enum class Role {
    EMPRESA,
    TRABAJADOR
}

data class UsuarioEntity(
    val id: String,
    val email: String,
    val rol: Role,
    val nombre: String,
    val telefono: String,
    val empresaId: String? = null
)



