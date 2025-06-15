package com.appgestion.gestionempresa.data.model

sealed class Response<out T> {
    data class Success<out T>(val data: T) : Response<T>()
    data class Failure(val exception: Throwable) : Response<Nothing>()
    object Loading : Response<Nothing>()
}