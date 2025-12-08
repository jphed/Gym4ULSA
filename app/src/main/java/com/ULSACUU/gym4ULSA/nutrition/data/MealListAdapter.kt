package com.ULSACUU.gym4ULSA.nutrition.data

import com.ULSACUU.gym4ULSA.nutrition.model.Meal
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

/**
 * Some documents returned by /api/crud/meals contain placeholder strings inside the
 * meals[] array (e.g. ["Breakfast","Lunch","Dinner"]). This adapter skips non-object
 * entries so Gson won't crash expecting a Meal object.
 */
class MealListAdapter : JsonDeserializer<List<Meal>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): List<Meal> {
        if (json == null || !json.isJsonArray || context == null) return emptyList()

        val result = mutableListOf<Meal>()
        json.asJsonArray.forEach { element ->
            if (element.isJsonObject) {
                runCatching {
                    context.deserialize<Meal>(element, Meal::class.java)
                }.getOrNull()?.let { result.add(it) }
            }
        }
        return result
    }
}
