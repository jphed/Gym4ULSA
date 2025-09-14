package com.jorgeromo.androidClassMp1.firstpartial.login.model.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*
 RetrofitProvider

 Este objeto configura y provee una instancia de Retrofit
 lista para usarse en las llamadas a la API del backend.

 Contiene:
 - BASE_URL: dirección base de tu servidor backend.
 - HttpLoggingInterceptor: permite ver en el logcat todas las
   peticiones y respuestas HTTP (útil para debug).
 - OkHttpClient: cliente HTTP al que se le añade el interceptor
   de logging.
 - Retrofit: instancia principal de Retrofit configurada con
   la base URL, el conversor JSON (Gson) y el cliente HTTP.
 - authApi: implementación automática de la interfaz AuthApi,
   que contiene los endpoints definidos para autenticación.
*/
object RetrofitProvider {
    private const val BASE_URL = "https://eleventenbackend.onrender.com"

    // Interceptor para loguear requests/responses HTTP
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Cliente HTTP configurado con el interceptor de logging
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    // Instancia de Retrofit con base URL, Gson y cliente HTTP
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    // Implementación de la interfaz AuthApi lista para usarse
    val authApi: AuthApi = retrofit.create(AuthApi::class.java)
}