package com.appgestion.gestionempresa.ui.perfiles.trabajador.cv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appgestion.gestionempresa.domain.model.CurriculumEntity
import com.appgestion.gestionempresa.domain.usecase.trabajador.CreateCVUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CVFormViewModel @Inject constructor(
    private val createCV: CreateCVUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(CVFormState())
    val uiState = _uiState.asStateFlow()

    fun onNameChange(v: String)    = update { it.copy(name = v) }
    fun onEmailChange(v: String)   = update { it.copy(email = v) }
    fun onPhoneChange(v: String)   = update { it.copy(phone = v) }
    fun onSummaryChange(v: String) = update { it.copy(summary = v) }
    fun onSkillsChange(v: String)  = update { it.copy(skillsText = v) }

    private fun update(transform: (CVFormState) -> CVFormState) {
        _uiState.update { old ->
            val candidate = transform(old)

            val nameErr    = if (candidate.name.isBlank()) "Requerido" else null
            val emailErr   = if (!candidate.email.contains("@")) "Email inválido" else null
            val phoneErr   = null
            val summaryErr = null
            val skillsErr  = if (candidate.skillsText.isBlank()) "Requerido" else null

            val valid = listOf(nameErr, emailErr, phoneErr, summaryErr, skillsErr)
                .all { it == null }

            candidate.copy(
                nameError    = nameErr,
                emailError   = emailErr,
                phoneError   = phoneErr,
                summaryError = summaryErr,
                skillsError  = skillsErr,
                isValid      = valid
            )
        }
    }

    fun createCV() = viewModelScope.launch {
        val s = uiState.value
        val cv = CurriculumEntity(
            id        = "",
            workerId  = FirebaseAuth.getInstance().currentUser!!.uid,
            name      = s.name,
            email     = s.email,
            phone     = s.phone,
            summary   = s.summary,
            skills    = s.skillsText.split(",").map { it.trim() }
        )
        createCV(cv)
    }
}