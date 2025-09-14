package com.jorgeromo.androidClassMp1.firstpartial.login.model.repository

import com.jorgeromo.androidClassMp1.firstpartial.login.model.dto.LoginRequest
import com.jorgeromo.androidClassMp1.firstpartial.login.model.dto.LoginResponse
import com.jorgeromo.androidClassMp1.firstpartial.login.model.network.AuthApi

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
        - Devuelve el body convertido en LoginResponse.
        - Si el body es nulo, devuelve un LoginResponse con error.
     4. Si la respuesta es un error (HTTP 400, 401, etc.):
        - Lee el cuerpo de error (JSON).
        - Intenta parsear el mensaje que manda el backend.
        - Devuelve un LoginResponse con success=false y ese mensaje.
     5. Si ocurre IOException (sin internet, timeout):
        - Devuelve un LoginResponse con mensaje de red.
     6. Si ocurre cualquier otra excepción:
        - Devuelve un LoginResponse con mensaje genérico.
    */
    suspend fun login(email: String, password: String): LoginResponse {
        return try {
            val resp = api.login(LoginRequest(email, password))
            if (resp.isSuccessful) {
                resp.body() ?: LoginResponse(false, "Respuesta vacía del servidor")
            } else {
                val msg = resp.errorBody()?.string().orEmpty()
                val parsed = try {
                    org.json.JSONObject(msg).optString("message", "")
                } catch (_: Exception) { "" }
                LoginResponse(false, parsed.ifBlank { "Credenciales inválidas" })
            }
        } catch (_: java.io.IOException) {
            LoginResponse(false, "Sin conexión. Verifica tu red.")
        } catch (_: Exception) {
            LoginResponse(false, "Error inesperado")
        }
    }
}