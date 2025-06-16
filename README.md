# Gestión de Empresa – Proyecto Integrado

Aplicación Android multiplataforma para la gestión integral de la relación empresa–trabajador, desarrollada en **Kotlin** con **Jetpack Compose**. Proyecto Integrado (PI).

---

## 📱 Características principales

- **Autenticación y registro**  
  - Login / registro con **Firebase Auth** (correo + contraseña).  
  - Diferenciación de roles: **Empresa** y **Trabajador**.

- **Flujo Empresa**  
  - Perfil de empresa: datos básicos, listado de trabajadores vinculados.  
  - Gestión de ofertas: crear, editar, eliminar y ver candidaturas.  
  - Gestión de tareas: asignar tareas a trabajadores, estado (pendiente/completada).  
  - Gestión de vacaciones: aprobar/rechazar solicitudes de días libres.  
  - Gestión de facturas: subir imágenes de facturas y ver histórico.

- **Flujo Trabajador**  
  - Perfil de trabajador: datos personales, CV editable.  
  - Visualización de ofertas disponibles y detalle completo (descripción, salario, requisitos).  
  - Inscripción a ofertas (solicitud de candidatura).  
  - Fichaje de jornada: inicio/fin de sesión con resumen diario/semanal.  
  - Solicitud de vacaciones con selector de rango de fechas.  
  - Visualización de tareas asignadas.

- **Arquitectura y calidad**  
  - **MVVM** + **Use Cases** + repositorios.  
  - **Inyección de dependencias** con Hilt.  
  - **Kotlin Coroutines** y **StateFlow** para asincronía y estados reactivos.  
  - UI con **Material3**, componentes adaptativos y accesibilidad.  
  - Control de versiones en **Git** y despliegue en **GitHub**.

---

## 🛠️ Tecnologías y librerías

- **Lenguaje**: Kotlin 1.9  
- **UI**: Jetpack Compose (Material3, Navigation-Compose)  
- **Backend**: Firebase (Auth, Firestore, Storage)  
- **DI**: Hilt  
- **Arquitectura**: MVVM + Clean Architecture (Data / Domain / UI)  
- **Navegación**: Navigation Compose  
- **Persistencia**: Firestore + Firebase Storage  
- **Asincronía**: Coroutines + StateFlow  
- **Versionado**: Git + GitHub  
- **Build**: Gradle Kotlin DSL  

---

## 📂 Estructura del proyecto

/app
├─ data
│ ├─ model # DTOs / Entities
│ ├─ repository # Implementaciones de repositorio (Auth, Oferta, Tarea…)
├─ domain
│ ├─ model # Entidades de negocio
│ ├─ repository # Interfaces de repositorio
│ └─ usecase # Casos de uso
├─ ui
│ ├─ components # Composables reutilizables
│ ├─ navigation # AppScaffold, NavGraph
│ └─ perfiles
│ ├─ empresa
│ └─ trabajador
└─ MainActivity.kt

📅 Próximas mejoras
Aumentar la cobertura de tests (unitarios e instrumentados).

Actualizar modulo facturas con OCR para lectura de facturas.

Push notifications para eventos (nuevas candidaturas, aprobaciones…).

Panel web complementario para gestión desde navegador.

Optimización de rendimiento y caching local (Room / DataStore).

🧑‍💻 Autor
Rubén Pérez Arias
Estudiante de Desarrollo de Aplicaciones Multiplataforma.
Grado Superior (PI – curso 2024-2025)
