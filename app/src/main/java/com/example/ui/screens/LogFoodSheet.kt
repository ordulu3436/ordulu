package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CalorieOrange
import com.example.ui.theme.CarbsAmber
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.FatPink
import com.example.ui.theme.ProteinBlue
import com.example.ui.viewmodel.LogSheetState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogFoodSheet(
    state: LogSheetState,
    onDismiss: () -> Unit,
    onQueryChange: (String) -> Unit,
    onMealTypeChange: (String) -> Unit,
    onHourChange: (Int) -> Unit,
    onParseRequested: () -> Unit,
    onFieldChange: (name: String?, calories: String?, protein: String?, carbs: String?, fat: String?, fiber: String?, portion: String?) -> Unit,
    onCommit: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("log_food_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Başlık
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
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Yapay Zeka Ayrıştırıcı",
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Metinle Besin & Kalori Hesapla",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Öğününüzü gramaj veya porsiyonuyla serbestçe yazın",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                IconButton(onClick = onDismiss, modifier = Modifier.testTag("close_sheet_button")) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Kapat")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Öğün Seçim Çipleri
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    "BREAKFAST" to "Kahvaltı",
                    "LUNCH" to "Öğle Yemeği",
                    "DINNER" to "Akşam Yemeği",
                    "SNACK" to "Ara Öğün"
                ).forEach { (mealKey, mealTitle) ->
                    val isSelected = state.mealType.equals(mealKey, ignoreCase = true)
                    FilterChip(
                        selected = isSelected,
                        onClick = { onMealTypeChange(mealKey) },
                        label = { Text(mealTitle) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = EmeraldPrimary,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.testTag("chip_meal_${mealKey.lowercase()}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Doğal Metin Giriş Alanı
            OutlinedTextField(
                value = state.inputText,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("food_input_text_field"),
                placeholder = { Text("Örn: 200g tavuk göğsü ve 1 porsiyon pirinç pilavı") },
                singleLine = false,
                maxLines = 3,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { onParseRequested() }),
                trailingIcon = {
                    if (state.inputText.isNotBlank()) {
                        IconButton(onClick = onParseRequested, modifier = Modifier.testTag("submit_parse_button")) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "Şimdi Hesapla",
                                tint = EmeraldPrimary
                            )
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmeraldPrimary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Hızlı Örnek Çipleri
            Text(
                text = "Hızlı Örnekler:",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val samples = listOf(
                    "200g tavuk göğsü",
                    "2 haşlanmış yumurta ve 1 dilim ekmek",
                    "1 ölçek whey protein ve 250ml süt",
                    "1 orta boy muz ve 1 kaşık fıstık ezmesi",
                    "1 orta boy elma",
                    "150g somon balığı ve brokoli"
                )
                samples.forEach { sample ->
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.clickable {
                            onQueryChange(sample)
                            onParseRequested()
                        }
                    ) {
                        Text(
                            text = sample,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Hesapla Butonu
            Button(
                onClick = onParseRequested,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("parse_calories_button"),
                enabled = state.inputText.isNotBlank() && !state.isParsing,
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (state.isParsing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Besin değerleri hesaplanıyor...")
                } else {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Besin Değerlerini Hesapla")
                }
            }

            // Ayrıştırma ve Hesaplama Sonuçları
            AnimatedVisibility(
                visible = state.parsedResult != null || state.calories.isNotBlank(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    // Motor Rozeti
                    val engineName = state.parsedResult?.engineUsed ?: "caloree Akıllı Kural Motoru"
                    val confidence = state.parsedResult?.confidence ?: "Hesaplandı"
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(EmeraldPrimary.copy(alpha = 0.1f))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⚡ Motor: $engineName",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = EmeraldPrimary
                        )
                        Text(
                            text = confidence,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Düzenlenebilir Başlık ve Porsiyon
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = state.foodName,
                            onValueChange = { onFieldChange(it, null, null, null, null, null, null) },
                            label = { Text("Besin Adı") },
                            modifier = Modifier
                                .weight(1.5f)
                                .testTag("food_name_field"),
                            shape = RoundedCornerShape(10.dp)
                        )
                        OutlinedTextField(
                            value = state.portionDesc,
                            onValueChange = { onFieldChange(null, null, null, null, null, null, it) },
                            label = { Text("Porsiyon / Miktar") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("food_portion_field"),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Makro Değer Kartları (Düzenlenebilir)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        MacroInputPill(
                            label = "Kalori",
                            value = state.calories,
                            unit = "kcal",
                            color = CalorieOrange,
                            modifier = Modifier.weight(1.1f),
                            onValueChange = { onFieldChange(null, it, null, null, null, null, null) }
                        )
                        MacroInputPill(
                            label = "Protein",
                            value = state.protein,
                            unit = "g",
                            color = ProteinBlue,
                            modifier = Modifier.weight(1f),
                            onValueChange = { onFieldChange(null, null, it, null, null, null, null) }
                        )
                        MacroInputPill(
                            label = "Karb",
                            value = state.carbs,
                            unit = "g",
                            color = CarbsAmber,
                            modifier = Modifier.weight(1f),
                            onValueChange = { onFieldChange(null, null, null, it, null, null, null) }
                        )
                        MacroInputPill(
                            label = "Yağ",
                            value = state.fat,
                            unit = "g",
                            color = FatPink,
                            modifier = Modifier.weight(1f),
                            onValueChange = { onFieldChange(null, null, null, null, it, null, null) }
                        )
                    }

                    // Bileşik Besin Malzeme Detayı
                    if ((state.parsedResult?.items?.size ?: 0) > 1) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Ayrıştırılan Malzemeler:",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        state.parsedResult?.items?.forEach { subItem ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = subItem.foodName,
                                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                                        )
                                        Text(
                                            text = subItem.portionDescription,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Text(
                                        text = "${subItem.calories} kcal • ${subItem.proteinGrams}g P • ${subItem.carbsGrams}g K • ${subItem.fatGrams}g Y",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                        color = EmeraldPrimary
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Kayıt Saati Ayarlayıcı
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Schedule, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Kayıt Saati:",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.horizontalScroll(rememberScrollState())
                        ) {
                            val keyHours = listOf(8, 10, 12, 13, 15, 18, 20)
                            keyHours.forEach { h ->
                                val isHSelected = state.hourOfDay == h
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isHSelected) EmeraldPrimary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    modifier = Modifier.clickable { onHourChange(h) }
                                ) {
                                    Text(
                                        text = String.format("%02d:00", h),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = if (isHSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                        ),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Günlüğe Kaydet Butonu
                    Button(
                        onClick = onCommit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("commit_food_entry_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Öğünü Günlüğe Kaydet",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun MacroInputPill(
    label: String,
    value: String,
    unit: String,
    color: Color,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    Card(
        modifier = modifier.border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.08f)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = color)
            Spacer(modifier = Modifier.height(2.dp))
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("input_${label.lowercase()}"),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = color,
                    unfocusedBorderColor = Color.Transparent
                )
            )
            Text(text = unit, style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp), color = color)
        }
    }
}
