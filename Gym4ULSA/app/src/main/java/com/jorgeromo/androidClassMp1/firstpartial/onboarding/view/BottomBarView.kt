package com.jorgeromo.androidClassMp1.firstpartial.onboarding.views

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars

@Composable
fun BottomBarView(
    isLastPage: Boolean,
    page: Int,
    total: Int,
    onPrev: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                WindowInsets.navigationBars.asPaddingValues() // 🔽 deja espacio de la barra del sistema
            )
            .padding(horizontal = 16.dp, vertical = 8.dp) // tu padding normal
    ) {
        TextButton(
            enabled = page > 0,
            onClick = onPrev
        ) { Text("Anterior") }

        Spacer(Modifier.weight(1f))

        Button(onClick = onNext) {
            Text(if (isLastPage) "Empezar" else "Siguiente")
        }
    }
}
