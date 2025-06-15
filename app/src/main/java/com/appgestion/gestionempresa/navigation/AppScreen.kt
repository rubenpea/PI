package com.appgestion.gestionempresa.navigation

import com.appgestion.gestionempresa.ui.perfiles.empresa.ProfileScreen

sealed class AppScreen(val route: String, val title: String, val showBottomBar: Boolean = true) {


    object SplashScreen : AppScreen("splash", "", showBottomBar = false)

    object LoginScreen : AppScreen("login_screen", "Inciar Sesion",  false)

    object RegistroEmpresaScreen : AppScreen("registro_empresa_screen", "Registro Empresa", showBottomBar = false)

    object SetupEmpresaScreen : AppScreen("setup_empresa", "Configura Empresa", showBottomBar = false)

    object RegistroTrabajadorScreen : AppScreen("registro_trabajador_screen", "Registro Trabajador", showBottomBar = false)

    object RecupararScreen : AppScreen("recuperar_screen", "Recuperar Contraseña", showBottomBar = false)

    object HomeTrabajador : AppScreen("hom_trabajador", "Inicio")

    object HomeEmpresa : AppScreen("hom_empresa", "Inicio", showBottomBar = true)
    object Profile           : AppScreen("profile",       "Perfil", false)

    object OfertaScreen : AppScreen(
        route = "oferta_screen/{empresaId}",
        title = "Crear Oferta"
    ) {
        fun createRoute(empresaId: String) = "oferta_screen/$empresaId"
    }

    object MisOfertasEmpresaScreen : AppScreen(
        route = "mis_ofertas_empresa/{empresaId}",
        title = "Mis Ofertas"
    ) {
        fun createRoute(empresaId: String) = "mis_ofertas_empresa/$empresaId"
    }

    object MensajeScreen : AppScreen("mensaje_screen", "Mensaje")

    object FacturaScreen : AppScreen("factura_screen/{idEmpresa}", "Facturas") {
        fun createRoute(idEmpresa: String) = "factura_screen/$idEmpresa"
    }
    object FacturaListScreen : AppScreen("factura_list_screen/{idEmpresa}", "Mis Facturas") {
        fun createRoute(idEmpresa: String) = "factura_list_screen/$idEmpresa"
    }

    object FacturaListScreenNav : AppScreen(
        route = "factura_list_screen/{idEmpresa}",
        title = "Mis Facturas"
    ) {
        fun createRoute(idEmpresa: String) = "factura_list_screen/$idEmpresa"
    }

    object FacturaUploadScreenNav : AppScreen(
        route = "factura_screen/{idEmpresa}",
        title = "Subir Factura"
    ) {
        fun createRoute(idEmpresa: String) = "factura_screen/$idEmpresa"
    }

    object TareasEmpresaScreen : AppScreen("tareas_empresa_screen/{idEmpresa}", "Crear Tarea") {
        fun createRoute(idEmpresa: String) = "tareas_empresa_screen/$idEmpresa"
    }

    object VacacionesEmpresaScreen : AppScreen("vacaciones_empresa/{empresaId}", "Vacaciones Pedidas") {
        fun createRoute(empresaId: String) = "vacaciones_empresa/$empresaId"
    }


    //rutas Usuario Rol Trabajador.
    object TareasTrabajadorScreen : AppScreen("tarea_trabajador_screen", "Mis Tareas")

    object OfertasDisponiblesScreen : AppScreen(
        "ofertas_disponibles/{empresaId}",
        "Ofertas"
    ) {
        fun createRoute(empresaId: String) = "ofertas_disponibles/$empresaId"
    }
    object FichajeScreen : AppScreen("fichaje_Screen", "Fichaje")
    object ResumenSemanalScreen : AppScreen("resumen_fichaje_semanal_screen", "Resumen Fichaje")
    object HistorialTareasScreen : AppScreen("historial_tareas_screen", "Tareas Realizadas")
    object OfertasTrabajadorScreen : AppScreen("ofertas_trabajador", "Ofertas")
    object OfertaDetailScreen : AppScreen("offerDetail/{ofertaId}", "Detalles de Ofertas") {
        fun createRoute(ofertaId: String) = "offerDetail/$ofertaId"
    }

    object CVFormScreen : AppScreen("cv_form", "Crear Curriculum")

    object MisCandidaturasScreen : AppScreen("mis_candidaturas", "Ofertas Inscritas")

    object CandidaturasOfertaScreen : AppScreen("candidaturas/{ofertaId}", "Candidatos") {
        fun createRoute(ofertaId: String) = "candidaturas/$ofertaId"
    }

    object CvDetailScreen : AppScreen("cv_detail/{cvId}", "Detalles Cv") {
        // helper para generar rutas
        fun createRoute(cvId: String) = "cv_detail/$cvId"
    }

    object VacacionesScreen : AppScreen("vacaciones_screen", "Vacaciones")

    object VacacionesSolicitadasScreen : AppScreen("vacaciones_solicitudes_screen", "Vacaciones Solicitadas")

    object EmpresaTrabajadoresScreen : AppScreen("lista_trabajadores", "Trabajadores", true)



    companion object {
        /** listado con todas las pantallas para facilitar búsquedas dinámicas */
        val all = listOf(
            LoginScreen,
            RegistroEmpresaScreen,
            SetupEmpresaScreen,
            RegistroTrabajadorScreen,
            RecupararScreen,

            HomeTrabajador,


            EmpresaTrabajadoresScreen,
            MisOfertasEmpresaScreen,
            CvDetailScreen,

            FacturaUploadScreenNav,
            FacturaListScreenNav,

            HomeEmpresa,
            Profile,
            OfertaScreen,
            MensajeScreen,
            FacturaScreen,
            FacturaListScreen,
            TareasEmpresaScreen,
            VacacionesEmpresaScreen,
            CandidaturasOfertaScreen,

            HomeTrabajador,
            OfertasTrabajadorScreen,
            OfertaDetailScreen,
            MisCandidaturasScreen,
            TareasTrabajadorScreen,
            VacacionesScreen,
            VacacionesSolicitadasScreen,
            FichajeScreen,
            ResumenSemanalScreen,
            HistorialTareasScreen,
            CVFormScreen,
        )
    }
}



