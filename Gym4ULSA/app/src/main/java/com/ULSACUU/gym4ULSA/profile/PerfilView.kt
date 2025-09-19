package com.ULSACUU.gym4ULSA.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ULSACUU.gym4ULSA.navigation.ScreenNavigation
import com.ULSACUU.gym4ULSA.utils.CredentialsStore
import com.ULSACUU.gym4ULSA.utils.DataStoreManager
import kotlinx.coroutines.launch

@Composable
fun PerfilView(navController: NavController) {
    val scope = rememberCoroutineScope()
    val ds = remember { DataStoreManager(navController.context) }
    val creds = remember { CredentialsStore(navController.context) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "PERFIL",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                scope.launch {
                    // Clear remembered flags and credentials
                    ds.setRememberCredentials(false)
                    val email = creds.getSavedEmail() ?: ""
                    if (email.isNotBlank()) {
                        ds.setSavedEmail("")
                        creds.clearCredentials(email)
                    }
                }
                // Navigate back to Login and clear back stack
                navController.navigate(ScreenNavigation.Login.route) {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    launchSingleTop = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            colors = ButtonDefaults.buttonColors()
        ) {
            Text(text = "Log out")
        }
    }
}