package com.galeria.medicationstracker.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme


@Composable
fun GDropdownList(
    modifier: Modifier = Modifier,
    items: List<String>, onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember {
        mutableStateOf(
            items.firstOrNull() ?: "No Items"
        )
    }
    Box(
        modifier = modifier/* .fillMaxSize() */,
        contentAlignment = Alignment.Center
    ) {
        Column {
            GOutlinedButton(onClick = { expanded = true }) {
                Text(selectedItem)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = MedTrackerTheme.colors.secondaryBackground,
                modifier = Modifier.fillMaxWidth()
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                item,
                                style = MedTrackerTheme.typography.bodyMedium
                            )
                        },
                        onClick = {
                            selectedItem = item
                            expanded = false
                            onItemSelected(item) // Call back with the selected item
                        },
                        colors = MenuItemColors(
                            textColor = MedTrackerTheme.colors.sysBlack,
                            leadingIconColor = MedTrackerTheme.colors.sysBlack,
                            trailingIconColor = MedTrackerTheme.colors.sysBlack,
                            disabledTextColor = MedTrackerTheme.colors.sysBlack,
                            disabledLeadingIconColor = MedTrackerTheme.colors.sysBlack,
                            disabledTrailingIconColor = MedTrackerTheme.colors.sysBlack
                        ),
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = false, showBackground = true)
@Composable
fun DropdownMenuExamplePreview() {
     val items = listOf("Item 1", "Item 2", "Item 3")
    MedTrackerTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            GDropdownList(items = items, onItemSelected = { {}})
        }
    }
}