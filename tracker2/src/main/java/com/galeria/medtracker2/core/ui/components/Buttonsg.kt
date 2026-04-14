package com.galeria.medtracker2.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.galeria.medtracker2.core.ui.theme.MedTrackerTheme
import com.galeria.medtracker2.core.ui.theme.MedTrackerTheme.colors

@Composable
fun Buttong(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled && !isLoading,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = colors.primary400,
                contentColor = colors.sysWhite,
                disabledContainerColor = colors.tertiaryFill.copy(alpha = 0.12f),
                disabledContentColor = colors.tertiaryLabel,
            ),
        shape = RoundedCornerShape(12.dp),
        elevation =
            ButtonDefaults.buttonElevation(
                defaultElevation = 8.dp, pressedElevation = 4.dp, disabledElevation = 0.dp)) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            }
        }
}

@Preview(showBackground = true)
@Composable
fun ButtongPreview() {
    MedTrackerTheme {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Buttong(modifier = Modifier.fillMaxWidth(), text = "Primary Button", onClick = {})
                Buttong(text = "Disabled Button", onClick = {}, enabled = false)
                Buttong(text = "Loading Button", onClick = {}, isLoading = true)
            }
    }
}
