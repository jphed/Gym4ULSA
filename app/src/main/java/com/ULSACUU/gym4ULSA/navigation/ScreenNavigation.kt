package com.ULSACUU.gym4ULSA.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class ScreenNavigation(val route: String, val label: String, val icon: ImageVector) {
    object Home : ScreenNavigation("HomeRoute", "Home", Icons.Default.Home)
    object Routine : ScreenNavigation("RoutineRoute", "Routine", Icons.Default.Chat)
    object Profile : ScreenNavigation("ProfileRoute", "Profile", Icons.Default.Person)
    object Settings : ScreenNavigation("SettingsRoute", "Settings", Icons.Default.Settings)
    object Login : ScreenNavigation("LoginRoute", "Login", Icons.Default.Event)
    object Onboarding : ScreenNavigation("OnboardingRoute", "Onboarding", Icons.Default.Info)
    object Nutrition : ScreenNavigation("NutritionRoute", "Nutrition", Icons.Default.Restaurant)
    object QrScanner : ScreenNavigation("QrScannerRoute", "QR Scanner", Icons.Default.QrCodeScanner)
    object HomeDetails : ScreenNavigation("HomeDetailsRoute/{exerciseId}","Exercise Detail", Icons.Default.FitnessCenter)
    object TermsAndConditions : ScreenNavigation("TermsAndConditionsRoute", "Terms & Conditions", Icons.Default.Info)
    object SignUp : ScreenNavigation("SignUpRoute", "Registro", Icons.Default.PersonAdd)

}
