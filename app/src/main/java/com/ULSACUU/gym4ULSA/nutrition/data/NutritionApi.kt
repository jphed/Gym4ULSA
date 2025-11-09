package com.ULSACUU.gym4ULSA.nutrition.data

import retrofit2.http.GET

/**
 * Retrofit API to fetch the Nutrition JSON from the provided Gist (raw).
 * We return raw String and parse manually to be tolerant to non-JSON wrappers.
 */
interface NutritionApi {
    // Fetch the exact raw file to ensure pure JSON (no HTML/JS wrappers)
    @GET("jphed/6d69347246db844f40cc68bc32e595e8/raw/gistfile1.txt")
    suspend fun getNutritionRaw(): String

    companion object {
        const val BASE_URL = "https://gist.githubusercontent.com/"
    }
}
