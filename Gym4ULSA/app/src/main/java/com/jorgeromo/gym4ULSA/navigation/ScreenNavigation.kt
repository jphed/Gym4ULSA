package com.jorgeromo.gym4ULSA.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class ScreenNavigation(val route: String, val label: String, val icon: ImageVector) {
    object Ids : ScreenNavigation("IdsRoute", "Inicio", Icons.Default.Home)
    object FirstPartial : ScreenNavigation("FirstPartialRoute", "Rutina", Icons.Default.FitnessCenter)
    object SecondPartial : ScreenNavigation("SecondPartialRoute", "Perfil", Icons.Default.Person)
    object ThirdPartial : ScreenNavigation("ThirdPartialRoute", "Ajustes", Icons.Default.Settings)
    object IMC : ScreenNavigation("IMCRoute", "IMC", Icons.Default.FitnessCenter)
    object Login : ScreenNavigation("LoginRoute", "Login", Icons.Default.Event)
    object Sum : ScreenNavigation("SumRoute", "Sum", Icons.Default.Event)
    object Temperature : ScreenNavigation("TemperatureRoute", "Temperature", Icons.Default.Event)
    object StudentList : ScreenNavigation("StudentListRoute", "Estudiantes", Icons.Default.People)
    object Locations : ScreenNavigation("LocationsListRoute", "Location", Icons.Default.People)

}