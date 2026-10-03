package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DataObject
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.ViewCarousel
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CalorieOrange
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ProteinBlue

@Composable
fun ArchitectureScreen(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("architecture_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Column {
                Text(
                    text = "System Architecture & Flows",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Comprehensive specification of Screen Flows, Database Schema, and Input-Parsing Logic Pipeline",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Section 1: Screen Flows
        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    SectionHeader(
                        icon = Icons.Default.ViewCarousel,
                        title = "1. Application Screen Flows",
                        subtitle = "State-driven navigation and user journeys"
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    FlowStepCard(
                        step = "FLOW A",
                        title = "Daily Progress & Log Flow",
                        description = "Launch App ➔ Load Selected Date State ➔ Render Hero Calorie Gauge & Macro Bars ➔ Render Hourly Timeline ➔ Group Foods by Meal Category.",
                        badgeColor = EmeraldPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    FlowStepCard(
                        step = "FLOW B",
                        title = "Text Input & Calorie Calculation Flow",
                        description = "Tap '+ Log Food' / Meal ➔ Open Bottom Sheet Modal ➔ User types natural text (e.g. '200g chicken breast') ➔ Dual Engine Parsing (Gemini AI or Smart NLP Regex) ➔ Real-time Macro Extraction Preview ➔ Editable Review ➔ Commit to Room DB.",
                        badgeColor = CalorieOrange
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    FlowStepCard(
                        step = "FLOW C",
                        title = "Hourly Analytics & Eating Window Flow",
                        description = "Navigate to 'Hourly Logs' tab ➔ Aggregate 24-hr entries by hour ➔ Calculate Fasting / Eating Window ➔ Display chronological timestamped entries with macro tags.",
                        badgeColor = ProteinBlue
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    FlowStepCard(
                        step = "FLOW D",
                        title = "User Profile & TDEE Setup Flow",
                        description = "Navigate to 'Profile' ➔ Enter Biometrics (Weight, Height, Age, Gender) ➔ Select Activity Level & Goal ➔ Compute Mifflin-St Jeor BMR & TDEE ➔ Pick Macro Preset ➔ Persist to DB.",
                        badgeColor = Color(0xFF8B5CF6)
                    )
                }
            }
        }

        // Section 2: Input-Parsing Logic Flow
        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    SectionHeader(
                        icon = Icons.Default.AutoAwesome,
                        title = "2. Input-Parsing Logic Flow",
                        subtitle = "End-to-end natural language nutrition extraction pipeline"
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    PipelineStage(
                        number = "01",
                        title = "Text Normalization & Delimiter Splitting",
                        detail = "Splits compound phrases by 'and', 'with', '+', ',', 'plus'. Replaces word numbers ('two' ➔ 2, 'half' ➔ 0.5, 'a/an' ➔ 1)."
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    PipelineStage(
                        number = "02",
                        title = "AI Engine Branch (Gemini 3.5 Flash)",
                        detail = "If API key is valid, sends structured prompt to generativelanguage.googleapis.com REST endpoint. Receives strict JSON candidate with items array, calories, and macros."
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    PipelineStage(
                        number = "03",
                        title = "Smart NLP Rule Engine (Offline Fallback)",
                        detail = "Regex extracts quantities and units (g, kg, oz, cup, tbsp, slice, piece, scoop, bowl). Fuzzy & keyword token matcher scans 150+ food definitions."
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    PipelineStage(
                        number = "04",
                        title = "Unit-to-Gram Weight Conversion",
                        detail = "Maps unit weights: 1 egg = 50g, 1 slice bread = 32g, 1 cup cooked rice = 158g, 1 scoop whey = 30g, 1 tbsp peanut butter = 16g."
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    PipelineStage(
                        number = "05",
                        title = "Macro & Calorie Computation",
                        detail = "Calculates Calories = (calPer100g * grams / 100), Protein = (protPer100g * grams / 100), etc. Aggregates composite meal totals."
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    PipelineStage(
                        number = "06",
                        title = "Timestamp & Hourly Tagging",
                        detail = "Captures exact Unix Epoch timestamp, extracts hourOfDay (0..23) and local dateString ('yyyy-MM-dd') for instant Room indexing."
                    )
                }
            }
        }

        // Section 3: Database Schema
        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    SectionHeader(
                        icon = Icons.Default.Storage,
                        title = "3. Room Database Schema",
                        subtitle = "SQLite entities, relationships, and index strategy"
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    SchemaTableCard(
                        tableName = "food_entries",
                        description = "Stores every logged food item, input text, parsed nutrients, and timestamps",
                        columns = listOf(
                            "id: Long (PK, autoGenerate)" to "Unique primary key",
                            "rawInputText: String" to "Original text (e.g. '200g chicken breast')",
                            "foodName: String" to "Normalized name (e.g. 'Chicken Breast')",
                            "portionDesc: String" to "Display portion (e.g. '200g')",
                            "calories: Int" to "Total calculated kilocalories",
                            "proteinGrams: Float" to "Protein in grams",
                            "carbsGrams: Float" to "Carbohydrates in grams",
                            "fatGrams: Float" to "Fats in grams",
                            "fiberGrams: Float" to "Dietary fiber in grams",
                            "mealType: String" to "BREAKFAST | LUNCH | DINNER | SNACK",
                            "timestampMillis: Long" to "Exact Unix epoch timestamp",
                            "dateString: String (Indexed)" to "ISO 'yyyy-MM-dd' for rapid date filtering",
                            "hourOfDay: Int (Indexed)" to "0..23 for hourly intake aggregations",
                            "parsedByAi: Boolean" to "Flag indicating Gemini AI vs Rule Engine"
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    SchemaTableCard(
                        tableName = "user_profile",
                        description = "Stores user biometrics, calculated BMR, TDEE, and daily macro targets",
                        columns = listOf(
                            "id: Int (PK = 1)" to "Single-row singleton user record",
                            "name: String" to "User's display name",
                            "gender: String" to "Male | Female | Other",
                            "age: Int" to "Age in years",
                            "heightCm: Float" to "Height in centimeters",
                            "weightKg: Float" to "Weight in kilograms",
                            "activityLevel: String" to "SEDENTARY..VERY_ACTIVE",
                            "goalType: String" to "LOSE_FAST..GAIN_FAST",
                            "dailyCalorieTarget: Int" to "Calculated TDEE adjusted budget",
                            "proteinGramsTarget: Int" to "Target protein grams",
                            "carbsGramsTarget: Int" to "Target carbs grams",
                            "fatGramsTarget: Int" to "Target fat grams",
                            "waterMlTarget: Int" to "Daily hydration goal (ml)"
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    SchemaTableCard(
                        tableName = "water_entries",
                        description = "Stores timestamped hydration logs",
                        columns = listOf(
                            "id: Long (PK, autoGenerate)" to "Primary key",
                            "amountMl: Int" to "Milliliters consumed (e.g. 250, 500)",
                            "timestampMillis: Long" to "Time logged",
                            "dateString: String (Indexed)" to "ISO date string"
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(
    icon: ImageVector,
    title: String,
    subtitle: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(EmeraldPrimary.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun FlowStepCard(
    step: String,
    title: String,
    description: String,
    badgeColor: Color
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = badgeColor
                ) {
                    Text(
                        text = step,
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.White, fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun PipelineStage(
    number: String,
    title: String,
    detail: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(EmeraldPrimary.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = EmeraldPrimary
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = detail,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SchemaTableCard(
    tableName: String,
    description: String,
    columns: List<Pair<String, String>>
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.DataObject, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Table: $tableName",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    ),
                    color = EmeraldPrimary
                )
            }
            Text(
                text = description,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(8.dp)
            ) {
                columns.forEach { (col, desc) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = col,
                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace, fontSize = 10.sp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = desc,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
