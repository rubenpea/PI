package com.appgestion.gestionempresa.ui.perfiles.empresa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.data.model.UsuarioDto
import com.appgestion.gestionempresa.data.model.toDomain
import com.appgestion.gestionempresa.domain.model.EmpresaEntity
import com.appgestion.gestionempresa.domain.model.UsuarioEntity
import com.appgestion.gestionempresa.domain.repository.EmpresaRepository
import com.appgestion.gestionempresa.domain.usecase.empresa.AddTrabajadorByEmailUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class EmpresaViewModel @Inject constructor(
    private val empresaRepository: EmpresaRepository,
    private val addTrabajadorByEmailUseCase: AddTrabajadorByEmailUseCase,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _uiState = MutableStateFlow(EmpresaUiState())
    val uiState: StateFlow<EmpresaUiState> = _uiState

    val trabajadoresInfo = MutableStateFlow<List<UsuarioEntity>>(emptyList())

    fun saveEmpresa(empresa: EmpresaEntity) {
        viewModelScope.launch {
            _uiState.value =
                _uiState.value.copy(loading = true, error = null, successMessage = null)
            when (val resp = empresaRepository.saveEmpresa(empresa)) {
                is Response.Success -> {
                    try {

                        val uidEmpresa = FirebaseAuth.getInstance().currentUser?.uid

                        uidEmpresa?.let { uid ->
                            firestore.collection("usuarios").document(uid)
                                .update("empresaId", uid)
                                .await()
                        }

                        _uiState.value = _uiState.value.copy(
                            loading = false,
                            successMessage = "Perfil guardado"
                        )
                    } catch (e: Exception) {
                        _uiState.value = _uiState.value.copy(
                            loading = false,
                            error = "Error al asignar empresaId: ${e.message}"
                        )
                    }
                }

                is Response.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        error = resp.exception.message ?: "Error al guardar empresa"
                    )
                }
                is Response.Loading -> Response.Failure(Exception("Operación no válida en estado Loading"))
            }
        }
    }

    fun fetchEmpresa(uid: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null)
            when (val resp = empresaRepository.fetchEmpresa(uid)) {
                is Response.Success -> {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        empresa = resp.data
                    )
                    _uiState.value = _uiState.value.copy(empresa = resp.data)
                    cargarInfoTrabajadores(resp.data.listaTrabajadores)
                }

                is Response.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        error = resp.exception.message ?: "Error al cargar empresa"
                    )
                }
                is Response.Loading -> Response.Failure(Exception("Operación no válida en estado Loading"))
            }
        }
    }

    fun agregarTrabajador(email: String, idEmpresa: String) {
        viewModelScope.launch {
            _uiState.value =
                _uiState.value.copy(loading = true, error = null, successMessage = null)

            when (val result = addTrabajadorByEmailUseCase(idEmpresa, email)) {
                is Response.Success -> {
                    fetchEmpresa(idEmpresa)

                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        successMessage = "Trabajador añadido con éxito"
                    )

                    kotlinx.coroutines.delay(3000)
                    _uiState.value = _uiState.value.copy(successMessage = null)
                }

                is Response.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        error = result.exception.message ?: "Error al añadir trabajador"
                    )

                    kotlinx.coroutines.delay(3000)
                    _uiState.value = _uiState.value.copy(error = null)
                }
                is Response.Loading -> Response.Failure(Exception("Operación no válida en estado Loading"))
            }
        }
    }

    fun cargarInfoTrabajadores(listaUids: List<String>) {
        viewModelScope.launch {
            val resultados = mutableListOf<UsuarioEntity>()
            for (uid in listaUids) {
                try {
                    val snapshot = FirebaseFirestore.getInstance()
                        .collection("usuarios")
                        .document(uid)
                        .get()
                        .await()

                    val usuario = snapshot.toObject(UsuarioDto::class.java)?.toDomain()
                    usuario?.let { resultados.add(it) }
                } catch (e: Exception) {
                    println("Error cargando trabajador $uid: ${e.message}")
                }
            }
            trabajadoresInfo.value = resultados
        }
    }

    fun eliminarTrabajador(empresaUid: String, trabajadorUid: String) {
        viewModelScope.launch {
            _uiState.value =
                _uiState.value.copy(loading = true, error = null, successMessage = null)

            when (val result = empresaRepository.removeTrabajador(empresaUid, trabajadorUid)) {
                is Response.Success -> {
                    fetchEmpresa(empresaUid)
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        successMessage = "Trabajador eliminado"
                    )
                }

                is Response.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        error = result.exception.message ?: "Error al eliminar trabajador"
                    )
                }
                is Response.Loading -> Response.Failure(Exception("Operación no válida en estado Loading"))
            }
        }
    }



    fun cargarTrabajadoresVinculados(empresaId: String) {
        viewModelScope.launch {
            println("Cargando trabajadores para empresaId: $empresaId")
            val trabajadores = empresaRepository.obtenerTrabajadoresDeEmpresa(empresaId)
            println("Total trabajadores recibidos: ${trabajadores.size}")
            trabajadores.forEach { println("Trabajador: ${it.nombre} - ${it.email} - empresaId=${it.empresaId}") }
            trabajadoresInfo.value = trabajadores
        }
    }
    }



