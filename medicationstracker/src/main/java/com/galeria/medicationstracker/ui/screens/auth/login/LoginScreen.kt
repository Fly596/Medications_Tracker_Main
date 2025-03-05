package com.galeria.medicationstracker.ui.screens.auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.galeria.medicationstracker.R
import com.galeria.medicationstracker.data.UserType
import com.galeria.medicationstracker.ui.components.GOutlinedButton
import com.galeria.medicationstracker.ui.components.GPrimaryButton
import com.galeria.medicationstracker.ui.componentsOld.FlyTextButton
import com.galeria.medicationstracker.ui.componentsOld.MySwitch
import com.galeria.medicationstracker.ui.componentsOld.MyTextField
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLogin: () -> Unit = {},
    onRegistration: () -> Unit = {},
    onResetPassword: () -> Unit = {},
    onSignInSuccess: (userType: UserType) -> Unit = {},
    viewModel: LoginScreenViewModel = viewModel(),
) {
    val state = viewModel.loginScreenState.collectAsStateWithLifecycle()
    
    Scaffold(
        containerColor = MedTrackerTheme.colors.secondaryBackground,
        topBar = {
            Text(
                stringResource(R.string.sign_in_screen_title),
                style = MedTrackerTheme.typography.display2Emphasized,
                modifier = Modifier.padding(16.dp)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
              .fillMaxWidth()
              .padding(innerPadding)
              .padding(16.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                MyTextField(
                    value = state.value.email,
                    onValueChange = { viewModel.updateEmail(it) },
                    isPrimaryColor = true,
                    isError = state.value.emailError?.isNotEmpty() ?: false,
                    errorMessage = state.value.emailError,
                    label = stringResource(R.string.email_text_field_label),
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                )
                
                MyTextField(
                    value = state.value.password,
                    onValueChange = { viewModel.updatePassword(it) },
                    isPrimaryColor = true,
                    isError = state.value.passwordError?.isNotEmpty() ?: false,
                    errorMessage = state.value.passwordError,
                    label = stringResource(R.string.password_text_field_label),
                    placeholder = stringResource(R.string.password_text_field_reqs_placeholder),
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation =
                        if (state.value.showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                )
            }
            // Show password switch.
            RememberMeSwitch(
                checked = state.value.showPassword,
                onCheckedChange = { newValue ->
                    viewModel.updateShowPassword(newValue)
                },
            )
            
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                GPrimaryButton(
                    onClick = {
                        viewModel.onSignInClick(
                            state.value.email,
                            state.value.password
                        ) { userType ->
                            onSignInSuccess(userType)
                        }
                    },
                    enabled = true,
                ) {
                    Text(text = stringResource(R.string.sign_in_button_text))
                }
                
                GOutlinedButton(
                    onClick = {
                        onRegistration()
                    },
                    enabled = true
                ) {
                    Text(text = stringResource(R.string.create_account_button_text))
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            FlyTextButton(
                onClick = {
                    onResetPassword()
                },
                enabled = true
            ) {
                Text(text = stringResource(R.string.forgot_password_button_text))
            }
        }
    }
}

@Composable
fun RememberMeSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            stringResource(R.string.show_password_switch_text),
            style = MedTrackerTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.width(12.dp))
        
        MySwitch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
