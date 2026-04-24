package com.galeria.medtracker2.feature.auth.presentation.registration

/*@Composable
fun RegistrationScreen(
    modifier: Modifier = Modifier,
    viewModel: RegistrationViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val datePickerState = rememberDatePickerState()
    var selectedDate by remember { mutableStateOf("") }
    var checked by remember { mutableStateOf(true) }

    Column(
        modifier = modifier.fillMaxWidth().padding(12.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        if (state.value.showDatePicker) {
            Popup(
                onDismissRequest = { viewModel.dismissDatePicker() },
                alignment = Alignment.TopStart) {
                    Box(
                        modifier =
                            Modifier.fillMaxWidth()
                                .offset(y = 64.dp)
                                .shadow(elevation = 4.dp)
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(16.dp)) {
                            ModalDatePicker(
                                datePickerState = datePickerState,
                                onDateSelected = {
                                    it?.let {
                                        selectedDate = viewModel.convertMilliisToStringDate(it)
                                    }
                                    viewModel.updateBirthDate(it)
                                },
                                onDismiss = { viewModel.dismissDatePicker() },
                            )
                        }
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
            // Show password switch.
            Switch(
                checked = state.value.showPassword,
                onCheckedChange = { viewModel.isShowPasswordChecked(state.value.showPassword) },
                thumbContent =
                    if (checked) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        }
                    } else {
                        null
                    })
            MyTextField(
                value =
                    viewModel.convertMilliisToStringDate(
                        state.value.birthDate.toEpochMilliseconds()),
                onValueChange = {},
                label = "Birth Date",
                modifier = Modifier.clickable { viewModel.showDatePicker() },
            )
            FlyButton(onClick = { viewModel.showDatePicker() }) { Text("Show datePicker") }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                FlyTextButton(onClick = { *//* onNavigateBack *//* }) {
                    Text(stringResource(R.string.cancel))
                }

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
}*/

/*
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
                }) {
                    Text("OK")
                }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    ) {
        DatePicker(state = datePickerState)
    }
}
*/

/*
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
                state.value.birthDate.toLocalDateTime(timeZone = TimeZone.currentSystemDefault())
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
*/
