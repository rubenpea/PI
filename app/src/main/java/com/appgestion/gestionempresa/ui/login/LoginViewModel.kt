package com.appgestion.gestionempresa.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.UsuarioEntity
import com.appgestion.gestionempresa.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState

    private val _user = MutableStateFlow<UsuarioEntity?>(null)
    val user: StateFlow<UsuarioEntity?> = _user

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun emailChange(newEmail: String) {
        _uiState.value = uiState.value.copy(email = newEmail)
        _error.value = null
    }

    fun passChange(newPass: String) {
        _uiState.value = uiState.value.copy(password = newPass)
        _error.value = null // Limpiar error
    }

    fun login() {
        val email = _uiState.value.email.trim()
        val password = _uiState.value.password.trim()

        if (email.isBlank() || password.isBlank()) {
            _error.value = "Email y contraseña son obligatorios"
            return
        }

        viewModelScope.launch {
            when (val resp = authRepository.loginUser(email, password)) {
                is Response.Failure -> {
                    val ex = resp.exception
                    _error.value = when (ex) {
                        is com.google.firebase.auth.FirebaseAuthInvalidUserException ->
                            "No existe ningún usuario con este email"

                        is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException ->
                            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
                                "Formato de email no válido"
                            else
                                "Contraseña incorrecta"

                        else ->
                            "Error al iniciar sesión: ${ex.localizedMessage ?: "desconocido"}"
                    }
                }

                is Response.Success -> {
                    _user.value = resp.data
                }

                is Response.Loading -> {
                    _error.value = "Operación no válida en estado Loading"
                }
            }
        }
    }
}





