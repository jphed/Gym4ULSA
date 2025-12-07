package com.ULSACUU.gym4ULSA.login.model.repository

import com.ULSACUU.gym4ULSA.login.model.dto.Goal
import com.ULSACUU.gym4ULSA.login.model.dto.LoginRequest
import com.ULSACUU.gym4ULSA.login.model.dto.LoginResponse
import com.ULSACUU.gym4ULSA.login.model.dto.SignupRequest
import com.ULSACUU.gym4ULSA.login.model.dto.SignupResponse
import com.ULSACUU.gym4ULSA.login.model.network.AuthApi

/*
 AuthRepository

 Esta clase actúa como un puente entre el ViewModel y la capa de red (AuthApi).

 Su responsabilidad es:
 - Construir las peticiones que se enviarán al servidor.
 - Manejar la respuesta de Retrofit (éxito o error).
 - Devolver un objeto LoginResponse listo para que lo use el ViewModel.

 De esta manera, el ViewModel no se preocupa de cómo se hace
 la llamada HTTP ni cómo se parsea el error: simplemente obtiene
 un resultado ya procesado.
*/
class AuthRepository(private val api: AuthApi) {

    /*
     login(email, password)

     Esta función es suspend (puede usarse en coroutines) y
     realiza la llamada al endpoint de login.

     Flujo:
     1. Construye un LoginRequest con email y password.
     2. Llama al método api.login() de Retrofit.
     3. Si la respuesta es exitosa (HTTP 200):
        - Devuelve el body que contiene el usuario completo.
        - Si el body es nulo, lanza una excepción.
     4. Si la respuesta es un error (HTTP 400, 401, etc.):
        - Lee el cuerpo de error (JSON).
        - Intenta parsear el mensaje que manda el backend.
        - Lanza una excepción con el mensaje de error.
     5. Si ocurre IOException (sin internet, timeout):
        - Lanza una excepción con mensaje de red.
     6. Si ocurre cualquier otra excepción:
        - Lanza una excepción con mensaje genérico.
    */
    suspend fun login(email: String, password: String): LoginResponse {
        return try {
            val resp = api.login(LoginRequest(email, password))
            if (resp.isSuccessful) {
                resp.body() ?: throw Exception("Respuesta vacía del servidor")
            } else {
                val msg = resp.errorBody()?.string().orEmpty()
                val parsed = try {
                    val json = org.json.JSONObject(msg)
                    json.optString("message") ?: json.optString("error") ?: msg
                } catch (_: Exception) { msg }
                throw Exception(parsed.ifBlank { "Credenciales inválidas" })
            }
        } catch (_: java.io.IOException) {
            throw Exception("Sin conexión. Verifica tu red.")
        } catch (_: Exception) {
            throw Exception("Error inesperado")
        }
    }

    /*
     signup(name, email, password, age, sex, height_cm, weight_kg, goal, activity_level)

     Esta función es suspend (puede usarse en coroutines) y
     realiza la llamada al endpoint de registro.

     Flujo:
     1. Construye un SignupRequest con todos los datos del usuario.
     2. Llama al método api.signup() de Retrofit.
     3. Si la respuesta es exitosa (HTTP 200):
        - Devuelve el body que contiene token y usuario.
        - Si el body es nulo, lanza una excepción.
     4. Si la respuesta es un error (HTTP 400, 409, etc.):
        - Lee el cuerpo de error (JSON).
        - Intenta parsear el mensaje que manda el backend.
        - Lanza una excepción con el mensaje de error.
     5. Si ocurre IOException (sin internet, timeout):
        - Lanza una excepción con mensaje de red.
     6. Si ocurre cualquier otra excepción:
        - Lanza una excepción con mensaje genérico.
    */
    suspend fun signup(
        name: String,
        email: String,
        password: String,
        age: String,
        sex: String,
        height_cm: String,
        weight_kg: String,
        goal: Goal,
        activity_level: String,
        experiencia: String,
        created_utc: String = java.time.Instant.now().toString()
    ): SignupResponse {
        return try {
            val resp = api.signup(
                SignupRequest(
                    name,
                    email,
                    password,
                    age,
                    sex,
                    height_cm,
                    weight_kg,
                    goal,
                    activity_level,
                    experiencia,
                    created_utc
                )
            )

            if (resp.isSuccessful) {
                resp.body() ?: throw Exception("Respuesta vacía del servidor")
            } else {
                val msg = resp.errorBody()?.string().orEmpty()
                val parsed = try {
                    val json = org.json.JSONObject(msg)
                    json.optString("message") ?: json.optString("error") ?: msg
                } catch (_: Exception) { msg }
                throw Exception(parsed.ifBlank { "Error al registrar usuario" })
            }
        } catch (_: java.io.IOException) {
            throw Exception("Sin conexión. Verifica tu red.")
        } catch (_: Exception) {
            throw Exception("Error inesperado")
        }
    }
}