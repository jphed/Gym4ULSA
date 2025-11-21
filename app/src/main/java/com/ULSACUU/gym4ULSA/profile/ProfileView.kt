package com.ULSACUU.gym4ULSA.profile

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ULSACUU.gym4ULSA.home.HomeViewModel
import com.ULSACUU.gym4ULSA.navigation.ScreenNavigation
import com.ULSACUU.gym4ULSA.utils.CredentialsStore
import com.ULSACUU.gym4ULSA.utils.DataStoreManager
import kotlinx.coroutines.launch
import com.ULSACUU.gym4ULSA.R

@Composable
fun ProfileView(navController: NavController) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val ds = remember { DataStoreManager(context) }
    val creds = remember { CredentialsStore(context) }

    val name = ds.userNameFlow.collectAsState(initial = "").value
    val email = ds.userEmailFlow.collectAsState(initial = "").value
    val createdAt = ds.accountCreatedAtFlow.collectAsState(initial = "").value
    val photoUri = ds.userPhotoUriFlow.collectAsState(initial = "").value

    val homeVm: HomeViewModel = viewModel()
    val routineType = homeVm.selectedGistRoutine?.musculo ?: ""

    // Selector de imagen con persistencia de permisos
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: Exception) { }
            scope.launch {
                ds.setUserPhotoUri(uri.toString())
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(12.dp))

        // 🧩 Avatar seguro
        val bmp = remember(photoUri) {
            runCatching {
                if (photoUri.isNotBlank()) {
                    val uri = Uri.parse(photoUri)
                    context.contentResolver.openInputStream(uri)?.use { ins ->
                        BitmapFactory.decodeStream(ins)
                    }
                } else null
            }.getOrElse { null }
        }

        Image(
            bitmap = bmp?.asImageBitmap()
                ?: BitmapFactory.decodeResource(
                    navController.context.resources,
                    R.drawable.ic_default_profile
                ).asImageBitmap(),
            contentDescription = stringResource(id = R.string.profile_photo_desc),
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .clickable { pickImageLauncher.launch("image/*") }
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = if (name.isNotBlank()) name else stringResource(id = R.string.profile_name_placeholder),
            fontSize = 22.sp,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        if (email.isNotBlank()) {
            Text(
                text = email,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.height(16.dp))

        if (routineType.isNotBlank()) {
            Text(
                text = stringResource(id = R.string.profile_routine_type),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Text(
                text = routineType,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp)
            )
        }

        if (createdAt.isNotBlank()) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = stringResource(id = R.string.profile_account_created),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Text(
                text = createdAt,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

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
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(text = stringResource(id = R.string.settings_logout), color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}
