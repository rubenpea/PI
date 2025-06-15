package com.appgestion.gestionempresa.data.model


// file: data/model/UsuarioDto.kt

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

    // Observación: normalmente no conviene guardar contraseñas en texto plano en Firestore.
    // Firebase Auth las guarda por separado. Si no las guardas en Firestore, quita este campo.
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

/**
 * Convierte de entidad de dominio a DTO (para guardar en Firestore).
 * Si no guardas la contraseña en Firestore, omítelo o pásalo como cadena vacía.
 */
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