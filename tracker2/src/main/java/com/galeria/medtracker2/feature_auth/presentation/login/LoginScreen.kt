package com.galeria.medtracker2.feature_auth.presentation.login

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.galeria.medtracker2.R
import com.galeria.medtracker2.shared.components.MySwitch
import com.galeria.medtracker2.ui.theme.MedTrackerTheme

@Composable fun LoginScreen(viewModel: LoginViewModel = hiltViewModel()) {}

@Composable
fun RememberMeSwitch(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            stringResource(R.string.show_password),
            style = MedTrackerTheme.typography.bodyMedium
        )
        
        MySwitch(checked = checked, onCheckedChange = onCheckedChange)
    }
}