package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.FoodEntryEntity
import com.example.data.repository.NutritionRepository
import com.example.ui.components.CalorieGauge
import com.example.ui.components.HourlyBarChart
import com.example.ui.components.MacroProgressBar
import com.example.ui.theme.CarbsAmber
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.FatPink
import com.example.ui.theme.FiberGreen
import com.example.ui.theme.ProteinBlue
import com.example.ui.theme.WaterBlue
import com.example.ui.viewmodel.DashboardUiState
import kotlin.math.roundToInt

@Composable
fun DashboardScreen(
    state: DashboardUiState,
    onNavigateDays: (Int) -> Unit,
    onResetToday: () -> Unit,
    onOpenLogFood: (String?) -> Unit,
    onDeleteEntry: (FoodEntryEntity) -> Unit,
    onLogWater: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("dashboard_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Tarih Gezintisi
        item {
            DateNavigatorHeader(
                displayDate = state.displayDate,
                onPreviousDay = { onNavigateDays(-1) },
                onNextDay = { onNavigateDays(1) },
                onResetToday = onResetToday
            )
        }

        // 2. Ana Günlük Kalori İlerleme Kartı
        item {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hero_calorie_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Günlük Kalori Bütçesi",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            val goalLabel = when (state.profile.goalType) {
                                "LOSE_FAST" -> "Hızlı Yağ Yakımı Hedefi"
                                "LOSE_MILD" -> "Hafif Kilo Verme Hedefi"
                                "MAINTAIN" -> "Kilo Koruma Hedefi"
                                "GAIN_MILD" -> "Temiz Hacim Hedefi"
                                "GAIN_FAST" -> "Kilo Kazanımı Hedefi"
                                else -> "Kilo Koruma Hedefi"
                            }
                            Text(
                                text = goalLabel,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = EmeraldPrimary.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = "${state.entries.size} besin girildi",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = EmeraldPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    CalorieGauge(
                        consumedCalories = state.totalCalories,
                        targetCalories = state.targetCalories
                    )
                }
            }
        }

        // 3. Makro Besin Dağılım Kartı
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("macro_breakdown_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Makro Besinler",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        val totalMacroCals = (state.totalProtein * 4 + state.totalCarbs * 4 + state.totalFat * 9).coerceAtLeast(1f)
                        val pPct = ((state.totalProtein * 4 / totalMacroCals) * 100).roundToInt()
                        val cPct = ((state.totalCarbs * 4 / totalMacroCals) * 100).roundToInt()
                        val fPct = ((state.totalFat * 9 / totalMacroCals) * 100).roundToInt()

                        Text(
                            text = "Oran: %$pPct Protein / %$cPct Karb / %$fPct Yağ",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    MacroProgressBar(
                        name = "Protein",
                        consumedGrams = state.totalProtein,
                        targetGrams = state.profile.proteinGramsTarget,
                        color = ProteinBlue
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    MacroProgressBar(
                        name = "Karbonhidrat",
                        consumedGrams = state.totalCarbs,
                        targetGrams = state.profile.carbsGramsTarget,
                        color = CarbsAmber
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    MacroProgressBar(
                        name = "Yağ",
                        consumedGrams = state.totalFat,
                        targetGrams = state.profile.fatGramsTarget,
                        color = FatPink
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    MacroProgressBar(
                        name = "Diyet Lifi",
                        consumedGrams = state.totalFiber,
                        targetGrams = 30,
                        color = FiberGreen
                    )
                }
            }
        }

        // 4. Saatlik Dağılım Kartı
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hourly_timeline_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                HourlyBarChart(
                    hourlyCalories = state.hourlyCalories,
                    modifier = Modifier.padding(18.dp)
                )
            }
        }

        // 5. Su Takibi Kartı
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hydration_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(WaterBlue.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalDrink,
                                    contentDescription = "Su",
                                    tint = WaterBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Günlük Su Takibi",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "${state.totalWaterMl} / ${state.profile.waterMlTarget} ml",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            OutlinedButton(
                                onClick = { onLogWater(250) },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Text("+250ml", style = MaterialTheme.typography.labelSmall)
                            }
                            OutlinedButton(
                                onClick = { onLogWater(500) },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Text("+500ml", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    val waterProgress = (state.totalWaterMl.toFloat() / state.profile.waterMlTarget.toFloat()).coerceIn(0f, 1f)
                    LinearProgressIndicator(
                        progress = { waterProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = WaterBlue,
                        trackColor = WaterBlue.copy(alpha = 0.15f)
                    )
                }
            }
        }

        // 6. Öğünler Başlığı
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Öğünler & Zaman Damgaları",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Button(
                    onClick = { onOpenLogFood(null) },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("quick_log_food_button")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Besin Ekle", style = MaterialTheme.typography.labelMedium)
                }
            }
        }

        // Öğün Kartları
        item {
            MealGroupCard(
                mealName = "Kahvaltı",
                entries = state.breakfastEntries,
                onAddClick = { onOpenLogFood("BREAKFAST") },
                onDeleteEntry = onDeleteEntry
            )
        }

        item {
            MealGroupCard(
                mealName = "Öğle Yemeği",
                entries = state.lunchEntries,
                onAddClick = { onOpenLogFood("LUNCH") },
                onDeleteEntry = onDeleteEntry
            )
        }

        item {
            MealGroupCard(
                mealName = "Akşam Yemeği",
                entries = state.dinnerEntries,
                onAddClick = { onOpenLogFood("DINNER") },
                onDeleteEntry = onDeleteEntry
            )
        }

        item {
            MealGroupCard(
                mealName = "Ara Öğünler",
                entries = state.snackEntries,
                onAddClick = { onOpenLogFood("SNACK") },
                onDeleteEntry = onDeleteEntry
            )
        }
    }
}

@Composable
private fun DateNavigatorHeader(
    displayDate: String,
    onPreviousDay: () -> Unit,
    onNextDay: () -> Unit,
    onResetToday: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("date_navigator_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPreviousDay, modifier = Modifier.testTag("prev_day_button")) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Önceki Gün"
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onResetToday() }
            ) {
                Icon(
                    imageVector = Icons.Default.Today,
                    contentDescription = null,
                    tint = EmeraldPrimary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = displayDate,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            IconButton(onClick = onNextDay, modifier = Modifier.testTag("next_day_button")) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Sonraki Gün"
                )
            }
        }
    }
}

@Composable
private fun MealGroupCard(
    mealName: String,
    entries: List<FoodEntryEntity>,
    onAddClick: () -> Unit,
    onDeleteEntry: (FoodEntryEntity) -> Unit
) {
    var expanded by remember { mutableStateOf(true) }
    val totalCals = entries.sumOf { it.calories }
    val totalP = entries.sumOf { it.proteinGrams.toDouble() }.toFloat()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("meal_card_${mealName.lowercase()}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            // Başlık Satırı
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { expanded = !expanded }
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(EmeraldPrimary.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Restaurant,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = mealName,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "$totalCals kcal • ${totalP.roundToInt()}g protein",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onAddClick, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Besin Ekle", tint = EmeraldPrimary)
                    }
                    IconButton(onClick = { expanded = !expanded }, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (expanded) "Daralt" else "Genişlet"
                        )
                    }
                }
            }

            // Besin Listesi
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    if (entries.isEmpty()) {
                        Text(
                            text = "$mealName için henüz besin kaydedilmedi.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        entries.forEach { entry ->
                            FoodItemRow(
                                entry = entry,
                                onDelete = { onDeleteEntry(entry) }
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FoodItemRow(
    entry: FoodEntryEntity,
    onDelete: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = entry.foodName,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (entry.parsedByAi) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = EmeraldPrimary.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "YZ",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold),
                                color = EmeraldPrimary,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${entry.portionDesc} • ${NutritionRepository.formatTimestampTime(entry.timestampMillis)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${entry.proteinGrams}g P • ${entry.carbsGrams}g K • ${entry.fatGrams}g Y",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                    color = EmeraldPrimary
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${entry.calories} kcal",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Sil",
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
