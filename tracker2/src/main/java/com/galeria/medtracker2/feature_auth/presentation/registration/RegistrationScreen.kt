package com.galeria.medtracker2.feature_auth.presentation.registration

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.galeria.medtracker2.feature_auth.presentation.login.RememberMeSwitch
import com.galeria.medtracker2.shared.components.FlyButton
import com.galeria.medtracker2.shared.components.FlyTextButton
import com.galeria.medtracker2.shared.components.MyTextField
import com.galeria.medtracker2.ui.theme.MedTrackerTheme
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun RegistrationScreen(viewModel: RegistrationViewModel = hiltViewModel()) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth().padding(12.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        OutlinedTextField(
            value =
                viewModel.convertMilliisToStringDate(state.value.birthDate.toEpochMilliseconds()),
            onValueChange = {},
            label = { Text("DOB") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { viewModel.showDatePicker() }) {
                    Icon(imageVector = Icons.Default.DateRange, contentDescription = "Select date")
                }
            },
            modifier = Modifier.fillMaxWidth().height(64.dp),
        )
        if (showDatePicker) {
            ModalDatePicker(
                datePickerState = datePickerState,
                onDateSelected = {
                    it?.let { selectedDate = viewModel.convertMilliisToStringDate(it) }
                    viewModel.updateBirthDate(it)
                },
                onDismiss = { viewModel.dismissDatePicker() },
            )
        }
    }
}

@Composable
fun ModalDatePicker(
    modifier: Modifier = Modifier,
    datePickerState: DatePickerState,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
) {
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    ) {
        DatePicker(state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen2(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    viewModel: RegistrationViewModel = hiltViewModel(),
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val datePickerState =
        rememberDatePickerState(
            initialSelectedDateMillis =
                state.value.birthDate
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
        )

    if (state.value.showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { viewModel.dismissDatePicker() },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedMillis =
                            datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                        val selectedDate =
                            Instant.ofEpochMilli(selectedMillis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissDatePicker() }) { Text("Cancel") }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }

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

        MyTextField(
            value =
                state.value.selectedBirthDate.format(DateTimeFormatter.ofPattern("MMMM dd yyyy")),
            onValueChange = {},
            label = "Birth Date",
            modifier = Modifier.clickable { viewModel.showDatePicker() },
        )
        FlyButton(onClick = { viewModel.showDatePicker() }) { Text("Show datePicker") }
        // Show password switch.
        RememberMeSwitch (
            checked = state.value.showPassword,
            onCheckedChange = { viewModel.isShowPasswordChecked(state.value.showPassword) },
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            FlyTextButton(onClick = onNavigateBack) { Text(stringResource(R.string.cancel)) }

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
