package com.appgestion.gestionempresa.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.data.model.Usuarios
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

    private val _user = MutableStateFlow<Usuarios?>(null)
    val user: StateFlow<Usuarios?> = _user

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    //resetear valor
    fun emailChange(newEmail: String) {
        _uiState.value = uiState.value.copy(
            email = newEmail
        )
    }

    //resetear valor
    fun passChange(newPass: String) {
        _uiState.value = uiState.value.copy(
            password = newPass
        )
    }

    fun login() {
        // 1) Extraemos explícitamente email y password
        val email = _uiState.value.email
        val password = _uiState.value.password

        viewModelScope.launch {
            // 2) Primera llamada: obtenemos el UID
            when (val loginResp = authRepository.loginUser(email, password)) {
                is Response.Success -> {
                    _user.value = loginResp.data   // aquí tienes el String con el UID
                }
                    is Response.Failure -> {
                        _error.value = loginResp.exception.message
                    }
                }
            }
        }
    }




