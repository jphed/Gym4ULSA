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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jorgeromo.gym4ULSA.firstpartial.onboarding.views.OnboardingView
import com.jorgeromo.gym4ULSA.firstpartial.onboarding.viewmodel.OnboardingViewModel
import androidx.compose.ui.res.stringResource
import com.jorgeromo.gym4ULSA.R
import androidx.compose.foundation.layout.WindowInsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabBarNavigationView(navController: NavHostController = rememberNavController()) {
    val items = listOf(
        ScreenNavigation.Ids,           // Home
        ScreenNavigation.FirstPartial,  // Routine
        ScreenNavigation.Nutrition,     // Nutrition
        ScreenNavigation.ThirdPartial   // Settings
    )

    // Mapa de títulos por ruta (incluye tabs y pantallas internas)
    val routeTitles = remember {
        mapOf(
            ScreenNavigation.Login.route to ScreenNavigation.Login.label,
            ScreenNavigation.Onboarding.route to ScreenNavigation.Onboarding.label,
            ScreenNavigation.Ids.route to ScreenNavigation.Ids.label,
            ScreenNavigation.FirstPartial.route to ScreenNavigation.FirstPartial.label,
            ScreenNavigation.Nutrition.route to ScreenNavigation.Nutrition.label,
            ScreenNavigation.ThirdPartial.route to ScreenNavigation.ThirdPartial.label,

            // Rutas internas (ajusta a tus strings preferidos)
            ScreenNavigation.IMC.route to "IMC",
            ScreenNavigation.Sum.route to "Sum",
            ScreenNavigation.Temperature.route to "Temperature",
            ScreenNavigation.StudentList.route to "Students",
            ScreenNavigation.Locations.route to "Locations"
        )
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    // Si usas nested graphs, puedes leer la jerarquía; aquí basta con la route actual:
    val currentRoute = navBackStackEntry?.destination?.route
    val currentTitle = routeTitles[currentRoute] ?: ""

    Scaffold(
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            if (currentRoute != ScreenNavigation.Login.route && currentRoute != ScreenNavigation.Onboarding.route) {
                CenterAlignedTopAppBar(
                    title = { Text(text = "Gym4ULSA") },
                    navigationIcon = {
                        IconButton(onClick = {
                            if (currentRoute != ScreenNavigation.Login.route) {
                                navController.navigate(ScreenNavigation.Login.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back to Login"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Black,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White,
                        actionIconContentColor = Color.White
                    )
                )
            }
        },

        floatingActionButton = {
            if (currentRoute != ScreenNavigation.Login.route && currentRoute != ScreenNavigation.Onboarding.route) {
                FloatingActionButton(
                    onClick = { /* TODO: assign action, e.g., navigate to add/new */ },
                    shape = RoundedCornerShape(24.dp),
                    containerColor = Color.Black,
                    contentColor = Color.White,
                    modifier = Modifier
                        .size(84.dp)
                        .offset(y = 68.dp) // lowered further; stronger overlap
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add"
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,

        bottomBar = {
            if (currentRoute != ScreenNavigation.Login.route && currentRoute != ScreenNavigation.Onboarding.route) {
                NavigationBar {
                    val leftItems = items.take(2)
                    val rightItems = items.takeLast(2)

                    leftItems.forEach { screen ->
                        val selected = currentRoute == screen.route
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.label) },
                            label = {
                                val labelId = when (screen) {
                                    ScreenNavigation.Ids -> R.string.bottom_home
                                    ScreenNavigation.FirstPartial -> R.string.bottom_routine
                                    ScreenNavigation.Nutrition -> R.string.bottom_nutrition
                                    ScreenNavigation.ThirdPartial -> R.string.bottom_settings
                                    else -> R.string.bottom_home
                                }
                                Text(stringResource(labelId))
                            },
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
                            label = {
                                val labelId = when (screen) {
                                    ScreenNavigation.Ids -> R.string.bottom_home
                                    ScreenNavigation.FirstPartial -> R.string.bottom_routine
                                    ScreenNavigation.Nutrition -> R.string.bottom_nutrition
                                    ScreenNavigation.ThirdPartial -> R.string.bottom_settings
                                    else -> R.string.bottom_home
                                }
                                Text(stringResource(labelId))
                            },
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
                composable(ScreenNavigation.Nutrition.route) {
                    // Placeholder nutrition screen
                    Box(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Nutrition (coming soon)")
                    }
                }
                composable(ScreenNavigation.Onboarding.route) {
                    val vm: OnboardingViewModel = viewModel()
                    OnboardingView(
                        viewModel = vm,
                        onFinish = {
                            // When finished onboarding inside the app, return to Login
                            navController.navigate(ScreenNavigation.Login.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    }
}
