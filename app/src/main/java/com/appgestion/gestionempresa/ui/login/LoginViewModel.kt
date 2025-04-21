package com.appgestion.gestionempresa.ui.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState

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
}

