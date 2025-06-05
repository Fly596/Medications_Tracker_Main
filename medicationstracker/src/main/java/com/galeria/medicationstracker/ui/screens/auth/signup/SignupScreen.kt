package com.galeria.medicationstracker.ui.screens.auth.signup

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medicationstracker.R
import com.galeria.medicationstracker.ui.componentsOld.FlyButton
import com.galeria.medicationstracker.ui.componentsOld.FlyTextButton
import com.galeria.medicationstracker.ui.componentsOld.MyTextField
import com.galeria.medicationstracker.ui.screens.auth.login.RememberMeSwitch
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme

@Composable
fun SignupScreen(
    modifier: Modifier = Modifier,
    passedEmail: String = "",
    navigateHome: () -> Unit,
    viewModel: SignupScreenViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) { viewModel.updateEmail(passedEmail) }
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Top,
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            stringResource(R.string.sign_up_screen_title),
            style = MedTrackerTheme.typography.display2Emphasized,
        )

        Spacer(modifier = Modifier.weight(1f))

        MyTextField(
            value = state.value.name,
            onValueChange = { viewModel.updateUserName(it) },
            isPrimaryColor = true,
            label = stringResource(R.string.name),
            placeholder = stringResource(R.string.name),
            modifier = Modifier.fillMaxWidth(),
        )

        MyTextField(
            value = state.value.email,
            onValueChange = { viewModel.updateEmail(it) },
            isPrimaryColor = true,
            isError = state.value.emailErrorMessage?.isNotEmpty() ?: false,
            errorMessage = state.value.emailErrorMessage,
            label = "Email",
            placeholder = "",
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        )

        MyTextField(
            value = state.value.password,
            onValueChange = { viewModel.updatePassword(it) },
            isPrimaryColor = true,
            isError = state.value.passwordErrorMessage?.isNotEmpty() ?: false,
            errorMessage = state.value.passwordErrorMessage,
            label = stringResource(R.string.password),
            placeholder = stringResource(R.string._6_or_more_characters),
            supportingText = stringResource(R.string._6_or_more_characters),
            modifier = Modifier.fillMaxWidth(),
            visualTransformation =
                if (state.value.showPassword) VisualTransformation.None
                else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        )
        // Show password switch.
        RememberMeSwitch(
            checked = state.value.showPassword,
            onCheckedChange = { viewModel.isShowPasswordChecked(state.value.showPassword) },
        )
        // Список типов пользователя.

        Spacer(modifier = Modifier.height(16.dp))
        val context = LocalContext.current

        Row(verticalAlignment = Alignment.CenterVertically) {
            FlyTextButton(onClick = navigateHome) { Text(stringResource(R.string.cancel)) }

            Spacer(modifier = Modifier.weight(1f))

            FlyButton(onClick = { viewModel.onRegisterClick() }) {
                Text(text = stringResource(R.string.create_account))
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        // просто, чтобы положение полей и кнопок было таким же, как на экране входа.
        FlyTextButton(onClick = {}, enabled = false) { Text(text = "") }
    }
}
