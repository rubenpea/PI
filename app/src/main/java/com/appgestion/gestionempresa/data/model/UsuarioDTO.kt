package com.appgestion.gestionempresa.data.model

import com.appgestion.gestionempresa.domain.model.Role
import com.appgestion.gestionempresa.domain.model.UsuarioEntity
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class UsuarioDto(
    @DocumentId
    val uid: String = "",

    @PropertyName("email")
    val email: String = "",

    @PropertyName("tipo")
    val tipo: String = "",

    @PropertyName("name")
    val name: String = "",

    @PropertyName("phone")
    val phone: String = "",

    @PropertyName("empresaId")
    val empresaId: String? = null,

    @PropertyName("password")
    val password: String = ""
)

fun UsuarioDto.toDomain(): UsuarioEntity {
    val roleEnum = when (tipo.lowercase()) {
        "empresa"    -> Role.EMPRESA
        "trabajador" -> Role.TRABAJADOR
        else         -> Role.TRABAJADOR
    }
    return UsuarioEntity(
        id = uid,
        email = email,
        rol = roleEnum,
        nombre = name,
        telefono = phone,
        empresaId = empresaId
    )
}

fun UsuarioEntity.toDto(passwordEnTexto: String = ""): UsuarioDto {
    val tipoStr = when (this.rol) {
        Role.EMPRESA    -> "empresa"
        Role.TRABAJADOR -> "trabajador"
    }
    return UsuarioDto(
        uid = this.id,
        email = this.email,
        tipo = tipoStr,
        name = this.nombre,
        phone = this.telefono,
        empresaId = this.empresaId,
        password = passwordEnTexto
    )
}