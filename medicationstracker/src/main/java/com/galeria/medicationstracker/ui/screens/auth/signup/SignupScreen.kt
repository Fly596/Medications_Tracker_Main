package com.galeria.medicationstracker.ui.screens.auth.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.galeria.medicationstracker.ui.components.GPrimaryButton
import com.galeria.medicationstracker.ui.components.GTextButton
import com.galeria.medicationstracker.ui.componentsOld.MyTextField
import com.galeria.medicationstracker.ui.screens.auth.login.RememberMeSwitch
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme

@Composable
fun SignupScreen(
  modifier: Modifier = Modifier,
  passedEmail: String = "",
  navigateHome: () -> Unit,
  viewModel: RegisterScreenViewModel = hiltViewModel(),
) {
  // LaunchedEffect(Unit) { viewModel.updateEmail(passedEmail) }
  val state = viewModel.registerScreenState.collectAsStateWithLifecycle()
  
  Scaffold(
    containerColor = MedTrackerTheme.colors.secondaryBackground,
    topBar = {
      Text(
        stringResource(R.string.sign_up_screen_title),
        style = MedTrackerTheme.typography.display2Emphasized,
        modifier = Modifier.padding(16.dp)
      )
    }
  ) { innerPadding ->
    Column(
      modifier = modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(16.dp)
    ) {
      MyTextField(
        value = state.value.name,
        onValueChange = { viewModel.updateName(it) },
        isPrimaryColor = true,
        label = "Name",
        placeholder = "Name",
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
        label = "Password",
        placeholder = "6 or more characters",
        supportingText = "6 or more characters",
        modifier = Modifier.fillMaxWidth(),
        visualTransformation =
          if (state.value.showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
      )
      // Show password switch.
      RememberMeSwitch(
        checked = state.value.showPassword,
        onCheckedChange = { viewModel.isShowPasswordChecked(state.value.showPassword) },
      )
      val context = LocalContext.current
      
      Row(verticalAlignment = Alignment.CenterVertically) {
        GTextButton(onClick = navigateHome) { Text(text = "Cancel") }
        
        Spacer(modifier = Modifier.weight(1f))
        
        GPrimaryButton(onClick = {
          viewModel.registerUser(
            onSuccessRegistration = navigateHome,
          )
        }) {
          Text(text = "Create Account")
        }
      }
    }
  }
}