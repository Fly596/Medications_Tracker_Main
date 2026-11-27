package com.galeria.medicationstracker.feature.medications.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medicationstracker.core.ui.theme.MedTrackerTheme
import com.galeria.medicationstracker.core.ui.theme.MedTrackerTheme.typography
import com.galeria.medicationstracker.data.MedicationForm
import com.galeria.medicationstracker.domain.medications.model.Medication
import com.galeria.medicationstracker.feature.medications.presentation.MedicationsViewModel
import com.galeria.medicationstracker.utils.getTodaysDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun MedicationsScreen(
    modifier: Modifier = Modifier,
    onAddClick: () -> Unit = {},
    onViewClick: (String) -> Unit = {},
    onEditClick: (String) -> Unit = {},
    medicationsViewModel: MedicationsViewModel = hiltViewModel(),
) {
    val uiState by medicationsViewModel.uiState.collectAsStateWithLifecycle()
    MedicationsScreenContent(
        medications = uiState.userMedications,
        modifier = modifier,
        onAddClick = onAddClick,
        onViewClick = onViewClick,
        onEditClick = onEditClick,
        onRemoveMedClick = { id ->
            medicationsViewModel.deleteMedicationFromFirestore(id)
        }
    )
}

@Composable
fun MedicationsScreenContent(
    medications: List<Medication>,
    modifier: Modifier = Modifier,
    onAddClick: () -> Unit = {},
    onViewClick: (String) -> Unit = {},
    onEditClick: (String) -> Unit = {},
    onRemoveMedClick: (String) -> Unit = {},
) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearchVisible by remember { mutableStateOf(false) }

    val filteredMedications = remember(medications, searchQuery) {
        if (searchQuery.isBlank()) {
            medications
        } else {
            medications.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                        it.dosage.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Scaffold(
        containerColor = MedTrackerTheme.colors.secondaryBackground,
        topBar = {
            MedicationsTopAppBar(
                activeCount = medications.size,
                isSearchVisible = isSearchVisible,
                onToggleSearch = {
                    isSearchVisible = !isSearchVisible
                    if (!isSearchVisible) searchQuery = ""
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { onAddClick() },
                containerColor = MedTrackerTheme.colors.primary500,
                contentColor = Color.White,
                shape = RoundedCornerShape(24.dp),
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Medication",
                        modifier = Modifier.size(24.dp)
                    )
                },
                text = {
                    Text(
                        text = "Add Medication",
                        style = typography.labelLargeEmphasized,
                        color = Color.White
                    )
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Search Bar (Collapsible with smooth animation)
            AnimatedVisibility(
                visible = isSearchVisible,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                MedicationSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onClearQuery = { searchQuery = "" }
                )
            }

            // Medications List or Empty State
            if (filteredMedications.isEmpty()) {
                MedicationsEmptyState(
                    hasSearchQuery = searchQuery.isNotBlank(),
                    onAddClick = onAddClick,
                    onClearSearch = { searchQuery = "" }
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredMedications, key = { it.id }) { med ->
                        MedicationCardItem(
                            medication = med,
                            onViewMed = { onViewClick(med.id) },
                            onEditClick = { onEditClick(med.id) },
                            onRemoveMedClick = { onRemoveMedClick(med.id) }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(80.dp)) // Extra space for FAB
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MedicationsTopAppBar(
    activeCount: Int,
    isSearchVisible: Boolean,
    onToggleSearch: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = getTodaysDate().format(DateTimeFormatter.ofPattern("EEEE, MMM d")),
                    style = typography.bodySmall.copy(
                        color = MedTrackerTheme.colors.secondaryLabel
                    )
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "My Medications",
                        style = typography.display3Emphasized.copy(fontSize = 24.sp),
                        color = MedTrackerTheme.colors.primaryLabel
                    )
                    Surface(
                        color = MedTrackerTheme.colors.primaryTinted,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "$activeCount active",
                            style = typography.labelMedium,
                            color = MedTrackerTheme.colors.primary500,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        },
        actions = {
            IconButton(
                onClick = onToggleSearch,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MedTrackerTheme.colors.primaryBackground)
            ) {
                Icon(
                    imageVector = if (isSearchVisible) Icons.Default.Clear else Icons.Default.Search,
                    contentDescription = "Search Medications",
                    tint = MedTrackerTheme.colors.primary500
                )
            }
        },
        windowInsets = WindowInsets(top = 16, bottom = 8),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MedTrackerTheme.colors.secondaryBackground
        )
    )
}

@Composable
private fun MedicationSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Search by name or dosage...", style = typography.bodyMedium) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MedTrackerTheme.colors.secondaryLabel
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClearQuery) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear search",
                        tint = MedTrackerTheme.colors.secondaryLabel
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MedTrackerTheme.colors.primaryBackground,
            unfocusedContainerColor = MedTrackerTheme.colors.primaryBackground,
            focusedBorderColor = MedTrackerTheme.colors.primary500,
            unfocusedBorderColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(16.dp))
    )
}

@Composable
fun MedicationCardItem(
    medication: Medication,
    onViewMed: () -> Unit,
    onEditClick: () -> Unit,
    onRemoveMedClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20.dp)
) {
    val badgeConfig = getFormBadgeConfig(medication.form)

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable { onViewMed() }
            .animateContentSize(),
        shape = shape,
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 6.dp
        ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MedTrackerTheme.colors.primaryBackground,
            contentColor = MedTrackerTheme.colors.primaryLabel
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Row: Badge Icon + Name & Form Tag + Chevron
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Form Icon Badge
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(badgeConfig.bgColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = badgeConfig.icon,
                        contentDescription = badgeConfig.label,
                        tint = badgeConfig.iconColor,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Name & Dosage / Form Tag
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = medication.name,
                        style = typography.title1Emphasized.copy(fontSize = 18.sp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Dosage Chip
                        if (medication.dosage.isNotBlank()) {
                            PillTag(
                                text = medication.dosage,
                                containerColor = MedTrackerTheme.colors.primaryLight,
                                contentColor = MedTrackerTheme.colors.primary500
                            )
                        }
                        // Form Chip
                        PillTag(
                            text = badgeConfig.label,
                            containerColor = badgeConfig.bgColor,
                            contentColor = badgeConfig.iconColor
                        )
                    }
                }

                // Chevron icon indicating clickability
                Icon(
                    imageVector = Icons.Outlined.ChevronRight,
                    contentDescription = "View Details",
                    tint = MedTrackerTheme.colors.tertiaryLabel,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Divider Line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MedTrackerTheme.colors.separator.copy(alpha = 0.3f))
            )

            // Info Chips: Time & Frequency
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                InfoChip(
                    icon = Icons.Outlined.AccessTime,
                    label = formatIntakeTime(medication.intakeTime)
                )
                InfoChip(
                    icon = Icons.Outlined.CalendarToday,
                    label = formatDaysOfWeek(medication.daysOfWeek)
                )
            }

            // Action Buttons (Edit & Delete)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Edit Button
                Surface(
                    onClick = onEditClick,
                    shape = RoundedCornerShape(12.dp),
                    color = MedTrackerTheme.colors.secondaryBackground,
                    contentColor = MedTrackerTheme.colors.primary500,
                    modifier = Modifier.height(36.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Edit",
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Edit",
                            style = typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Delete Button
                Surface(
                    onClick = onRemoveMedClick,
                    shape = RoundedCornerShape(12.dp),
                    color = MedTrackerTheme.colors.sysError.copy(alpha = 0.1f),
                    contentColor = MedTrackerTheme.colors.sysError,
                    modifier = Modifier.height(36.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Delete",
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Delete",
                            style = typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                }
            }
        }
    }
}

// Legacy wrapper to ensure full backward compatibility with existing usages
@Composable
fun FlyElevatedCardMedsList(
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit,
    onRemoveMedClick: () -> Unit,
    onViewMed: () -> Unit,
    medication: Medication? = null,
    shape: Shape = RoundedCornerShape(20.dp),
) {
    val medToDisplay = medication ?: Medication(
        id = "preview_1",
        name = "Aspirin Extra",
        dosage = "500 mg",
        form = MedicationForm.TABLET,
        startDate = null,
        endDate = null,
        daysOfWeek = listOf("Mon", "Wed", "Fri"),
        intakeTime = LocalTime.of(8, 30)
    )
    MedicationCardItem(
        medication = medToDisplay,
        onViewMed = onViewMed,
        onEditClick = onEditClick,
        onRemoveMedClick = onRemoveMedClick,
        modifier = modifier,
        shape = shape
    )
}

@Composable
private fun PillTag(
    text: String,
    containerColor: Color,
    contentColor: Color
) {
    Surface(
        color = containerColor,
        contentColor = contentColor,
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = text,
            style = typography.labelSmall.copy(fontWeight = FontWeight.Medium),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}

@Composable
private fun InfoChip(
    icon: ImageVector,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MedTrackerTheme.colors.secondaryLabel,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = label,
            style = typography.bodySmall.copy(color = MedTrackerTheme.colors.secondaryLabel)
        )
    }
}

@Composable
private fun MedicationsEmptyState(
    hasSearchQuery: Boolean,
    onAddClick: () -> Unit,
    onClearSearch: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp, horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            MedTrackerTheme.colors.primaryTinted,
                            MedTrackerTheme.colors.primaryLight
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Medication,
                contentDescription = null,
                tint = MedTrackerTheme.colors.primary500,
                modifier = Modifier.size(40.dp)
            )
        }

        Text(
            text = if (hasSearchQuery) "No Medications Found" else "No Medications Added",
            style = typography.title1Emphasized.copy(fontSize = 20.sp),
            color = MedTrackerTheme.colors.primaryLabel
        )

        Text(
            text = if (hasSearchQuery)
                "Try searching with a different medication name or dosage."
            else
                "Keep track of your daily prescriptions and health routines. Tap below to add your first medication.",
            style = typography.bodyMedium,
            color = MedTrackerTheme.colors.secondaryLabel,
            modifier = Modifier.padding(horizontal = 16.dp),
            textAlign = TextAlign.Center
        )

        if (hasSearchQuery) {
            Surface(
                onClick = onClearSearch,
                shape = RoundedCornerShape(12.dp),
                color = MedTrackerTheme.colors.primaryTinted,
                contentColor = MedTrackerTheme.colors.primary500
            ) {
                Text(
                    text = "Clear Search",
                    style = typography.labelLargeEmphasized,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }
        } else {
            Surface(
                onClick = onAddClick,
                shape = RoundedCornerShape(12.dp),
                color = MedTrackerTheme.colors.primary500,
                contentColor = Color.White
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Add First Medication",
                        style = typography.labelLargeEmphasized,
                        color = Color.White
                    )
                }
            }
        }
    }
}

private data class FormBadgeConfig(
    val icon: ImageVector,
    val bgColor: Color,
    val iconColor: Color,
    val label: String
)

@Composable
private fun getFormBadgeConfig(form: MedicationForm?): FormBadgeConfig {
    return when (form) {
        MedicationForm.TABLET -> FormBadgeConfig(
            icon = Icons.Default.Medication,
            bgColor = Color(0xFFE0F2FE),
            iconColor = Color(0xFF0284C7),
            label = "Tablet"
        )

        MedicationForm.CAPSULE -> FormBadgeConfig(
            icon = Icons.Default.Medication,
            bgColor = Color(0xFFF3E8FF),
            iconColor = Color(0xFF9333EA),
            label = "Capsule"
        )

        MedicationForm.LIQUID -> FormBadgeConfig(
            icon = Icons.Default.WaterDrop,
            bgColor = Color(0xFFE0E7FF),
            iconColor = Color(0xFF4F46E5),
            label = "Liquid"
        )

        MedicationForm.INJECTION -> FormBadgeConfig(
            icon = Icons.Default.Vaccines,
            bgColor = Color(0xFFDCFCE7),
            iconColor = Color(0xFF16A34A),
            label = "Injection"
        )

        MedicationForm.POWDER -> FormBadgeConfig(
            icon = Icons.Default.Science,
            bgColor = Color(0xFFFEF3C7),
            iconColor = Color(0xFFD97706),
            label = "Powder"
        )

        else -> FormBadgeConfig(
            icon = Icons.Outlined.Medication,
            bgColor = Color(0xFFF3F4F6),
            iconColor = Color(0xFF4B5563),
            label = "Medicine"
        )
    }
}

private fun formatIntakeTime(time: LocalTime?): String {
    if (time == null) return "Time not set"
    return try {
        time.format(DateTimeFormatter.ofPattern("hh:mm a"))
    } catch (_: Exception) {
        time.toString()
    }
}

private fun formatDaysOfWeek(days: List<String>?): String {
    if (days.isNullOrEmpty()) return "As needed"
    if (days.size >= 7 || days.any { it.equals("every day", ignoreCase = true) }) {
        return "Every day"
    }
    return days.joinToString(", ") { day ->
        day.take(3).lowercase()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}

@Preview(backgroundColor = 0xFFF2F2F7, showBackground = true)
@Composable
fun FlyElevatedCardMedsListPreview() {
    MedTrackerTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MedicationCardItem(
                medication = Medication(
                    id = "1",
                    name = "Amoxicillin",
                    dosage = "500 mg",
                    form = MedicationForm.CAPSULE,
                    startDate = null,
                    endDate = null,
                    daysOfWeek = listOf("Monday", "Wednesday", "Friday"),
                    intakeTime = LocalTime.of(9, 0)
                ),
                onViewMed = {},
                onEditClick = {},
                onRemoveMedClick = {}
            )

            MedicationCardItem(
                medication = Medication(
                    id = "2",
                    name = "Vitamin D3",
                    dosage = "2000 IU",
                    form = MedicationForm.TABLET,
                    startDate = null,
                    endDate = null,
                    daysOfWeek = listOf("Every day"),
                    intakeTime = LocalTime.of(14, 30)
                ),
                onViewMed = {},
                onEditClick = {},
                onRemoveMedClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MedicationsScreenPreview() {
    MedTrackerTheme {
        MedicationsScreenContent(
            medications = listOf(
                Medication(
                    id = "1",
                    name = "Amoxicillin",
                    dosage = "500 mg",
                    form = MedicationForm.CAPSULE,
                    startDate = null,
                    endDate = null,
                    daysOfWeek = listOf("Monday", "Wednesday", "Friday"),
                    intakeTime = LocalTime.of(9, 0)
                ),
                Medication(
                    id = "2",
                    name = "Vitamin D3",
                    dosage = "2000 IU",
                    form = MedicationForm.TABLET,
                    startDate = null,
                    endDate = null,
                    daysOfWeek = listOf("Every day"),
                    intakeTime = LocalTime.of(14, 30)
                )
            )
        )
    }
}
