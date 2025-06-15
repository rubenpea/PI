package com.appgestion.gestionempresa.di

import com.appgestion.gestionempresa.data.repository.AuthRepositoryImpl
import com.appgestion.gestionempresa.data.repository.CandidaturaRepositoryImpl
import com.appgestion.gestionempresa.data.repository.CurriculumRepositoryImpl
import com.appgestion.gestionempresa.data.repository.EmpresaRepositoryImpl
import com.appgestion.gestionempresa.data.repository.FacturaRepositoryImpl
import com.appgestion.gestionempresa.data.repository.FichajeRepositoryImpl
import com.appgestion.gestionempresa.data.repository.OfertaRepositoryImpl
import com.appgestion.gestionempresa.data.repository.TareaRepositoryImpl
import com.appgestion.gestionempresa.data.repository.UserRepositoryImpl
import com.appgestion.gestionempresa.data.repository.VacacionesRepositoryImpl
import com.appgestion.gestionempresa.domain.repository.AuthRepository
import com.appgestion.gestionempresa.domain.repository.CandidaturaRepository
import com.appgestion.gestionempresa.domain.repository.CurriculumRepository
import com.appgestion.gestionempresa.domain.repository.EmpresaRepository
import com.appgestion.gestionempresa.domain.repository.FacturaRepository
import com.appgestion.gestionempresa.domain.repository.FichajeRepository
import com.appgestion.gestionempresa.domain.repository.OfertaRepository
import com.appgestion.gestionempresa.domain.repository.TareaRepository
import com.appgestion.gestionempresa.domain.repository.UserRepository
import com.appgestion.gestionempresa.domain.repository.VacacionesRepository
import com.appgestion.gestionempresa.domain.usecase.empresa.AddTrabajadorByEmailUseCase
import com.appgestion.gestionempresa.domain.usecase.empresa.CreateOfertaUseCase
import com.appgestion.gestionempresa.domain.usecase.empresa.DeleteOfertaUseCase
import com.appgestion.gestionempresa.domain.usecase.empresa.GetAllOfertasUseCase
import com.appgestion.gestionempresa.domain.usecase.empresa.GetOfertaByIdUseCase
import com.appgestion.gestionempresa.domain.usecase.empresa.GetOfertasByEmpresaUseCase
import com.appgestion.gestionempresa.domain.usecase.empresa.TareaUseCases
import com.appgestion.gestionempresa.domain.usecase.trabajador.CreateCVUseCase
import com.appgestion.gestionempresa.domain.usecase.trabajador.FichajeUseCases
import com.appgestion.gestionempresa.domain.usecase.trabajador.FinalizarFichajeUseCase
import com.appgestion.gestionempresa.domain.usecase.trabajador.GetCandidaturasPorOfertaUseCase
import com.appgestion.gestionempresa.domain.usecase.trabajador.GetMisCandidaturasUseCase
import com.appgestion.gestionempresa.domain.usecase.trabajador.GetMisVacacionesUseCase
import com.appgestion.gestionempresa.domain.usecase.trabajador.GetMyCVsUseCase
import com.appgestion.gestionempresa.domain.usecase.trabajador.GetVacacionesPorEmpresaUseCase
import com.appgestion.gestionempresa.domain.usecase.trabajador.IniciarFichajeUseCase
import com.appgestion.gestionempresa.domain.usecase.trabajador.ObtenerFichajesUseCase
import com.appgestion.gestionempresa.domain.usecase.trabajador.SendCVUseCase
import com.appgestion.gestionempresa.domain.usecase.trabajador.SubmitVacacionesUseCase
import com.appgestion.gestionempresa.domain.usecase.trabajador.UpdateCandidaturaStatusUseCase
import com.appgestion.gestionempresa.domain.usecase.trabajador.UpdateVacacionesStatusUseCase
import com.appgestion.gestionempresa.domain.usecase.user.GetUserByIdUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() = Firebase.auth

    @Provides
    @Singleton
    fun provideFirebaseStorage() = Firebase.storage

    @Provides
    @Singleton
    fun provideFirebaseFireStore() = Firebase.firestore

    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): AuthRepository = AuthRepositoryImpl(auth, firestore)

    @Provides
    @Singleton
    fun provideEmpresaRepository(
        firestore: FirebaseFirestore
    ): EmpresaRepository = EmpresaRepositoryImpl(firestore)

    @Provides
    fun provideAddTrabajadorByEmailUseCase(
        empresaRepository: EmpresaRepository
    ): AddTrabajadorByEmailUseCase = AddTrabajadorByEmailUseCase(empresaRepository)

    @Provides
    fun provideOfertaRepository(firestore: FirebaseFirestore): OfertaRepository =
        OfertaRepositoryImpl(firestore)

    @Provides
    fun provideCreateOfertaUseCase(
        ofertaRepository: OfertaRepository
    ): CreateOfertaUseCase {
        return CreateOfertaUseCase(ofertaRepository)
    }

    @Provides fun provideDeleteOfertaUseCase(
        repo: OfertaRepository
    ): DeleteOfertaUseCase = DeleteOfertaUseCase(repo)

    @Provides
    fun provideGetOfertasByEmpresaUseCase(
        repo: OfertaRepository
    ) = GetOfertasByEmpresaUseCase(repo)


    @Provides
    fun provideGetAllOfertasUseCase(
        repo: OfertaRepository
    ): GetAllOfertasUseCase = GetAllOfertasUseCase(repo)


    @Provides
    fun provideGetOfertaByIdUseCase(
        repo: OfertaRepository
    ) = GetOfertaByIdUseCase(repo)


    @Provides
    @Singleton
    fun provideFacturaRepository(
        firestore: FirebaseFirestore,
        storage: FirebaseStorage
    ): FacturaRepository = FacturaRepositoryImpl(storage, firestore)

    @Provides
    @Singleton
    fun provideFichajeRepository(
        firestore: FirebaseFirestore
    ): FichajeRepository = FichajeRepositoryImpl(firestore)

    @Provides
    fun provideTareaUseCases(tareaRepository: TareaRepository): TareaUseCases {
        return TareaUseCases(tareaRepository)
    }

    @Provides
    @Singleton
    fun provideTareaRepository(
        firestore: FirebaseFirestore
    ): TareaRepository = TareaRepositoryImpl(firestore)

    @Provides
    @Singleton
    fun provideFichajeUseCases(
        repo: FichajeRepository
    ): FichajeUseCases {
        return FichajeUseCases(
            iniciarFichaje = IniciarFichajeUseCase(repo),
            finalizarFichaje = FinalizarFichajeUseCase(repo),
            obtenerFichajes = ObtenerFichajesUseCase(repo)
        )
    }

    @Provides fun provideCurriculumRepository(
        firestore: FirebaseFirestore
    ): CurriculumRepository = CurriculumRepositoryImpl(firestore)

    @Provides fun provideCreateCVUseCase(repo: CurriculumRepository) =
        CreateCVUseCase(repo)
    @Provides fun provideGetMyCVsUseCase(repo: CurriculumRepository) =
        GetMyCVsUseCase(repo)
    @Provides fun provideSendCVUseCase(repo: CurriculumRepository) =
        SendCVUseCase(repo)

    @Provides fun provideCandidaturaRepo(
        firestore: FirebaseFirestore
    ): CandidaturaRepository = CandidaturaRepositoryImpl(firestore)

    @Provides fun provideGetMisCandidaturas(
        repo: CandidaturaRepository
    ) = GetMisCandidaturasUseCase(repo)
    @Provides fun provideGetCandidaturasPorOferta(
        repo: CandidaturaRepository
    ) = GetCandidaturasPorOfertaUseCase(repo)
    @Provides fun provideUpdateCandidaturaStatus(
        repo: CandidaturaRepository
    ) = UpdateCandidaturaStatusUseCase(repo)

    @Provides
    @Singleton
    fun provideVacacionesRepository(
        firestore: FirebaseFirestore
    ): VacacionesRepository =
        VacacionesRepositoryImpl(firestore)

    @Provides
    fun provideSubmitVacacionesUseCase(
        repo: VacacionesRepository
    ): SubmitVacacionesUseCase =
        SubmitVacacionesUseCase(repo)

    @Provides
    fun provideGetMisVacacionesUseCase(
        repo: VacacionesRepository
    ): GetMisVacacionesUseCase =
        GetMisVacacionesUseCase(repo)

    @Provides
    fun provideGetVacacionesPorEmpresaUseCase(
        repo: VacacionesRepository
    ): GetVacacionesPorEmpresaUseCase =
        GetVacacionesPorEmpresaUseCase(repo)

    @Provides
    fun provideUpdateVacacionesStatusUseCase(
        repo: VacacionesRepository
    ): UpdateVacacionesStatusUseCase =
        UpdateVacacionesStatusUseCase(repo)


    @Provides
    @Singleton
    fun provideUserRepository(
        firestore: FirebaseFirestore
    ): UserRepository = UserRepositoryImpl(firestore)

    @Provides
    fun provideGetUserByIdUseCase(repo: UserRepository): GetUserByIdUseCase =
        GetUserByIdUseCase(repo)

}




