package com.appgestion.gestionempresa.domain.usecase.empresa

import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.OfertaEntity
import com.appgestion.gestionempresa.domain.repository.OfertaRepository

class CreateOfertaUseCase(
    private val ofertaRepository: OfertaRepository
) {
    suspend operator fun invoke(oferta: OfertaEntity): Response<Boolean>{
        return ofertaRepository.crearOferta(oferta)
    }
}

class DeleteOfertaUseCase(
    private val repo: OfertaRepository
) {
    suspend operator fun invoke(ofertaId: String): Response<Boolean> =
        repo.eliminarOferta(ofertaId)
}

class GetAllOfertasUseCase(
    private val repo: OfertaRepository
) {
    suspend operator fun invoke(): Response<List<OfertaEntity>> =
        repo.getAllOfertas()
}

class GetOfertaByIdUseCase(
    private val repo: OfertaRepository
) {
    suspend operator fun invoke(id: String): Response<OfertaEntity> =
        repo.getOfertaById(id)
}

class GetOfertasByEmpresaUseCase(
    private val repo: OfertaRepository
) {
    suspend operator fun invoke(empresaId: String): Response<List<OfertaEntity>> =
        repo.obtenerOfertasPorEmpresa(empresaId)
}

