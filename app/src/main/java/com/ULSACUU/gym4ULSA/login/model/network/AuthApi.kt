package com.ULSACUU.gym4ULSA.login.model.network


import com.ULSACUU.gym4ULSA.login.model.dto.LoginRequest
import com.ULSACUU.gym4ULSA.login.model.dto.LoginResponse
import com.ULSACUU.gym4ULSA.login.model.dto.SignupRequest
import com.ULSACUU.gym4ULSA.login.model.dto.SignupResponse
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

    /*
     Endpoint: POST /api/auth/signup

     Envía un objeto SignupRequest (con name, email y password)
     en el cuerpo de la petición.

     Devuelve un Response<SignupResponse>, que incluye:
     - Código HTTP (200, 400, etc.)
     - El body parseado como SignupResponse

     Es una función suspend, por lo que debe llamarse
     dentro de una coroutine.
    */
    @POST("/api/auth/signup")
    suspend fun signup(@Body req: SignupRequest): Response<SignupResponse>
}