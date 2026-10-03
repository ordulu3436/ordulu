package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserProfileEntity
import com.example.ui.theme.CarbsAmber
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.FatPink
import com.example.ui.theme.ProteinBlue
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun ProfileSetupScreen(
    currentProfile: UserProfileEntity,
    onSaveProfile: (UserProfileEntity) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    var name by remember(currentProfile) { mutableStateOf(currentProfile.name) }
    var gender by remember(currentProfile) { mutableStateOf(currentProfile.gender) }
    var ageText by remember(currentProfile) { mutableStateOf(currentProfile.age.toString()) }
    var heightText by remember(currentProfile) { mutableStateOf(currentProfile.heightCm.toInt().toString()) }
    var weightText by remember(currentProfile) { mutableStateOf(currentProfile.weightKg.toInt().toString()) }
    var activityLevel by remember(currentProfile) { mutableStateOf(currentProfile.activityLevel) }
    var goalType by remember(currentProfile) { mutableStateOf(currentProfile.goalType) }

    var calorieTargetText by remember(currentProfile) { mutableStateOf(currentProfile.dailyCalorieTarget.toString()) }
    var proteinTargetText by remember(currentProfile) { mutableStateOf(currentProfile.proteinGramsTarget.toString()) }
    var carbsTargetText by remember(currentProfile) { mutableStateOf(currentProfile.carbsGramsTarget.toString()) }
    var fatTargetText by remember(currentProfile) { mutableStateOf(currentProfile.fatGramsTarget.toString()) }
    var waterTargetText by remember(currentProfile) { mutableStateOf(currentProfile.waterMlTarget.toString()) }

    val coroutineScope = rememberCoroutineScope()

    // Dynamic Live Preview Model
    val age = ageText.toIntOrNull() ?: 28
    val height = heightText.toFloatOrNull() ?: 175f
    val weight = weightText.toFloatOrNull() ?: 72f

    val previewProfile = UserProfileEntity(
        name = name,
        gender = gender,
        age = age,
        heightCm = height,
        weightKg = weight,
        activityLevel = activityLevel,
        goalType = goalType
    )

    val liveBmr = previewProfile.calculateBmr()
    val liveTdee = previewProfile.calculateTdee()
    val liveRecommendedCals = previewProfile.calculateRecommendedCalories()

    fun applyPreset(preset: String) {
        val cals = calorieTargetText.toIntOrNull() ?: liveRecommendedCals
        when (preset) {
            "Balanced" -> {
                proteinTargetText = ((cals * 0.30f) / 4f).roundToInt().toString()
                carbsTargetText = ((cals * 0.45f) / 4f).roundToInt().toString()
                fatTargetText = ((cals * 0.25f) / 9f).roundToInt().toString()
            }
            "High Protein" -> {
                proteinTargetText = ((cals * 0.40f) / 4f).roundToInt().toString()
                carbsTargetText = ((cals * 0.35f) / 4f).roundToInt().toString()
                fatTargetText = ((cals * 0.25f) / 9f).roundToInt().toString()
            }
            "Low Carb" -> {
                proteinTargetText = ((cals * 0.35f) / 4f).roundToInt().toString()
                carbsTargetText = ((cals * 0.25f) / 4f).roundToInt().toString()
                fatTargetText = ((cals * 0.40f) / 9f).roundToInt().toString()
            }
            "Keto" -> {
                proteinTargetText = ((cals * 0.25f) / 4f).roundToInt().toString()
                carbsTargetText = ((cals * 0.05f) / 4f).roundToInt().toString()
                fatTargetText = ((cals * 0.70f) / 9f).roundToInt().toString()
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("profile_setup_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Column {
                Text(
                    text = "User Profile & Nutrition Goals",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Configure your biometric stats, TDEE energy targets, and macro split",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Biometrics Card
        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(EmeraldPrimary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Personal Biometrics",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Display Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_profile_name"),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Gender Selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Male", "Female", "Other").forEach { g ->
                            FilterChip(
                                selected = gender.equals(g, ignoreCase = true),
                                onClick = { gender = g },
                                label = { Text(g) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = EmeraldPrimary,
                                    selectedLabelColor = Color.White
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Age, Height, Weight
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = ageText,
                            onValueChange = { ageText = it },
                            label = { Text("Age") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("input_profile_age"),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = heightText,
                            onValueChange = { heightText = it },
                            label = { Text("Height (cm)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("input_profile_height"),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = weightText,
                            onValueChange = { weightText = it },
                            label = { Text("Weight (kg)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("input_profile_weight"),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }
        }

        // Activity Level & Goal
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(EmeraldPrimary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.FitnessCenter, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Activity Level & Primary Goal",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "Physical Activity:", style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            "SEDENTARY" to "Sedentary (Desk job)",
                            "LIGHT" to "Light (1-3 days/wk)",
                            "MODERATE" to "Moderate (3-5 days/wk)",
                            "ACTIVE" to "Active (6-7 days/wk)",
                            "VERY_ACTIVE" to "Extra Active"
                        ).forEach { (lvl, title) ->
                            FilterChip(
                                selected = activityLevel.equals(lvl, ignoreCase = true),
                                onClick = {
                                    activityLevel = lvl
                                    calorieTargetText = previewProfile.copy(activityLevel = lvl).calculateRecommendedCalories().toString()
                                },
                                label = { Text(title) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = EmeraldPrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "Target Goal:", style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            "LOSE_FAST" to "Fat Loss (-500 kcal)",
                            "LOSE_MILD" to "Mild Loss (-250 kcal)",
                            "MAINTAIN" to "Maintain Weight",
                            "GAIN_MILD" to "Lean Bulk (+250 kcal)",
                            "GAIN_FAST" to "Muscle Gain (+500 kcal)"
                        ).forEach { (gType, title) ->
                            FilterChip(
                                selected = goalType.equals(gType, ignoreCase = true),
                                onClick = {
                                    goalType = gType
                                    calorieTargetText = previewProfile.copy(goalType = gType).calculateRecommendedCalories().toString()
                                },
                                label = { Text(title) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = EmeraldPrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }
        }

        // Live Calculated TDEE & BMR Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Calculate, contentDescription = null, tint = EmeraldPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Mifflin-St Jeor Energy Science",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = EmeraldPrimary.copy(alpha = 0.15f),
                            modifier = Modifier.clickable {
                                calorieTargetText = liveRecommendedCals.toString()
                                applyPreset("Balanced")
                            }
                        ) {
                            Text(
                                text = "Use $liveRecommendedCals kcal",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = EmeraldPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        MetricItem(title = "BMR (Basal)", value = "$liveBmr kcal", subtitle = "At complete rest")
                        MetricItem(title = "TDEE (Maintenance)", value = "$liveTdee kcal", subtitle = "Burned with activity")
                        MetricItem(title = "Recommended", value = "$liveRecommendedCals kcal", subtitle = "To hit $goalType")
                    }
                }
            }
        }

        // Macro Splits & Targets Customization
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Macro Target Targets (Grams)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Pick a preset ratio or fine-tune daily targets manually",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Preset Buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Balanced", "High Protein", "Low Carb", "Keto").forEach { preset ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                                modifier = Modifier.clickable { applyPreset(preset) }
                            ) {
                                Text(
                                    text = preset,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = calorieTargetText,
                        onValueChange = { calorieTargetText = it },
                        label = { Text("Daily Calorie Target (kcal)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_target_calories"),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = proteinTargetText,
                            onValueChange = { proteinTargetText = it },
                            label = { Text("Protein (g)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("input_target_protein"),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = carbsTargetText,
                            onValueChange = { carbsTargetText = it },
                            label = { Text("Carbs (g)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("input_target_carbs"),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = fatTargetText,
                            onValueChange = { fatTargetText = it },
                            label = { Text("Fat (g)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("input_target_fat"),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = waterTargetText,
                        onValueChange = { waterTargetText = it },
                        label = { Text("Daily Water Goal (ml)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_target_water"),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }

        // Save Button
        item {
            Button(
                onClick = {
                    val updated = currentProfile.copy(
                        name = name.ifBlank { "Alex" },
                        gender = gender,
                        age = age,
                        heightCm = height,
                        weightKg = weight,
                        activityLevel = activityLevel,
                        goalType = goalType,
                        dailyCalorieTarget = calorieTargetText.toIntOrNull() ?: liveRecommendedCals,
                        proteinGramsTarget = proteinTargetText.toIntOrNull() ?: 160,
                        carbsGramsTarget = carbsTargetText.toIntOrNull() ?: 230,
                        fatGramsTarget = fatTargetText.toIntOrNull() ?: 65,
                        waterMlTarget = waterTargetText.toIntOrNull() ?: 2500,
                        isSetupComplete = true
                    )
                    onSaveProfile(updated)
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Profile & nutrition targets updated successfully!")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("save_profile_button"),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(imageVector = Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Save Profile & Update Targets",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
private fun MetricItem(title: String, value: String, subtitle: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(text = subtitle, style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp), color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
