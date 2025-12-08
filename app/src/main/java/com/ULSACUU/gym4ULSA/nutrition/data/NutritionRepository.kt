package com.ULSACUU.gym4ULSA.nutrition.data

import com.ULSACUU.gym4ULSA.nutrition.model.RootNutrition

class NutritionRepository(private val api: NutritionApi) {

    suspend fun fetchNutrition(): RootNutrition {
        val settingsDocs = api.getNutritionSettings()
        val foods = api.getFoods()
        val meals = api.getMeals()

        val nutritionSettings = settingsDocs.firstNotNullOfOrNull { it.asNutritionSettings() }
        val units = settingsDocs.firstNotNullOfOrNull { it.asUnits() }
        val mealPlanDay = meals.firstNotNullOfOrNull { it.asMealPlanDay() }

        return RootNutrition(
            version = settingsDocs.firstOrNull()?.version,
            units = units,
            user_profile = null,
            goal = null,
            nutrition_settings = nutritionSettings,
            food_database = foods,
            meal_plan_day = mealPlanDay,
            supplements_catalog = null,
            supplements_selection = null
        )
    }
}
