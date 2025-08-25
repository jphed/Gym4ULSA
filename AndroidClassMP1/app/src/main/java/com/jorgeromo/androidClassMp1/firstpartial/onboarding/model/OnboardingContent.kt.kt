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
            title = "Descubre tu vocación Lasallista",
            description = "Explora una educación universitario con valores cristianos e integrales, donde formar profesionales con vocación al servicio y responsabilidad social es el compromiso que nos distingue."
        ),
        OnboardingPageModel(
            imageRes = R.drawable.onb_2,
            title = "Innovación educativa y colaboración",
            description = "Accede a modernas aulas, tecnología de punta y espacios de innovación, pensados para fomentar el trabajo colaborativo y el desarrollo profesional desde hoy."
        ),
        OnboardingPageModel(
            imageRes = R.drawable.onb_3,
            title = "Transforma tu futuro con excelencia",
            description = "Forma parte de una comunidad educativa que impulsa tu crecimiento académico, humanista e investigativo, en un entorno universitario de calidad y prestigio regional."
        )
    )
}