package com.ULSACUU.gym4ULSA.nutrition.data

import com.ULSACUU.gym4ULSA.nutrition.model.CalorieAdjustment
import com.ULSACUU.gym4ULSA.nutrition.model.MacroSplitPercent
import com.ULSACUU.gym4ULSA.nutrition.model.MacroTargets
import com.ULSACUU.gym4ULSA.nutrition.model.Meal
import com.ULSACUU.gym4ULSA.nutrition.model.MealPlanDay
import com.ULSACUU.gym4ULSA.nutrition.model.MealSummary
import com.ULSACUU.gym4ULSA.nutrition.model.NutritionSettings
import com.ULSACUU.gym4ULSA.nutrition.model.Units
import com.google.gson.annotations.SerializedName

/**
 * Raw document coming from /api/crud/nutrition_settings.
 * Some entries hold actual settings, others hold meta data such as unit system.
 */
data class NutritionSettingsDocument(
    @SerializedName("_id") val id: String? = null,
    val version: String? = null,
    val system: String? = null,
    val weight: String? = null,
    val height: String? = null,
    val energy: String? = null,
    val formula: String? = null,
    val bmr_kcal: Int? = null,
    val tdee_kcal: Int? = null,
    val calorie_adjustment: CalorieAdjustment? = null,
    val calorie_target_kcal: Int? = null,
    val macro_split_percent: MacroSplitPercent? = null,
    val macro_targets: MacroTargets? = null
) {
    fun asNutritionSettings(): NutritionSettings? {
        if (
            formula == null &&
            bmr_kcal == null &&
            tdee_kcal == null &&
            calorie_adjustment == null &&
            calorie_target_kcal == null &&
            macro_split_percent == null &&
            macro_targets == null
        ) {
            return null
        }
        return NutritionSettings(
            formula = formula,
            bmr_kcal = bmr_kcal,
            tdee_kcal = tdee_kcal,
            calorie_adjustment = calorie_adjustment,
            calorie_target_kcal = calorie_target_kcal,
            macro_split_percent = macro_split_percent,
            macro_targets = macro_targets
        )
    }

    fun asUnits(): Units? {
        if (system == null && weight == null && height == null && energy == null) return null
        return Units(
            system = system,
            weight = weight,
            height = height,
            energy = energy
        )
    }
}

/**
 * Raw meal document returned by /api/crud/meals.
 * We only keep the values necessary for the MealPlanDay UI.
 */
data class MealDocument(
    @SerializedName("_id") val id: String? = null,
    val user_id: String? = null,
    val date: String? = null,
    val meals: List<Meal>? = null,
    val summary: MealSummary? = null
) {
    fun asMealPlanDay(): MealPlanDay? {
        val finalMeals = meals ?: emptyList()
        if (date == null && finalMeals.isEmpty() && summary == null) return null
        return MealPlanDay(
            date = date,
            meals = finalMeals,
            summary = summary
        )
    }
}
