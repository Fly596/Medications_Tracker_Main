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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.galeria.medicationstracker.R
import com.galeria.medicationstracker.data.UserType
import com.galeria.medicationstracker.ui.components.GPrimaryButton
import com.galeria.medicationstracker.ui.components.GTextButton
import com.galeria.medicationstracker.ui.componentsOld.FlyButton
import com.galeria.medicationstracker.ui.componentsOld.FlySimpleCard
import com.galeria.medicationstracker.ui.componentsOld.FlyTextButton
import com.galeria.medicationstracker.ui.componentsOld.MyRadioButton
import com.galeria.medicationstracker.ui.componentsOld.MyTextField
import com.galeria.medicationstracker.ui.screens.auth.login.RememberMeSwitch
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme

@Composable
fun SignupScreen(
  modifier: Modifier = Modifier,
  passedEmail: String = "",
  navigateHome: () -> Unit,
  viewModel: SignupScreenViewModel = viewModel(),
) {
  LaunchedEffect(Unit) { viewModel.updateEmail(passedEmail) }
  val state = viewModel.signupScreenState.collectAsStateWithLifecycle()
  
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
      modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
          .padding(16.dp),
      verticalArrangement = Arrangement.Center
    ) {
      MyTextField(
        value = state.value.firstName,
        onValueChange = { viewModel.updateUserName(it) },
        isPrimaryColor = true,
        label = "Name",
        placeholder = "Name",
        modifier = Modifier.fillMaxWidth(),
      )
      MyTextField(
        value = state.value.age.toString(),
        onValueChange = {
          if (it.isNotEmpty()) {
            viewModel.updateUserAge(it.toInt())
          } else {
            viewModel.updateUserAge(0)
          }
        },
        isPrimaryColor = true,
        label = "Age",
        placeholder = "Age",
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
      // Список типов пользователя.
      var selectedType by remember { mutableStateOf(state.value.userType) }
      val options = UserType.entries.toTypedArray()
      
      Spacer(modifier = Modifier.height(16.dp))
      val context = LocalContext.current
      
      Row(verticalAlignment = Alignment.CenterVertically) {
        GTextButton(onClick = navigateHome) { Text(text = "Cancel") }
        
        Spacer(modifier = Modifier.weight(1f))
        
        GPrimaryButton(onClick = {
          viewModel.onRegisterClick(
            context,
            onSignupSuccess = navigateHome
          )
        }) {
          Text(text = "Create Account")
        }
      }
    }
  }
  
}

/**
 * Composable function that displays a radio button group for selecting user types.
 *
 * This function iterates through available user types and renders a radio button for each type.
 * The selected type is tracked and updated based on user interaction.
 *
 * @param viewModel The SignupScreenViewModel instance providing data and state.value for the signup screen.
 */
@Composable
fun UserTypesSelection(viewModel: SignupScreenViewModel) {
  var selectedType by remember { mutableStateOf(viewModel.signupScreenState.value.userType) }
  val options = UserType.entries.toTypedArray()
  
  Column(
    modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(8.dp),
    horizontalAlignment = Alignment.Start
  ) {
    options.forEach { type ->
      Row {
        MyRadioButton(
          selected = selectedType == type,
          onClick = {
            selectedType = type
          },
          caption = type.toString().lowercase()
        )
      }
    }
  }
  
}
