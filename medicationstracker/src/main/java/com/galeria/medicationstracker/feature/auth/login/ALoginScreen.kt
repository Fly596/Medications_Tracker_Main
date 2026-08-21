package com.galeria.medicationstracker.feature.auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medicationstracker.R
import com.galeria.medicationstracker.ui.componentsOld.FlyButton
import com.galeria.medicationstracker.ui.componentsOld.FlyTonalButton
import com.galeria.medicationstracker.ui.componentsOld.MyTextField
import com.galeria.medicationstracker.ui.screens.auth.login.ALoginScreenViewModel
import com.galeria.medicationstracker.ui.screens.auth.login.ALoginUiEffect
import com.galeria.medicationstracker.ui.screens.auth.login.RememberMeSwitch
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme

@Composable
fun ALoginScreen(
  onNavigateToHome: () -> Unit = {},
  onRegistration: () -> Unit = {},
  onResetPassword: () -> Unit = {},
  viewModel: ALoginScreenViewModel = hiltViewModel(),
) {
  val state = viewModel.uiState.collectAsStateWithLifecycle()

  val snackbarHostState = remember { SnackbarHostState() }

  LaunchedEffect(key1 = Unit) {
    viewModel.effectFlow.collect { effect ->
      when (effect) {
        is ALoginUiEffect.NavigateToHome -> {
          onNavigateToHome()
        }

        is ALoginUiEffect.NavigateToRegistration -> {
          onRegistration()
        }

        is ALoginUiEffect.NavigateToResetPassword -> {
          onResetPassword()
        }

        is ALoginUiEffect.ShowSnackbar -> {
          snackbarHostState.showSnackbar(message = effect.message, withDismissAction = true)
        }
      }
    }
  }

  Scaffold(
    snackbarHost = {
      SnackbarHost(snackbarHostState)
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(16.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        stringResource(R.string.sign_in_screen_title),
        style = MedTrackerTheme.typography.display2Emphasized,
      )

      Spacer(modifier = Modifier.weight(1f))
      Column(verticalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier) {
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
          placeholder = stringResource(R.string._6_or_more_characters),
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
          onClick = {
            viewModel.onLoginClick()
          },
          enabled = !state.value.isLoading,
        ) {
          Text(text = stringResource(R.string.sign_in))
        }

        Spacer(modifier = Modifier.weight(1f))

        FlyTonalButton(onClick = { onRegistration() }, enabled = true) {
          Text(text = stringResource(R.string.create_account))
        }
      }

      Spacer(modifier = Modifier.weight(1f))

      Button(onClick = {
        viewModel.onLoginClick()
        onNavigateToHome()
      }) {
        Text("Login")
      }
    }

  }
}