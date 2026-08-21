package com.galeria.medicationstracker.feature.auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.galeria.medicationstracker.ui.screens.auth.login.ALoginScreenViewModel
import com.galeria.medicationstracker.ui.screens.auth.login.ALoginUiEffect

@Composable
fun ALoginScreen(
  onNavigateToHome: () -> Unit = {},
  onRegistration: () -> Unit = {},
  onResetPassword: () -> Unit = {},
  viewModel: ALoginScreenViewModel = hiltViewModel(),
) {
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
      Button(onClick = { viewModel.onLoginClick() }) {
        Text("Login")
      }
    }

  }
}