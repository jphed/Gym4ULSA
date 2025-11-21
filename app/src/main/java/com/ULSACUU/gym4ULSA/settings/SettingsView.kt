package com.ULSACUU.gym4ULSA.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ULSACUU.gym4ULSA.R
import com.ULSACUU.gym4ULSA.settings.viewmodel.SettingsViewModel
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.ULSACUU.gym4ULSA.navigation.ScreenNavigation
import com.ULSACUU.gym4ULSA.utils.CredentialsStore
import com.ULSACUU.gym4ULSA.utils.DataStoreManager
import kotlinx.coroutines.launch


@Composable
fun SettingsView(
    navController: NavHostController,
    viewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    val isDarkTheme by viewModel.isDarkTheme.collectAsState()
    val language by viewModel.language.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val ds = remember { DataStoreManager(context) }
    val creds = remember { CredentialsStore(context) }

    // Hacemos la columna "scrollable"
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()) // <-- Para scrolling
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.settings_title),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(32.dp))

        //Tarjeta de Apariencia ---
        Text(
            text = stringResource(R.string.settings_section_appearance),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            // Tema oscuro
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.settings_dark_theme),
                    style = MaterialTheme.typography.bodyLarge
                )
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = { viewModel.toggleTheme() }
                )
            }

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            // Idioma
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.settings_language),
                    style = MaterialTheme.typography.bodyLarge
                )
                LanguageSelector(
                    currentLanguage = language,
                    onLanguageSelected = { viewModel.changeLanguage(it) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ---  Tarjeta de Cuenta ---
        Text(
            text = stringResource(R.string.settings_section_account),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            // Botón Editar Perfil
            SettingButtonRow(
                text = stringResource(R.string.settings_edit_profile),
                icon = Icons.Default.Person,
                onClick = { /* TODO: Navegar a pantalla de perfil */ }
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            // Botón Notificaciones
            SettingSwitchRow(
                text = stringResource(R.string.settings_notifications),
                icon = Icons.Default.Notifications,
                checked = true, // Valor de ejemplo
                onCheckedChange = { /* TODO: Guardar preferencia */ }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ---  Tarjeta de Acerca de ---
        Text(
            text = stringResource(R.string.settings_section_about),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            // Botón Política de Privacidad
            SettingButtonRow(
                text = stringResource(R.string.settings_privacy_policy),
                icon = Icons.Default.Shield,
                onClick = { /* TODO: Abrir enlace web */ }
            )
        }

        // ---  Botones de Sesión (al fondo) ---
        Spacer(modifier = Modifier.weight(1f).heightIn(min = 32.dp))

        // Botón Cerrar Sesión
        Button(
            onClick = {
                scope.launch {
                    ds.setRememberCredentials(false)
                    val savedEmail = creds.getSavedEmail() ?: ""
                    if (savedEmail.isNotBlank()) {
                        ds.setSavedEmail("")
                        creds.clearCredentials(savedEmail)
                    }
                }
                navController.navigate(ScreenNavigation.Login.route) {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    launchSingleTop = true
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
            Text(stringResource(R.string.settings_logout))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón Eliminar Cuenta (Destructivo)
        Button(
            onClick = { /* TODO: Mostrar diálogo de PELIGRO */ },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.DeleteForever, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
            Text(stringResource(R.string.settings_delete_account))
        }
    }
}

// --- COMPONENTES REUTILIZABLES ---

@Composable
private fun SettingButtonRow(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.padding(end = 16.dp))
        Text(text, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        Icon(Icons.Default.ChevronRight, contentDescription = null)
    }
}

@Composable
private fun SettingSwitchRow(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.padding(end = 16.dp))
        Text(text, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}


// --- Selector de Idioma ---
@Composable
fun LanguageSelector(currentLanguage: String, onLanguageSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val languages = listOf("es", "en")


    val languageMap = mapOf(
        "es" to R.string.language_es,
        "en" to R.string.language_en
    )

    Box {
        Button(onClick = { expanded = true }) {
            Text(
                text = stringResource(
                    languageMap[currentLanguage] ?: R.string.language_es
                )
            )
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            languages.forEach { langCode ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(languageMap[langCode] ?: R.string.language_es)
                        )
                    },
                    onClick = {
                        onLanguageSelected(langCode)
                        expanded = false
                    }
                )
            }
        }
    }
}