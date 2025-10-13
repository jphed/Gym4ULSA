package com.ULSACUU.gym4ULSA.home

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface RoutinesApi {
    @GET("13db2f8550ce2f20961f3289d5b0c8d7a5169fef/routines.json")
    suspend fun getRoutines(): RoutinesResponse
}

object RoutinesRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://gist.githubusercontent.com/YajahiraPP/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: RoutinesApi = retrofit.create(RoutinesApi::class.java)

    suspend fun fetchRoutines() = api.getRoutines()
}
