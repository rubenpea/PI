package com.appgestion.gestionempresa.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appgestion.gestionempresa.data.model.Response
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

    private val _loginUser = MutableStateFlow(false)
    val loginUser : StateFlow<Boolean> = _loginUser

    //resetear valor
    fun emailChange(newEmail:String){
        _uiState.value = uiState.value.copy(
            email = newEmail
        )
    }

    //resetear valor
    fun passChange(newPass: String){
        _uiState.value = uiState.value.copy(
            password = newPass
        )
    }

    fun login(){
        val st = _uiState.value

        viewModelScope.launch {
            when( authRepository.loginUser
                (
                email = st.email,
                password = st.password
                        )
            ) {
                is Response.Success -> _loginUser.value = true
                
                is Response.Failure -> _loginUser.value = false

            }
        }
    }
}

