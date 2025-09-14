package com.jorgeromo.androidClassMp1.firstpartial.onboarding.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * Este será el modelo de datos de nuestro onboarding
 * Imagen que viene desde el local de la computadora
 * Titulo de la pagina del onboarding
 * Descripción de la funcionaldiad de  la pagina
 */
data class OnboardingPageModel(
    @DrawableRes val imageRes: Int,
    val videoRes: Int? = null,
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int
)