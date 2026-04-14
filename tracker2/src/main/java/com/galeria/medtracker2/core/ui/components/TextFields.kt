package com.galeria.medtracker2.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.galeria.medtracker2.core.ui.theme.MedTrackerTheme

@Composable
fun FlyTableTextField(
    modifier: Modifier = Modifier,
    value: String,
    isHeader: Boolean = false,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = false,
    isPrimaryColor: Boolean = false,
    supportingText: String? = null,
    textStyles: TextStyle = MedTrackerTheme.typography.bodyMedium,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
) {
    FlyTextField(
        modifier = modifier.width(150.dp),
        value = value,
        singleLine = true,
        readOnly = readOnly,
        isError = isError,
        textStyle =
            if (isHeader) {
                MedTrackerTheme.typography.bodyMediumEmphasized
            } else {
                MedTrackerTheme.typography.bodySmall
            },
        onValueChange = onValueChange,
        placeholder = { placeholder?.let { Text(it) } },
        label = {
            Text(
                if (isError) {
                    errorMessage!!
                } else {
                    label
                },
                style =
                    if (isHeader) {
                        MedTrackerTheme.typography.bodyMediumEmphasized
                    } else {
                        MedTrackerTheme.typography.bodySmall
                    })
        },
        maxLines = 1,
        colors =
            TextFieldDefaults.colors(
                unfocusedContainerColor =
                    if (isPrimaryColor) {
                        MedTrackerTheme.colors.secondaryBackground
                    } else {
                        MedTrackerTheme.colors.primaryBackground
                    },
                unfocusedIndicatorColor = MedTrackerTheme.colors.primaryBackground,
                disabledIndicatorColor = MedTrackerTheme.colors.primary400,
                focusedIndicatorColor = MedTrackerTheme.colors.primary400,
                focusedContainerColor = MedTrackerTheme.colors.secondaryBackground,
                errorIndicatorColor = MedTrackerTheme.colors.sysError,
                focusedLabelColor = MedTrackerTheme.colors.primary400,
                unfocusedLabelColor = MedTrackerTheme.colors.secondaryLabel,
                errorLabelColor = MedTrackerTheme.colors.sysError,
                errorSupportingTextColor = MedTrackerTheme.colors.sysError,
                errorCursorColor = MedTrackerTheme.colors.sysError,
            ),
    )
}

@Composable
fun FlyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = MedTrackerTheme.typography.bodyMedium,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource? = null,
    shape: Shape = TextFieldDefaults.shape,
    colors: TextFieldColors = TextFieldDefaults.colors()
) {
    BasicTextField(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(0.dp /* top = 1.dp, bottom = 1.dp */)
                .border(
                    width = 0.25.dp,
                    color = MedTrackerTheme.colors.opaqueSeparator,
                    shape = RectangleShape),
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        keyboardOptions = keyboardOptions,
        textStyle = textStyle,
        decorationBox = { innerTextField ->
            Box(
                modifier =
                    Modifier.fillMaxWidth()
                        .background(
                            MedTrackerTheme.colors.primaryBackground, RoundedCornerShape(6.dp))
                        .height(32.dp)
                        .padding(horizontal = 4.dp),
                contentAlignment = Alignment.CenterStart) {
                    if (value.isEmpty()) {
                        Text("Add Medication Name", color = Color.LightGray)
                    }
                    innerTextField()
                }
        })
}

@Composable
fun MyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPrimaryColor: Boolean = false,
    supportingText: String? = null,
    placeholder: String? = null,
    enabled: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null,
    readOnly: Boolean = false,
    maxLines: Int = 100,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
) {
    TextField(
        value = value,
        singleLine = true,
        readOnly = readOnly,
        isError = isError,
        textStyle = MedTrackerTheme.typography.bodyMedium,
        onValueChange = onValueChange,
        placeholder = { placeholder?.let { Text(it) } },
        maxLines = maxLines,
        label = {
            Text(
                if (isError) {
                    errorMessage!!
                } else {
                    label
                })
        },
        supportingText = { supportingText?.let { Text(it) } },
        enabled = enabled,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        modifier = modifier,
        shape =
            RoundedCornerShape(
                topStart = 12.dp, topEnd = 12.dp, bottomEnd = 12.dp, bottomStart = 12.dp),
        colors =
            TextFieldDefaults.colors(
                unfocusedContainerColor =
                    if (isPrimaryColor) {
                        MedTrackerTheme.colors.primaryBackground
                    } else {
                        MedTrackerTheme.colors.secondaryBackground
                    },
                unfocusedIndicatorColor = MedTrackerTheme.colors.sysTransparent,
                disabledIndicatorColor = MedTrackerTheme.colors.sysTransparent,
                focusedIndicatorColor = MedTrackerTheme.colors.primary400,
                focusedContainerColor = MedTrackerTheme.colors.primaryBackground,
                errorIndicatorColor = MedTrackerTheme.colors.sysError,
                focusedLabelColor = MedTrackerTheme.colors.primary400,
                unfocusedLabelColor = MedTrackerTheme.colors.secondaryLabel,
                errorLabelColor = MedTrackerTheme.colors.sysError,
                errorSupportingTextColor = MedTrackerTheme.colors.sysError,
                errorCursorColor = MedTrackerTheme.colors.sysError,
            ),
    )
}

// TODO.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { label?.let { Text(it, color = MedTrackerTheme.colors.primaryLabel) } },
        placeholder = {
            placeholder?.let { Text(it, color = MedTrackerTheme.colors.secondaryLabel) }
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = isError,
        enabled = enabled,
        readOnly = readOnly,
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors =
            TextFieldDefaults.colors(
                focusedTextColor = MedTrackerTheme.colors.primaryLabel,
                unfocusedTextColor = MedTrackerTheme.colors.primaryLabel,
                disabledTextColor = MedTrackerTheme.colors.tertiaryLabel,
                errorTextColor = MedTrackerTheme.colors.primaryLabel,
                //
                focusedContainerColor = MedTrackerTheme.colors.sysTransparent,
                unfocusedContainerColor = MedTrackerTheme.colors.sysTransparent,
                disabledContainerColor = MedTrackerTheme.colors.sysTransparent,
                errorContainerColor = MedTrackerTheme.colors.sysTransparent,
                //
                unfocusedIndicatorColor = MedTrackerTheme.colors.sysTransparent,
                disabledIndicatorColor = MedTrackerTheme.colors.sysTransparent,
                errorIndicatorColor = MedTrackerTheme.colors.sysTransparent,
            ),
    )
}

@Composable
internal fun CustomTextField(
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    placeholderText: String = "Placeholder",
    fontSize: TextUnit = MedTrackerTheme.typography.bodyMedium.fontSize
) {
    var text by rememberSaveable { mutableStateOf("") }
    BasicTextField(
        modifier =
            modifier
                .background(
                    MedTrackerTheme.colors.secondaryBackground,
                    MaterialTheme.shapes.small,
                )
                .fillMaxWidth(),
        value = text,
        onValueChange = { text = it },
        singleLine = true,
        cursorBrush = SolidColor(MedTrackerTheme.colors.primary400),
        textStyle =
            LocalTextStyle.current.copy(
                color = MedTrackerTheme.colors.primaryBackgroundGrouped, fontSize = fontSize),
        decorationBox = { innerTextField ->
            Row(modifier, verticalAlignment = Alignment.CenterVertically) {
                if (leadingIcon != null) leadingIcon()

                Box(Modifier.weight(1f)) {
                    if (text.isEmpty()) {
                        Text(
                            text = placeholderText,
                            style =
                                LocalTextStyle.current.copy(
                                    color = MedTrackerTheme.colors.primaryLabel,
                                    fontSize = fontSize))
                    }
                    innerTextField()
                }
                if (trailingIcon != null) trailingIcon()
            }
        })
}

@Preview()
@Composable
fun FlyTextFieldPreview() {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(24.dp)) {
        /*   FlyTextField(
          value = "",
          onValueChange = {},
          label = "Label",
          supportingText = "Supporting Text",
          placeholder = "Placeholder",
          enabled = true,
          isError = false,
          errorMessage = null,
          modifier = Modifier,
          visualTransformation = VisualTransformation.None,
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        ) */
        /*     FlyTextField(
          value = "",
          onValueChange = {},
          label = "Label",
          supportingText = "Supporting Text",
          placeholder = "Placeholder",
          enabled = true,
          isError = false,
          errorMessage = null,
          modifier = Modifier,
          visualTransformation = VisualTransformation.None,
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        )
        CustomTextField(
          leadingIcon = {
            Icon(
              imageVector = Icons.Default.Email,
              contentDescription = null
            )
          },
          trailingIcon = {
            Icon(
              imageVector = Icons.Default.Lock,
              contentDescription = null
            )
          },
          placeholderText = "Enter your email",
          modifier = Modifier
        )
        MyOutlinedTextField(
          value = "value",
          onValueChange = {},
          label = "label",
          placeholder = "placeholder",
        ) */
        /*     FlyTableTextField(
          value = "",
          onValueChange = {},
          label = "label",
          supportingText = "Supporting Text",
          placeholder = "Placeholder",
          enabled = true,
          isError = false,
          errorMessage = null,
          modifier = Modifier,
          visualTransformation = VisualTransformation.None,
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        ) */
    }
}

class FlyTableTextFieldPreviewParameterProvider : PreviewParameterProvider<Boolean> {

    override val values = sequenceOf(true, false)
    override val count: Int = values.count()
}

@Preview
@Composable
fun FlyTableTextFieldPreview(
    @PreviewParameter(FlyTableTextFieldPreviewParameterProvider::class) isPrimaryColor: Boolean,
) {
    FlyTableTextField(
        value = "value",
        onValueChange = {},
        isPrimaryColor = isPrimaryColor,
        label = "label",
    )
}
