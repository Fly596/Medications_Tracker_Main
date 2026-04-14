package com.galeria.medtracker2.feature.auth.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medtracker2.R
import com.galeria.medtracker2.core.ui.components.FlyButton
import com.galeria.medtracker2.core.ui.components.FlyTextButton
import com.galeria.medtracker2.core.ui.components.FlyTonalButton
import com.galeria.medtracker2.core.ui.components.MySwitch
import com.galeria.medtracker2.core.ui.components.MyTextField
import com.galeria.medtracker2.core.ui.theme.MedTrackerTheme

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginSuccessNavigation: () -> Unit = {},
    onRegistration: (String) -> Unit = {},
    onResetPassword: (String) -> Unit = {},
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Top,
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Sign In",
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
        ) {
            MyTextField(
                value = state.value.email,
                onValueChange = { viewModel.updateEmail(it) },
                isPrimaryColor = true,
                isError = state.value.emailError?.isNotEmpty() ?: false,
                errorMessage = state.value.emailError,
                label = "Email",
                placeholder = "",
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            )
            
            MyTextField(
                value = state.value.password,
                onValueChange = { viewModel.updatePassword(it) },
                isPrimaryColor = true,
                isError = state.value.passwordError?.isNotEmpty() ?: false,
                errorMessage = state.value.passwordError,
                label = "Password",
                placeholder = "6 or more characters",
                // supportingText = "6 or more characters",
                modifier = Modifier.fillMaxWidth(),
                visualTransformation =
                    if (state.value.showPassword) VisualTransformation.None
                    else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            )
        }
        // Show password switch.
        RememberMeSwitch(
            checked = state.value.showPassword,
            onCheckedChange = { viewModel.isShowPasswordChecked(state.value.showPassword) },
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            FlyButton(
                onClick = { viewModel.onSignInClick() },
                enabled = true,
            ) {
                Text("Sign In")
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            FlyTonalButton(
                onClick = { onRegistration(state.value.email) },
                enabled = true
            ) {
                Text("Create account")
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        FlyTextButton(
            onClick = { onResetPassword(state.value.email) },
            enabled = true
        ) {
            Text("Forgot password")
        }
    }
}

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