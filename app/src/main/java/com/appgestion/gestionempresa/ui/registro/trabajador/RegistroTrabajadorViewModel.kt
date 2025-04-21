package com.appgestion.gestionempresa.ui.registro.trabajador

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.repository.AuthRepository
import com.appgestion.gestionempresa.navigation.AppScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistroTrabajadorViewModel @Inject constructor(
   private val authRepository: AuthRepository
): ViewModel() {


    private val _uiState = MutableStateFlow(RegistroTrabajadorState())
    val uiState: StateFlow<RegistroTrabajadorState> = _uiState


    fun changeEmail(newEmail:String){
        _uiState.value = _uiState.value.copy(
            email = newEmail,
            emailError = null
        )
    }

    fun changePass(newPass:String){
        _uiState.update { current ->
            val confirm = current.passwordConfirm
            current.copy(
                password = newPass,
                passwordError = null,
                passDiffError = if (confirm.isNotBlank() && newPass != confirm) {
                    "Contraseñas diferentes"
                } else {
                    null
                }
            )
        }
    }

    fun changePassConfirm(newPass:String){
        _uiState.update { current ->
            current.copy(
                passwordConfirm = newPass,
                passDiffError = if (current.password.isNotBlank() && current.password != newPass) {
                    "Contraseñas diferentes"
                } else {
                    null
                }
            )
        }
    }

    fun changeName(newName:String){
        _uiState.value = _uiState.value.copy(
            name = newName,
            nameError = null
        )
    }

    fun changePhone(newPhone:String){
        _uiState.value = _uiState.value.copy(
            phone = newPhone,
            phoneError = null
        )
    }

    fun validar(): Boolean {
        // Tomamos el estado actual para evitar llamadas repetidas a _uiState.value
        val currentState = _uiState.value

        // Calculamos el error para cada campo de forma local
        val emailError = if (currentState.email.isBlank()) "Campo obligatorio" else null
        val passwordError = if (currentState.password.isBlank()) "Campo obligatorio" else null
        val passwordConfirmError = if (currentState.passwordConfirm.isBlank()) "Campo obligatorio" else null
        val nombreError = if (currentState.name.isBlank()) "Campo obligatorio" else null
        val phoneError = if (currentState.phone.isBlank()) "Campo obligatorio" else null

        // Actualizamos el estado del ViewModel una sola vez con todos los errores calculados
        _uiState.update {
            it.copy(
                emailError = emailError,
                passwordError = passwordError,
                passwordConfirmError = passwordConfirmError,
                nameError = nombreError,
                phoneError = phoneError,
            )
        }
        // La validación es exitosa si ninguno de los errores tiene valor (es decir, todos son null)
        return listOf(emailError, passwordError, passwordConfirmError, nombreError, phoneError)
            .all { it == null}
    }

     fun registrarTrabajador() {
        val email = uiState.value.email
        val password = uiState.value.password
        val tipe = "trabajador"

        viewModelScope.launch {
          val result = authRepository.registerUser(email, password, tipe)

            when(result){
                is Response.Success -> {

                }
                is Response.Failure -> {

                }
            }
        }
    }
}
