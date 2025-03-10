package com.galeria.medicationstracker.ui.screens.auth.accountrecovery

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.galeria.medicationstracker.R
import com.galeria.medicationstracker.ui.componentsOld.FlyButton
import com.galeria.medicationstracker.ui.componentsOld.FlyTextButton
import com.galeria.medicationstracker.ui.componentsOld.MyTextField
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme

@Composable
fun AccountRecoveryScreen(
    passedEmail: String = "",
    navigateHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AccountRecoveryScreenViewModel = viewModel(),
) {
    LaunchedEffect(Unit) { viewModel.updateEmail(passedEmail) }
    val state = viewModel.accountRecoveryScreenState

    Column(
        modifier = modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Top,
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            stringResource(R.string.recover_password_screen_title),
            style = MedTrackerTheme.typography.display2Emphasized,
        )

        Spacer(modifier = Modifier.weight(1f))

        MyTextField(
            value = state.email,
            onValueChange = { viewModel.updateEmail(it) },
            isError = state.emailError?.isNotEmpty() ?: false,
            isPrimaryColor = true,
            errorMessage = state.emailError,
            label = "Email",
            placeholder = "Email",
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        )

        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            FlyTextButton(onClick = { navigateHome.invoke() }) { Text(text = "Cancel") }

            Spacer(modifier = Modifier.weight(1f))

            FlyButton(
                onClick = {
                    viewModel.resetPassword(state.email)
                    navigateHome.invoke()
                },
                enabled = true,
            ) {
                Text(text = "Reset Password")
            }
        }
        Spacer(modifier = Modifier.height(40.dp).weight(1f))
    }
}

@Preview(showBackground = true)
@Composable
fun AccountRecoveryScreenPreview() {
    MedTrackerTheme { AccountRecoveryScreen(passedEmail = "test@example.com", navigateHome = {}) }
}
