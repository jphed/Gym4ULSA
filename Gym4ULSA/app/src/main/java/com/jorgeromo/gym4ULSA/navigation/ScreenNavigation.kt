package com.jorgeromo.gym4ULSA.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class ScreenNavigation(val route: String, val label: String, val icon: ImageVector) {
    object Home : ScreenNavigation("HomeRoute", "Home", Icons.Default.Home)
    object Rutina : ScreenNavigation("RutinaRoute", "Routine", Icons.Default.FitnessCenter)
    object Perfil : ScreenNavigation("PerfilRoute", "Perfil", Icons.Default.Person)
    object Ajustes : ScreenNavigation("AjustesRoute", "Settings", Icons.Default.Settings)
    object Login : ScreenNavigation("LoginRoute", "Login", Icons.Default.Event)
    object Onboarding : ScreenNavigation("OnboardingRoute", "Onboarding", Icons.Default.Info)
    object Nutrition : ScreenNavigation("NutritionRoute", "Nutrition", Icons.Default.Restaurant)
}