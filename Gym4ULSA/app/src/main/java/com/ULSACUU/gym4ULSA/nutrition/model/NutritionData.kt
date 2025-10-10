package com.ULSACUU.gym4ULSA.nutrition.model

// Root JSON structure from the provided Gist
// Only the fields used in UI/VM are strictly required, but we mirror the schema for flexibility

data class Units(
    val system: String?,
    val weight: String?,
    val height: String?,
    val energy: String?
)

data class UserProfile(
    val age: Int?,
    val sex: String?,
    val height_cm: Int?,
    val weight_kg: Double?,
    val activity_level: String?,
    val body_fat_percent: Double?,
    val allergies: List<String>?,
    val dietary_preferences: List<String>?,
    val last_updated_utc: String?
)

data class GoalRate(
    val unit: String?,
    val value: Double?
)

data class Goal(
    val type: String?,
    val target_weight_kg: Double?,
    val rate: GoalRate?,
    val timeline_days: Int?
)

data class CalorieAdjustment(
    val mode: String?,
    val kcal: Int?
)

data class MacroSplitPercent(
    val protein: Int?,
    val carbs: Int?,
    val fat: Int?
)

data class MacroTargets(
    val protein_g: Int?,
    val carbs_g: Int?,
    val fat_g: Int?
)

data class NutritionSettings(
    val formula: String?,
    val bmr_kcal: Int?,
    val tdee_kcal: Int?,
    val calorie_adjustment: CalorieAdjustment?,
    val calorie_target_kcal: Int?,
    val macro_split_percent: MacroSplitPercent?,
    val macro_targets: MacroTargets?
)

data class Serving(
    val id: String,
    val label: String,
    val grams: Double
)

data class Per100g(
    val energy_kcal: Double?,
    val protein_g: Double?,
    val carbs_g: Double?,
    val fat_g: Double?,
    val fiber_g: Double?,
    val sugar_g: Double?
)

data class Food(
    val id: String,
    val name: String,
    val servings: List<Serving>,
    val per_100g: Per100g,
    val tags: List<String>?,
    val image_url: String? = null
)

data class Computed(
    val grams: Double?,
    val energy_kcal: Double?,
    val protein_g: Double?,
    val carbs_g: Double?,
    val fat_g: Double?,
    val fiber_g: Double?,
    val sugar_g: Double?
)

data class MealEntry(
    val food_id: String,
    val serving_id: String,
    val quantity: Double,
    val computed: Computed?
)

data class MealBlock(
    val name: String,
    val items: List<MealEntry>
)

data class MealSummary(
    val energy_kcal: Double?,
    val protein_g: Double?,
    val carbs_g: Double?,
    val fat_g: Double?,
    val fiber_g: Double?,
    val sugar_g: Double?
)

data class MealPlanDay(
    val date: String?,
    val meals: List<MealBlock>,
    val summary: MealSummary?
)

data class SupplementServing(
    val label: String,
    val grams: Double
)

data class SupplementPerServing(
    val energy_kcal: Double?,
    val protein_g: Double?,
    val carbs_g: Double?,
    val fat_g: Double?
)

data class Supplement(
    val id: String,
    val name: String,
    val brand: String?,
    val category: String?,
    val serving: SupplementServing?,
    val per_serving: SupplementPerServing?,
    val price: Price?,
    val in_stock: Boolean?,
    val image_url: String? = null
)

data class Price(
    val currency: String?,
    val value: Double?
)

data class SupplementsSelection(
    val supplement_id: String,
    val quantity: Int
)

data class RootNutrition(
    val version: String?,
    val units: Units?,
    val user_profile: UserProfile?,
    val goal: Goal?,
    val nutrition_settings: NutritionSettings?,
    val food_database: List<Food>?,
    val meal_plan_day: MealPlanDay?,
    val supplements_catalog: List<Supplement>?,
    val supplements_selection: List<SupplementsSelection>?
)
