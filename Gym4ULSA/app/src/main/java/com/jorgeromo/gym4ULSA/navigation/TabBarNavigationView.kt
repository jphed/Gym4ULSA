package com.jorgeromo.gym4ULSA.navigation

import SecondPartialView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import com.jorgeromo.gym4ULSA.firstpartial.FirstPartialView
import com.jorgeromo.gym4ULSA.ids.imc.views.IMCView
import com.jorgeromo.gym4ULSA.ids.IdsView
import com.jorgeromo.gym4ULSA.ids.location.views.LocationListScreen
import com.jorgeromo.gym4ULSA.ids.student.views.StudentView
import com.jorgeromo.gym4ULSA.ids.sum.views.SumView
import com.jorgeromo.gym4ULSA.ids.temperature.views.TempView
import com.jorgeromo.gym4ULSA.thirdpartial.ThirdPartialView
import androidx.compose.ui.graphics.Color
import com.jorgeromo.gym4ULSA.firstpartial.login.views.LoginView
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabBarNavigationView(navController: NavHostController = rememberNavController()) {
    val items = listOf(
        ScreenNavigation.Login,
        ScreenNavigation.FirstPartial,
        ScreenNavigation.SecondPartial,
        ScreenNavigation.ThirdPartial
    )

    // Mapa de títulos por ruta (incluye tabs y pantallas internas)
    val routeTitles = remember {
        mapOf(
            ScreenNavigation.Login.route to ScreenNavigation.Login.label,
            ScreenNavigation.FirstPartial.route to ScreenNavigation.FirstPartial.label,
            ScreenNavigation.SecondPartial.route to ScreenNavigation.SecondPartial.label,
            ScreenNavigation.ThirdPartial.route to ScreenNavigation.ThirdPartial.label,

            // Rutas internas (ajusta a tus strings preferidos)
            ScreenNavigation.IMC.route to "IMC",
            ScreenNavigation.Login.route to "Login",
            ScreenNavigation.Sum.route to "Suma",
            ScreenNavigation.Temperature.route to "Temperatura",
            ScreenNavigation.StudentList.route to "Estudiantes",
            ScreenNavigation.Locations.route to "Ubicaciones"
        )
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    // Si usas nested graphs, puedes leer la jerarquía; aquí basta con la route actual:
    val currentRoute = navBackStackEntry?.destination?.route
    val currentTitle = routeTitles[currentRoute] ?: ""

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Gym4ULSA") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: assign action, e.g., navigate to add/new */ },
                shape = RoundedCornerShape(24.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .size(84.dp)
                    .offset(y = 68.dp) // lowered further; stronger overlap
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,

        bottomBar = {
            NavigationBar {
                val leftItems = items.take(2)
                val rightItems = items.takeLast(2)

                leftItems.forEach { screen ->
                    val selected = currentRoute == screen.route
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = selected,
                        onClick = {
                            if (!selected) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }

                // Center spacer to create symmetry around the FAB
                NavigationBarItem(
                    icon = { Box(modifier = Modifier.size(48.dp)) {} },
                    label = { },
                    selected = false,
                    enabled = false,
                    onClick = { }
                )

                rightItems.forEach { screen ->
                    val selected = currentRoute == screen.route
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = selected,
                        onClick = {
                            if (!selected) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = ScreenNavigation.Login.route,
                modifier = Modifier
            ) {
                composable(ScreenNavigation.Ids.route) { IdsView(navController) }
                composable(ScreenNavigation.FirstPartial.route) { FirstPartialView() }
                composable(ScreenNavigation.SecondPartial.route) { SecondPartialView() }
                composable(ScreenNavigation.ThirdPartial.route) { ThirdPartialView(navController) }

                // Rutas internas
                composable(ScreenNavigation.IMC.route) { IMCView() }
                composable(ScreenNavigation.Login.route) { LoginView(navController) }
                composable(ScreenNavigation.Sum.route) { SumView() }
                composable(ScreenNavigation.Temperature.route) { TempView() }
                composable(ScreenNavigation.StudentList.route) { StudentView() }
                composable(ScreenNavigation.Locations.route) { LocationListScreen() }
            }
        }
    }
}
