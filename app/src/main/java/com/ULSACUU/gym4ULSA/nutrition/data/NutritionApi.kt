package com.ULSACUU.gym4ULSA.nutrition.data

import com.ULSACUU.gym4ULSA.nutrition.model.Food
import retrofit2.http.GET

/**
 * Retrofit API that points to the local backend (10.0.2.2) instead of Gists.
 * Each collection is exposed via the generic CRUD route documented by el backend.
 */
interface NutritionApi {

    @GET("api/crud/nutrition_settings")
    suspend fun getNutritionSettings(): List<NutritionSettingsDocument>

    @GET("api/crud/foods")
    suspend fun getFoods(): List<Food>

    @GET("api/crud/meals")
    suspend fun getMeals(): List<MealDocument>

    companion object {
        const val BASE_URL = "http://10.0.2.2:4001/"
    }
}
