package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.FoodEntryEntity
import com.example.data.repository.NutritionRepository
import com.example.ui.components.HourlyBarChart
import com.example.ui.theme.CalorieOrange
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ProteinBlue
import com.example.ui.viewmodel.DashboardUiState
import com.example.ui.viewmodel.DayCalorieSummary
import kotlin.math.max

@Composable
fun HourlyTimelineScreen(
    state: DashboardUiState,
    weeklySummaries: List<DayCalorieSummary>,
    onDeleteEntry: (FoodEntryEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val sortedEntries = state.entries.sortedBy { it.timestampMillis }
    val firstMeal = sortedEntries.firstOrNull()
    val lastMeal = sortedEntries.lastOrNull()

    val eatingWindowHours = if (firstMeal != null && lastMeal != null && firstMeal.id != lastMeal.id) {
        val diffMillis = lastMeal.timestampMillis - firstMeal.timestampMillis
        val hours = diffMillis / (1000 * 60 * 60)
        val mins = (diffMillis / (1000 * 60)) % 60
        "${hours} sa ${mins} dk"
    } else if (firstMeal != null) {
        "Tek bir öğün kaydedildi"
    } else {
        "Henüz öğün girilmedi"
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("hourly_timeline_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Başlık ve Açıklama
        item {
            Column {
                Text(
                    text = "Saatlik Dağılım & Kronolojik Günlük",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${state.displayDate} • Saatlik kalori tüketimi ve tam zaman damgalı besinler",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Beslenme Penceresi Kartı
        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldPrimary.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AvTimer,
                                    contentDescription = null,
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Günlük Beslenme Penceresi",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = eatingWindowHours,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = EmeraldPrimary
                                )
                            }
                        }

                        if (firstMeal != null && lastMeal != null) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ) {
                                Text(
                                    text = "${NutritionRepository.formatTimestampTime(firstMeal.timestampMillis)} - ${NutritionRepository.formatTimestampTime(lastMeal.timestampMillis)}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    HourlyBarChart(hourlyCalories = state.hourlyCalories)
                }
            }
        }

        // 7 Günlük Kalori Trendi Kartı
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Son 7 Günlük Kalori Trendi",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    val maxWeekCal = weeklySummaries.maxOfOrNull { max(it.calories, it.target) } ?: 2500

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        weeklySummaries.forEach { day ->
                            val heightRatio = (day.calories.toFloat() / maxWeekCal.toFloat()).coerceIn(0.05f, 1f)
                            val isMet = day.calories in (day.target - 200)..(day.target + 150)

                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                Text(
                                    text = if (day.calories > 0) "${day.calories}" else "0",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .width(18.dp)
                                        .height((heightRatio * 75).dp)
                                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                        .background(
                                            when {
                                                day.isToday -> EmeraldPrimary
                                                isMet -> Color(0xFF10B981)
                                                day.calories > day.target -> Color(0xFFEF4444)
                                                else -> CalorieOrange.copy(alpha = 0.7f)
                                            }
                                        )
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = day.dayLabel,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        fontWeight = if (day.isToday) FontWeight.Bold else FontWeight.Normal
                                    ),
                                    color = if (day.isToday) EmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // Kronolojik Akış Başlığı
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Saatlik Besin Günlüğü (${sortedEntries.size})",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Kronolojik Sıralı",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (sortedEntries.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(imageVector = Icons.Default.Schedule, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Bu tarih için kayıtlı öğün bulunmuyor.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(sortedEntries, key = { it.id }) { entry ->
                ChronologicalLogItemCard(entry = entry, onDelete = { onDeleteEntry(entry) })
            }
        }
    }
}

@Composable
private fun ChronologicalLogItemCard(
    entry: FoodEntryEntity,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Sol saat rozeti
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(end = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(EmeraldPrimary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = String.format("%02d:00", entry.hourOfDay),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp),
                        color = EmeraldPrimary
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                val trMeal = when (entry.mealType.uppercase()) {
                    "BREAKFAST" -> "Kahvaltı"
                    "LUNCH" -> "Öğle"
                    "DINNER" -> "Akşam"
                    "SNACK" -> "Ara Öğün"
                    else -> entry.mealType
                }
                Text(
                    text = trMeal,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Besin detayları
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.foodName,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "“${entry.rawInputText}”",
                    style = MaterialTheme.typography.bodySmall.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "${entry.proteinGrams}g P",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = ProteinBlue
                    )
                    Text(
                        text = "${entry.carbsGrams}g K",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = Color(0xFFF59E0B)
                    )
                    Text(
                        text = "${entry.fatGrams}g Y",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = Color(0xFFEC4899)
                    )
                }
            }

            // Kalori & Silme Butonu
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${entry.calories} kcal",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
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
