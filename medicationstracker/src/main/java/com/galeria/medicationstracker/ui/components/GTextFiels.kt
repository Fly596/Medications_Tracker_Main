package com.galeria.medicationstracker.ui.components

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme.colors
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme.shapes
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme.typography

@Composable
fun GTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    isPrimaryColor: Boolean = false,
    supportingText: String? = null,
    placeholder: String? = null,
    singleLine: Boolean = false,
    enabled: Boolean = true,
    textStyle: TextStyle = typography.bodySmall,
    isError: Boolean = false,
    errorMessage: String? = null,
    readOnly: Boolean = false,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
) {
    TextField(
        value = value,
        minLines = minLines,
        singleLine = singleLine,
        readOnly = readOnly,
        isError = isError,
        textStyle = textStyle,
        onValueChange = onValueChange,
        placeholder = { placeholder?.let { Text(it) } },
        label = {
            label?.let {
                Text(
                    if (isError) {
                        errorMessage!!
                    } else {
                        label
                    }
                )
            }
        },
        supportingText = { supportingText?.let { Text(it) } },
        enabled = enabled,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        modifier = modifier,
        shape = shapes.small,
        colors =
            TextFieldDefaults.colors(
                unfocusedContainerColor =
                    if (isPrimaryColor) {
                        colors.primaryBackground
                    } else {
                        colors.secondaryBackground

                    },
                unfocusedIndicatorColor = colors.sysBlack,
                disabledIndicatorColor = colors.sysTransparent,
                focusedIndicatorColor = colors.sysBlack,
                focusedContainerColor = colors.primaryBackground,
                errorIndicatorColor = colors.sysError,
                focusedLabelColor = colors.sysBlack,
                unfocusedLabelColor = colors.primaryLabel,
                errorLabelColor = colors.sysError,
                errorSupportingTextColor = colors.sysError,
                errorCursorColor = colors.sysError,
            ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GBasicTextField(
    modifier: Modifier = Modifier,
    prefixModifier: Modifier = Modifier,
    suffixModifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    isPrimaryColor: Boolean = false,
    supportingText: String? = null,
    placeholder: String? = null,
    singleLine: Boolean = false,
    enabled: Boolean = true,
    textStyle: TextStyle = typography.bodyMedium,
    prefixStyle: TextStyle = typography.bodyLarge,
    suffixStyle: TextStyle = typography.bodyMedium,
    isError: Boolean = false,
    errorMessage: String? = null,
    alignEnd: Boolean = true,
    readOnly: Boolean = false,
    minLines: Int = 1,
    prefix: String? = null,
    suffix: String? = null,
    contentPaddingValues: PaddingValues = TextFieldDefaults.contentPaddingWithoutLabel(
        top = 4.dp,
        bottom = 6.dp,
        end = 4.dp,
        start = 0.dp
    ),
    interactionSource: InteractionSource = MutableInteractionSource(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        keyboardOptions = keyboardOptions,
        readOnly = readOnly,
        textStyle = textStyle.copy(
            textAlign = if (alignEnd) {
                TextAlign.End
            } else {
                TextAlign.Start
            }
        ),
        minLines = minLines,
    ) { innerTextField ->
        TextFieldDefaults.DecorationBox(
            isError = isError,
            value = value,
            prefix = {
                prefix?.let {
                    Text(
                        text = prefix,
                        modifier = prefixModifier.padding(end = 16.dp),
                        style = prefixStyle
                    )
                }
            },
            suffix = {
                suffix?.let {
                    Text(
                        text = suffix,
                        modifier = suffixModifier,
                        style = suffixStyle
                    )
                }
            },
            placeholder = {
                placeholder?.let {
                    placeholder
                }
            },
            innerTextField = innerTextField,
            enabled = enabled,
            singleLine = singleLine,
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
            contentPadding = contentPaddingValues,
            shape = shapes.small,
            colors =
                TextFieldDefaults.colors(
                    unfocusedContainerColor = if (isPrimaryColor) {
                        colors.sysBlack
                    } else {
                        colors.sysBlack
                    },
                    focusedContainerColor = if (isPrimaryColor) {
                        colors.sysBlack
                    } else {
                        colors.sysBlack
                    },
                    unfocusedIndicatorColor = colors.sysBlack,
                    focusedIndicatorColor = colors.sysBlack,
                    errorIndicatorColor = colors.sysError,
                    disabledIndicatorColor = colors.sysTransparent,
                    focusedLabelColor = colors.sysBlack,
                    unfocusedLabelColor = colors.primaryLabel,
                    errorLabelColor = colors.sysError,
                    errorSupportingTextColor = colors.sysError,
                    errorCursorColor = colors.sysError,
                ),
            container = {
                // Custom container with only bottom border
                val colorNoError: Color = colors.separator
                val colorError: Color = colors.sysError
                Box(
                    modifier = Modifier
                        .drawBehind {
                            val strokeWidth = 1.dp.toPx()
                            val y = size.height - strokeWidth / 2
                            drawLine(
                                color = if (isError) {
                                    colorError
                                } else {
                                    colorNoError
                                },
                                start = Offset(0f, y),
                                end = Offset(size.width, y),
                                strokeWidth = strokeWidth
                            )
                        }
                )
            }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun GTFPreview() {
    MedTrackerTheme {
        val interactionSource = remember { MutableInteractionSource() }

        Column(modifier = Modifier.fillMaxSize()) {
            GBasicTextField(
                value = "value",
                onValueChange = {},
                interactionSource = interactionSource,
                prefix = "Prefix"
            )
        }
    }
}