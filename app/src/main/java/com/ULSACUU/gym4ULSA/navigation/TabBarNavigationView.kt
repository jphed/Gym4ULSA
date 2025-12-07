package com.ULSACUU.gym4ULSA.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.ULSACUU.gym4ULSA.R
import com.ULSACUU.gym4ULSA.home.HomeView
import com.ULSACUU.gym4ULSA.home.HomeViewModel
import com.ULSACUU.gym4ULSA.home.HomeDetailsView
import com.ULSACUU.gym4ULSA.chat.ChatView
import com.ULSACUU.gym4ULSA.chat.onboarding.views.OnboardingView
import com.ULSACUU.gym4ULSA.profile.ProfileView
import com.ULSACUU.gym4ULSA.settings.SettingsView
import com.ULSACUU.gym4ULSA.login.views.LoginView
import com.ULSACUU.gym4ULSA.create_routine.view.CreateRoutineScreen
import com.ULSACUU.gym4ULSA.nutrition.view.NutritionView
import com.ULSACUU.gym4ULSA.onboarding.viewmodel.OnboardingViewModel
import com.ULSACUU.gym4ULSA.qr.view.QrScannerView
import com.ULSACUU.gym4ULSA.settings.TermsAndConditionsView
import com.ULSACUU.gym4ULSA.settings.viewmodel.SettingsViewModel
import com.ULSACUU.gym4ULSA.sign_up.view.RegistroScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabBarNavigationView(
    navController: NavHostController = rememberNavController(),
    settingsViewModel: SettingsViewModel
) {
    val items = listOf(
        ScreenNavigation.Home,
        ScreenNavigation.Routine,
        ScreenNavigation.Nutrition,
        ScreenNavigation.Settings,
        ScreenNavigation.HomeDetails
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBars = currentRoute != ScreenNavigation.Login.route &&
            currentRoute != ScreenNavigation.Onboarding.route &&
            currentRoute != ScreenNavigation.SignUp.route

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            if (showBars) {
                CenterAlignedTopAppBar(
                    title = { Text(text = stringResource(id = R.string.app_name)) },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigate(ScreenNavigation.QrScanner.route) }) {
                            Icon(
                                imageVector = Icons.Outlined.QrCodeScanner,
                                contentDescription = stringResource(id = R.string.action_scan_qr)
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate(ScreenNavigation.Profile.route) }) {
                            Icon(
                                imageVector = Icons.Outlined.Person,
                                contentDescription = stringResource(id = R.string.action_profile)
                            )
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            if (showBars) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate("create_routine")
                    },
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .size(84.dp)
                        .offset(y = 68.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.action_add)
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            if (showBars) {
                AppBottomBar(
                    items = items,
                    currentRoute = currentRoute,
                    navController = navController
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = ScreenNavigation.Login.route,
                modifier = Modifier
            ) {

                composable("create_routine") {
                    CreateRoutineScreen(navController = navController)
                }
                composable(ScreenNavigation.Home.route) { HomeView(navController) }
                composable(ScreenNavigation.Routine.route) { ChatView() }
                composable(ScreenNavigation.Profile.route) { ProfileView(navController) }
                composable(ScreenNavigation.Settings.route) {
                    SettingsView(navController, settingsViewModel)
                }

                composable(ScreenNavigation.Login.route) {
                    LoginView(
                        navController = navController
                    )
                }

                composable(ScreenNavigation.SignUp.route) {
                    RegistroScreen(
                        onRegistroExitoso = {
                            navController.navigate(ScreenNavigation.Login.route) {
                                popUpTo(ScreenNavigation.Login.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }

                // Onboarding
                composable(ScreenNavigation.Onboarding.route) {
                    val vm: OnboardingViewModel = viewModel()
                    OnboardingView(
                        viewModel = vm,
                        onFinish = {
                            navController.navigate(ScreenNavigation.Login.route) {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            }
                        }
                    )
                }

                composable(ScreenNavigation.Nutrition.route) {
                    NutritionView(navController)
                }
                composable(ScreenNavigation.QrScanner.route) {
                    QrScannerView(navController)
                }
                composable(ScreenNavigation.TermsAndConditions.route) {
                    TermsAndConditionsView(navController)
                }

                composable(
                    route = "HomeDetailsRoute/{exerciseId}?",
                    arguments = listOf(
                        navArgument("exerciseId") {
                            type = NavType.StringType
                            nullable = true
                            defaultValue = "-1"
                        }
                    )
                ) { backStackEntry ->
                    val exerciseIdString = backStackEntry.arguments?.getString("exerciseId")
                    val exerciseId = exerciseIdString?.toIntOrNull() ?: -1

                    val homeViewModel: HomeViewModel = viewModel()
                    val exercise = homeViewModel.allExercises.find { it.id == exerciseId }

                    exercise?.let {
                        HomeDetailsView(exercise = it)
                    }
                }
            }
        }
    }
}

@Composable
private fun AppBottomBar(
    items: List<ScreenNavigation>,
    currentRoute: String?,
    navController: NavHostController
) {
    NavigationBar {
        val navItems = items.chunked(items.size / 2)

        // Renderiza los items de la izquierda
        navItems[0].forEach { screen ->
            AddItemToNavBar(screen, currentRoute, navController)
        }

        // Espaciador para el FAB
        NavigationBarItem(
            icon = { Box(modifier = Modifier.size(48.dp)) },
            label = {},
            selected = false,
            enabled = false,
            onClick = {}
        )

        // Renderiza los items de la derecha
        navItems[1].forEach { screen ->
            AddItemToNavBar(screen, currentRoute, navController)
        }
    }
}

@Composable
private fun RowScope.AddItemToNavBar(
    screen: ScreenNavigation,
    currentRoute: String?,
    navController: NavHostController
) {
    val selected = currentRoute == screen.route
    val labelId = when (screen) {
        ScreenNavigation.Home -> R.string.bottom_home
        ScreenNavigation.Routine -> R.string.bottom_routine
        ScreenNavigation.Nutrition -> R.string.bottom_nutrition
        ScreenNavigation.Settings -> R.string.bottom_settings
        else -> R.string.bottom_home // Default
    }

    NavigationBarItem(
        icon = { Icon(screen.icon, contentDescription = stringResource(labelId)) },
        label = { Text(stringResource(labelId)) },
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