package com.jorgeromo.androidClassMp1.firstpartial.login.model.network


import com.jorgeromo.androidClassMp1.firstpartial.login.model.dto.LoginRequest
import com.jorgeromo.androidClassMp1.firstpartial.login.model.dto.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/*
 AuthApi

 Esta interfaz define los endpoints de autenticación
 que la app puede consumir usando Retrofit.

 Retrofit se encarga de generar automáticamente
 la implementación de esta interfaz en tiempo de ejecución.

 Aquí tenemos un único endpoint: login.
*/
interface AuthApi {

    /*
     Endpoint: POST /api/auth/login

     Envía un objeto LoginRequest (con email y password)
     en el cuerpo de la petición.

     Devuelve un Response<LoginResponse>, que incluye:
     - Código HTTP (200, 400, etc.)
     - El body parseado como LoginResponse

     Es una función suspend, por lo que debe llamarse
     dentro de una coroutine.
    */
    @POST("/api/auth/login")
    suspend fun login(@Body req: LoginRequest): Response<LoginResponse>
}