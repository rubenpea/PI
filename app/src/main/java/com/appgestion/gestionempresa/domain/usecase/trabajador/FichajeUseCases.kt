package com.appgestion.gestionempresa.domain.usecase.trabajador

import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.FichajeEntity
import com.appgestion.gestionempresa.domain.repository.FichajeRepository
import javax.inject.Inject

class FichajeUseCases(
    val iniciarFichaje: IniciarFichajeUseCase,
    val finalizarFichaje: FinalizarFichajeUseCase,
    val obtenerFichajes: ObtenerFichajesUseCase
)

class IniciarFichajeUseCase @Inject constructor(
    private val repo: FichajeRepository
) {
    suspend operator fun invoke(fichaje: FichajeEntity): Response<Boolean> {
        return repo.iniciarFichaje(fichaje)
    }
}

class FinalizarFichajeUseCase @Inject constructor(
    private val repo: FichajeRepository
) {
    suspend operator fun invoke(fichajeId: String, checkOutTime: Long): Response<Boolean> {
        return repo.finalizarFichaje(fichajeId, checkOutTime)
    }
}

class ObtenerFichajesUseCase @Inject constructor(
    private val repo: FichajeRepository
) {
    suspend operator fun invoke(uid: String): Response<List<FichajeEntity>> {
        return repo.obtenerFichajesUsuario(uid)
    }
}