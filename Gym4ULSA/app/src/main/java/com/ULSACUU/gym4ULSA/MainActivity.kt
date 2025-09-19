package com.ULSACUU.gym4ULSA

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ULSACUU.gym4ULSA.onboarding.viewmodel.OnboardingViewModel
import com.ULSACUU.gym4ULSA.routine.onboarding.views.OnboardingView
import com.ULSACUU.gym4ULSA.navigation.TabBarNavigationView
import com.ULSACUU.gym4ULSA.ui.theme.AndroidClassMP1Theme
import com.ULSACUU.gym4ULSA.utils.DataStoreManager
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val ds = DataStoreManager(this)

        setContent {
            AndroidClassMP1Theme {
                val scope = rememberCoroutineScope()
                val vm: OnboardingViewModel = viewModel()

                val onboardingDone: Boolean? by ds.onboardingDoneFlow.collectAsState(initial = null)

                when (onboardingDone) {
                    null -> SplashLoader()
                    false -> OnboardingView(
                        viewModel = vm,
                        onFinish = {
                            scope.launch { ds.setOnboardingDone(true) }
                        }
                    )
                    true -> TabBarNavigationView() // Aquí LoginView ya se maneja dentro del NavHost
                }
            }
        }
    }
}

@Composable
private fun SplashLoader() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

