package com.ULSACUU.gym4ULSA.chat

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import android.content.ActivityNotFoundException
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// --- FUNCIÓN DE AYUDA PARA WHATSAPP ---
fun launchWhatsApp(context: Context, phoneNumber: String) {
    val message = "Hola, soy usuario de Gym4ULSA y necesito asesoría."
    val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=${Uri.encode(message)}"
    val intent = Intent(Intent.ACTION_VIEW).apply {
        Log.d("La url es ", url)
        data = Uri.parse(url)
    }
    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "No se pudo abrir WhatsApp.", Toast.LENGTH_SHORT).show()
    }
}

// --- LA PANTALLA VISUAL ---
@Composable
fun ChatView() {
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Text(
            text = "Elige a tu Coach",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn {
            items(coachList) { coach ->
                CoachItemCard(coach = coach) {
                    launchWhatsApp(context, coach.phoneNumber)
                }
            }
        }
    }
}

// --- COMPONENTE PARA CADA FILA (TARJETA) ---
@Composable
fun CoachItemCard(coach: Coach, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = coach.name, fontSize = 18.sp)
                Text(text = coach.specialty, fontSize = 14.sp, color = MaterialTheme.colorScheme.secondary)
            }
        }
    }
}