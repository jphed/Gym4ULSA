package com.ULSACUU.gym4ULSA

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import com.ULSACUU.gym4ULSA.routine.onboarding.views.OnboardingView
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

    // 1. Instanciamos UserPreferencesDataStore para pasárselo al ViewModel
    private val userPreferences by lazy { UserPreferencesDataStore(this) }

    // 2. Creamos el ViewModel como una propiedad de la Activity para poder usarlo en `attachBaseContext`
    private val settingsViewModel: SettingsViewModel by viewModels {
        SettingsViewModelFactory(userPreferences)
    }

    // 👈 AÑADIDO: Lógica para aplicar el idioma ANTES de que la UI se cree
    override fun attachBaseContext(newBase: Context) {
        // Leemos el idioma guardado y lo aplicamos
        val lang = runBlocking { settingsViewModel.language.first() }
        val context = LocaleHelper.setLocale(newBase, lang)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Mantenemos tu instancia de DataStoreManager para el onboarding
        val dsManager = DataStoreManager(this)

        setContent {
            // Recolectamos los estados del ViewModel (esta parte ya la tenías bien)
            val isDarkTheme by settingsViewModel.isDarkTheme.collectAsStateWithLifecycle()

            // 👈 AÑADIDO: Recolectamos el estado del idioma
            val currentLanguage by settingsViewModel.language.collectAsStateWithLifecycle()

            // 👈 AÑADIDO: Efecto que recrea la Activity cuando el idioma cambia
            LaunchedEffect(currentLanguage) {
                val contextLanguage = this@MainActivity.resources.configuration.locales[0].language
                if (currentLanguage != contextLanguage) {
                    recreate() // Esto fuerza la recarga de los strings.xml
                }
            }

            // Tu lógica del tema (¡perfecta!)
            AndroidClassMP1Theme(darkTheme = isDarkTheme) {
                val scope = rememberCoroutineScope()

                // Tu lógica de onboarding (sin cambios)
                val onboardingDone: Boolean? by dsManager.onboardingDoneFlow.collectAsState(initial = null)

                when (onboardingDone) {
                    null -> SplashLoader()
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

// 헬 Helper para el idioma (puedes ponerlo en un archivo /util/LocaleHelper.kt)
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