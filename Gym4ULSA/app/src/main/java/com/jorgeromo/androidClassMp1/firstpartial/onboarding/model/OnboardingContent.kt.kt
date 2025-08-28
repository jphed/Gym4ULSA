package com.jorgeromo.androidClassMp1.firstpartial.onboarding.model
import com.jorgeromo.androidClassMp1.R

/**
 * Contenido del onboarding
 * Las paginas que tendra el onboarding
 */
object OnboardingContent {
    val pages = listOf(
        OnboardingPageModel(
            imageRes = R.drawable.onb_1,
            title = "Tu mejor version empieza hoy!",
            description = "No esperes el momento perfecto, empieza y haz que suceda."
        ),
        OnboardingPageModel(
            imageRes = R.drawable.onb_2,
            title = "Rompe tus limites!",
            description = "Cada repetición es un paso más hacia tu meta."
        ),
        OnboardingPageModel(
            imageRes = R.drawable.onb_3,
            title = "El progreso se construye con constancia!",
            description = "La disciplina de hoy es el resultado de mañana."
        )
    )
}