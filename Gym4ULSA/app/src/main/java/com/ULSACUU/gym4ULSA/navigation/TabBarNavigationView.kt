package com.ULSACUU.gym4ULSA.navigation


import com.ULSACUU.gym4ULSA.profile.PerfilView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.graphics.Color
import com.ULSACUU.gym4ULSA.login.views.LoginView
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ULSACUU.gym4ULSA.routine.onboarding.views.OnboardingView
import com.ULSACUU.gym4ULSA.onboarding.viewmodel.OnboardingViewModel
import androidx.compose.ui.res.stringResource
import com.ULSACUU.gym4ULSA.R
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.filled.AccountCircle
import com.ULSACUU.gym4ULSA.routine.RutinaView
import com.ULSACUU.gym4ULSA.home.HomeView
import com.ULSACUU.gym4ULSA.settings.AjustesView
import com.ULSACUU.gym4ULSA.nutrition.view.NutritionView
import com.ULSACUU.gym4ULSA.settings.viewmodel.SettingsViewModel
import com.ULSACUU.gym4ULSA.settings.viewmodel.SettingsViewModelFactory
import com.ULSACUU.gym4ULSA.utils.UserPreferencesDataStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabBarNavigationView(
    navController: NavHostController = rememberNavController(),
    settingsViewModel: SettingsViewModel
) {
    val items = listOf(
        ScreenNavigation.Home,           // Home
        ScreenNavigation.Rutina,  // Routine
        ScreenNavigation.Nutrition,     // Nutrition
        ScreenNavigation.Ajustes   // Settings
    )

    // Mapa de títulos por ruta (incluye tabs y pantallas internas)
    val routeTitles = remember {
        mapOf(
            ScreenNavigation.Login.route to ScreenNavigation.Login.label,
            ScreenNavigation.Onboarding.route to ScreenNavigation.Onboarding.label,
            ScreenNavigation.Home.route to ScreenNavigation.Home.label,
            ScreenNavigation.Rutina.route to ScreenNavigation.Rutina.label,
            ScreenNavigation.Nutrition.route to ScreenNavigation.Nutrition.label,
            ScreenNavigation.Ajustes.route to ScreenNavigation.Ajustes.label
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
                            // Navegar a la pantalla Perfil
                            navController.navigate(ScreenNavigation.Perfil.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Perfil"
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
                                    ScreenNavigation.Home -> R.string.bottom_home
                                    ScreenNavigation.Rutina -> R.string.bottom_routine
                                    ScreenNavigation.Nutrition -> R.string.bottom_nutrition
                                    ScreenNavigation.Ajustes -> R.string.bottom_settings
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
                                    ScreenNavigation.Home -> R.string.bottom_home
                                    ScreenNavigation.Rutina -> R.string.bottom_routine
                                    ScreenNavigation.Nutrition -> R.string.bottom_nutrition
                                    ScreenNavigation.Ajustes -> R.string.bottom_settings
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
                composable(ScreenNavigation.Home.route) { HomeView(navController) }
                composable(ScreenNavigation.Rutina.route) { RutinaView() }
                composable(ScreenNavigation.Perfil.route) { PerfilView(navController) }
                composable(ScreenNavigation.Ajustes.route) {
                    AjustesView(navController, settingsViewModel)
                }



                // Rutas internas
                composable(ScreenNavigation.Login.route) { LoginView(navController) }
                composable(ScreenNavigation.Nutrition.route) {
                    NutritionView(navController)
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
