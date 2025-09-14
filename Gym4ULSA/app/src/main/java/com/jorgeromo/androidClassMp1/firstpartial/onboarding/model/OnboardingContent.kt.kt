package com.jorgeromo.androidClassMp1.firstpartial.onboarding.model
import com.jorgeromo.androidClassMp1.R

/**
 * Contenido del onboarding
 * Las paginas que tendra el onboarding
 */
object OnboardingContent {
    val pages = listOf(
        OnboardingPageModel(
            imageRes = R.drawable.img1,
            titleRes = R.string.onboarding_title_1,
            descriptionRes = R.string.onboarding_description_1
        ),
        OnboardingPageModel(
            imageRes = R.drawable.img2,
            titleRes = R.string.onboarding_title_2,
            descriptionRes = R.string.onboarding_description_2
        ),
        OnboardingPageModel(
            imageRes = R.drawable.img3,
            titleRes = R.string.onboarding_title_3,
            descriptionRes = R.string.onboarding_description_3
        )
    )
}