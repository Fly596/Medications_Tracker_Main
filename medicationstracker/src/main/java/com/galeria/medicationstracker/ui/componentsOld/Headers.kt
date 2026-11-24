package com.galeria.medicationstracker.ui.componentsOld

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.galeria.medicationstracker.core.ui.theme.MedTrackerTheme

@Composable
fun HeaderWithIconButtonAndTitle(
    onBackClick: () -> Unit,
    title: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onBackClick.invoke() }
        ) {
            Icon(
                imageVector = Icons.Rounded.ArrowBackIosNew,
                contentDescription = null,
                tint = MedTrackerTheme.colors.sysBlack,
            )
        }
        // Spacer(modifier = Modifier.weight(1f))

        Text(
            title,
            style = MedTrackerTheme.typography.display3Emphasized
        )
    }
}