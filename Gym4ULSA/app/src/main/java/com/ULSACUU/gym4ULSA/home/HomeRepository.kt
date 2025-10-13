package com.ULSACUU.gym4ULSA.home

import RoutinesResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

interface RoutinesApi {
    // Aquí ponemos la URL completa, igual que Nutrición
    @GET
    suspend fun getRoutines(
        @Url url: String = "https://gist.githubusercontent.com/YajahiraPP/5d64c24ac355f7c309eeb7d3cdc614b3/raw/13db2f8550ce2f20961f3289d5b0c8d7a5169fef/routines.json"
    ): RoutinesResponse
}

object RoutinesRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://gist.githubusercontent.com/") // Base genérica
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: RoutinesApi = retrofit.create(RoutinesApi::class.java)

    suspend fun fetchRoutines() = api.getRoutines() // ya no dará 404
}
