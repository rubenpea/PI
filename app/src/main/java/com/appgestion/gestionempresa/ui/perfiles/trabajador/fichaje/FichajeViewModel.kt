package com.appgestion.gestionempresa.ui.perfiles.trabajador.fichaje


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.FichajeEntity
import com.appgestion.gestionempresa.domain.usecase.trabajador.FichajeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class FichajeViewModel @Inject constructor(
    private val fichajeUseCases: FichajeUseCases
) : ViewModel(){

    private val _registro = MutableStateFlow<Response<Boolean>>(Response.Success(false))
    val registro: StateFlow<Response<Boolean>> = _registro

    private val _historial = MutableStateFlow<Response<List<FichajeEntity>>>(Response.Loading)
    val historial: StateFlow<Response<List<FichajeEntity>>> = _historial

    private val _fichajeAbierto = MutableStateFlow<FichajeEntity?>(null)
    val fichajeAbierto: StateFlow<FichajeEntity?> = _fichajeAbierto

    fun cargarHistorial(uid: String) {
        viewModelScope.launch {
            val response = fichajeUseCases.obtenerFichajes(uid)
            _historial.value = response

            if (response is Response.Success) {
                _fichajeAbierto.value = response.data.lastOrNull { it.checkOut == null }
            }
        }
    }

    fun iniciarFichaje(uid: String) {
        val fecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val checkIn = System.currentTimeMillis()

        val fichaje = FichajeEntity(
            uidUsuario = uid,
            fecha = fecha,
            checkIn = checkIn
        )

        viewModelScope.launch {
            _registro.value = fichajeUseCases.iniciarFichaje(fichaje)
            cargarHistorial(uid)
        }
    }

    fun finalizarFichaje(uid: String) {
        viewModelScope.launch {
            val lista = (_historial.value as? Response.Success)?.data ?: return@launch
            val abierto = lista.lastOrNull { it.checkOut == null } ?: return@launch

            val checkOut = System.currentTimeMillis()
            _registro.value = fichajeUseCases.finalizarFichaje(abierto.id, checkOut)
            cargarHistorial(uid)
        }
    }

    fun calcularHorasSemanaActual(): Float {
        val lista = (_historial.value as? Response.Success)?.data ?: return 0f

        val calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

        val inicioSemana = calendar.timeInMillis
        val finSemana = inicioSemana + 7 * 24 * 60 * 60 * 1000L

        return lista
            .filter { it.checkIn in inicioSemana..finSemana && it.duracion != null }
            .map { it.duracion!!.toFloat() / 1000f / 60f / 60f }
            .sum()  // horas
    }
}
