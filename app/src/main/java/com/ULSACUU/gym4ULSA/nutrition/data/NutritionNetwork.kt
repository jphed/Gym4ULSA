package com.ULSACUU.gym4ULSA.nutrition.data

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import com.google.gson.reflect.TypeToken
import com.ULSACUU.gym4ULSA.nutrition.model.Meal

object NutritionNetwork {
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val gson = GsonBuilder()
        .registerTypeAdapter(
            TypeToken.getParameterized(List::class.java, Meal::class.java).type,
            MealListAdapter()
        )
        .create()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(NutritionApi.BASE_URL)
        // Scalars first to allow String responses
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    val api: NutritionApi = retrofit.create(NutritionApi::class.java)
}
