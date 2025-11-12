package com.ULSACUU.gym4ULSA

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ULSACUU.gym4ULSA.navigation.TabBarNavigationView
import com.ULSACUU.gym4ULSA.onboarding.viewmodel.OnboardingViewModel
import com.ULSACUU.gym4ULSA.chat.onboarding.views.OnboardingView
import com.ULSACUU.gym4ULSA.settings.viewmodel.SettingsViewModel
import com.ULSACUU.gym4ULSA.settings.viewmodel.SettingsViewModelFactory
import com.ULSACUU.gym4ULSA.ui.theme.AndroidClassMP1Theme
import com.ULSACUU.gym4ULSA.utils.DataStoreManager
import com.ULSACUU.gym4ULSA.utils.UserPreferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Locale

class MainActivity : ComponentActivity() {

    // configura el idioma ANTES de crear la UI.
    // Leemos el valor directamente de DataStore.
    override fun attachBaseContext(newBase: Context) {
        val userPreferences = UserPreferencesDataStore(newBase)
        // Usamos runBlocking para leer el valor de forma síncrona.
        val lang = runBlocking { userPreferences.language.first() }
        val context = LocaleHelper.setLocale(newBase, lang)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val dsManager = DataStoreManager(this)

        setContent {
            // Creamos el ViewModel y sus dependencias aquí.
            val userPreferences = remember { UserPreferencesDataStore(this) }
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModelFactory(userPreferences)
            )

            val isDarkTheme by settingsViewModel.isDarkTheme.collectAsStateWithLifecycle()
            val currentLanguage by settingsViewModel.language.collectAsStateWithLifecycle()
            
            // Detecta el cambio y recrea la Activity para aplicar los nuevos recursos de idioma.
            LaunchedEffect(currentLanguage) {
                val contextLanguage = this@MainActivity.resources.configuration.locales[0].language
                if (currentLanguage != contextLanguage) {
                    recreate()
                }
            }

            AndroidClassMP1Theme(darkTheme = isDarkTheme) {
                val scope = rememberCoroutineScope()
                val onboardingDone: Boolean? by dsManager.onboardingDoneFlow.collectAsState(initial = null)

                when (onboardingDone) {
                    null -> SplashLoader() // Muestra un loader mientras se lee el estado de onboarding
                    false -> {
                        val vm: OnboardingViewModel = viewModel()
                        OnboardingView(
                            viewModel = vm,
                            onFinish = {
                                scope.launch { dsManager.setOnboardingDone(true) }
                            }
                        )
                    }
                    true -> TabBarNavigationView(settingsViewModel = settingsViewModel)
                }
            }
        }
    }
}

// Objeto Helper para cambiar el idioma de la app
object LocaleHelper {
    fun setLocale(context: Context, languageCode: String): ContextWrapper {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        val newContext = context.createConfigurationContext(config)
        return ContextWrapper(newContext)
    }
}

@Composable
private fun SplashLoader() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}