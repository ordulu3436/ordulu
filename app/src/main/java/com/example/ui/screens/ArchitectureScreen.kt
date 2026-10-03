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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DataObject
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.ViewCarousel
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
        // Başlık
        item {
            Column {
                Text(
                    text = "Sistem Mimarisi ve Akış Şemaları",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Ekran Akışları, Room Veritabanı Şeması ve Doğal Metin Ayrıştırma Hattı Spesifikasyonu",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Bölüm 1: Ekran Akışları
        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    SectionHeader(
                        icon = Icons.Default.ViewCarousel,
                        title = "1. Uygulama Ekran Akışları",
                        subtitle = "Kullanıcı deneyimi ve durum bazlı gezinme yolları"
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    FlowStepCard(
                        step = "AKIŞ A",
                        title = "Günlük İlerleme & Takip Akışı",
                        description = "Uygulama Açılışı ➔ Seçili Gün Durumunu Yükle ➔ Halka Kalori Göstergesi & Makro Çubuklarını Çiz ➔ 24 Saatlik Çubuk Grafiğini Çiz ➔ Öğünlere Göre Grupla.",
                        badgeColor = EmeraldPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    FlowStepCard(
                        step = "AKIŞ B",
                        title = "Metinle Besin & Kalori Hesaplama Akışı",
                        description = "'+ Besin Ekle' Butonuna Bas ➔ Modal Alt Panel Açılır ➔ Doğal Metin Yazılır (Örn: '200g tavuk göğsü') ➔ İkili Motor (Gemini YZ veya Akıllı Kural Motoru) ➔ Makrolar Ayrıştırılır ➔ Düzenlenebilir Önizleme ➔ Veritabanına Kaydet.",
                        badgeColor = CalorieOrange
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    FlowStepCard(
                        step = "AKIŞ C",
                        title = "Saatlik Analiz & Beslenme Penceresi Akışı",
                        description = "'Saatlik' Sekmesine Geç ➔ Günün 24 saatini kaloriye göre grupla ➔ İlk ve son öğün arasından Günlük Beslenme Penceresini hesapla ➔ Kronolojik zaman damgalı kartları listele.",
                        badgeColor = ProteinBlue
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    FlowStepCard(
                        step = "AKIŞ D",
                        title = "Profil & Biyometrik Hedef Belirleme",
                        description = "'Profil' Sekmesine Geç ➔ Kilo, Boy, Yaş, Cinsiyet Gir ➔ Aktivite ve Hedef Seç ➔ Mifflin-St Jeor ile BMR & TDEE hesapla ➔ Makro şablonu belirle ➔ Veritabanına kaydet.",
                        badgeColor = Color(0xFF8B5CF6)
                    )
                }
            }
        }

        // Bölüm 2: Metin Ayrıştırma Mantık Hattı
        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    SectionHeader(
                        icon = Icons.Default.AutoAwesome,
                        title = "2. Doğal Metin Ayrıştırma Mantık Hattı",
                        subtitle = "Metinden besin, miktar ve makro çıkarma adımları"
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    PipelineStage(
                        number = "01",
                        title = "Metin Normalizasyonu & Bağlaç Ayrımı",
                        detail = "Bileşik cümleler 've', 'ile', '+', ',', 'yanında' kelimelerine göre alt ögelere bölünür. Sayı kelimeleri ('iki' ➔ 2, 'yarım' ➔ 0.5, 'bir' ➔ 1) sayısala dönüştürülür."
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    PipelineStage(
                        number = "02",
                        title = "Yapay Zeka Motoru (Gemini 3.5 Flash)",
                        detail = "API anahtarı mevcutsa, doğrudan REST API ile yapılandırılmış JSON istemi gönderilir. Besin adı, porsiyon, kalori ve makrolar yapılandırılmış formatta alınır."
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    PipelineStage(
                        number = "03",
                        title = "Akıllı Kural Motoru (Çevrimdışı Yedek)",
                        detail = "Regex deseni miktar ve birimleri (gram, dilim, adet, ölçek, porsiyon, kase, yemek kaşığı vb.) yakalar. 150+ besinli yerel veri tabanında eşleşme aranır."
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    PipelineStage(
                        number = "04",
                        title = "Ölçü Biriminden Grama Dönüştürme",
                        detail = "Standart gram ağırlıkları uygulanır: 1 yumurta = 50g, 1 dilim ekmek = 32g, 1 porsiyon pirinç = 150g, 1 ölçek whey = 30g, 1 kaşık fıstık ezmesi = 16g."
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    PipelineStage(
                        number = "05",
                        title = "Makro ve Kalori Matematiksel Hesabı",
                        detail = "Kalori = (100g_kalori * gram / 100), Protein = (100g_protein * gram / 100) formülüyle tam değerler hesaplanır ve bileşik yemekler toplanır."
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    PipelineStage(
                        number = "06",
                        title = "Zaman Damgası ve Saat İndeksleme",
                        detail = "Tam Unix Epoch milisaniyesi, günün saati (0..23) ve ISO tarih ('yyyy-MM-dd') formatında etiketlenerek Room DB'ye kaydedilir."
                    )
                }
            }
        }

        // Bölüm 3: Veritabanı Şeması
        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    SectionHeader(
                        icon = Icons.Default.Storage,
                        title = "3. Room SQLite Veritabanı Şeması",
                        subtitle = "Tablolar, alanlar ve dizinleme (index) stratejisi"
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    SchemaTableCard(
                        tableName = "food_entries",
                        description = "Kaydedilen her besin girişini, makrolarını ve saatlik zaman damgalarını tutar",
                        columns = listOf(
                            "id: Long (PK, autoGenerate)" to "Benzersiz birincil anahtar",
                            "rawInputText: String" to "Kullanıcının yazdığı metin (Örn: '200g tavuk')",
                            "foodName: String" to "Ayrıştırılan besin adı",
                            "portionDesc: String" to "Porsiyon/miktar açıklaması (Örn: '200g')",
                            "calories: Int" to "Hesaplanan kilokalori (kcal)",
                            "proteinGrams: Float" to "Gram cinsinden protein",
                            "carbsGrams: Float" to "Gram cinsinden karbonhidrat",
                            "fatGrams: Float" to "Gram cinsinden yağ",
                            "fiberGrams: Float" to "Diyet lifi (g)",
                            "mealType: String" to "BREAKFAST | LUNCH | DINNER | SNACK",
                            "timestampMillis: Long" to "Tam milisaniye zaman damgası",
                            "dateString: String (İndeksli)" to "'yyyy-MM-dd' hızlı tarih filtreleme",
                            "hourOfDay: Int (İndeksli)" to "0..23 saatlik histogram gruplama",
                            "parsedByAi: Boolean" to "Yapay Zeka ayrıştırma bayrağı"
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    SchemaTableCard(
                        tableName = "user_profile",
                        description = "Biyometrik verileri, BMR, TDEE ve günlük makro hedeflerini tutar",
                        columns = listOf(
                            "id: Int (PK = 1)" to "Tekil kullanıcı kayıt satırı",
                            "name: String" to "Kullanıcı adı soyadı",
                            "gender: String" to "Erkek | Kadın | Diğer",
                            "age: Int" to "Yaş",
                            "heightCm: Float" to "Boy (cm)",
                            "weightKg: Float" to "Kilo (kg)",
                            "activityLevel: String" to "SEDENTARY..VERY_ACTIVE aktivite düzeyi",
                            "goalType: String" to "LOSE_FAST..GAIN_FAST kilo hedefi",
                            "dailyCalorieTarget: Int" to "Hesaplanan günlük kalori bütçesi",
                            "proteinGramsTarget: Int" to "Günlük hedef protein (g)",
                            "carbsGramsTarget: Int" to "Günlük hedef karbonhidrat (g)",
                            "fatGramsTarget: Int" to "Günlük hedef yağ (g)",
                            "waterMlTarget: Int" to "Günlük su tüketim hedefi (ml)"
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    SchemaTableCard(
                        tableName = "water_entries",
                        description = "Gün içinde tüketilen su miktarlarını ve zamanını tutar",
                        columns = listOf(
                            "id: Long (PK, autoGenerate)" to "Birincil anahtar",
                            "amountMl: Int" to "Tüketilen mililitre (Örn: 250, 500)",
                            "timestampMillis: Long" to "Kayıt zamanı",
                            "dateString: String (İndeksli)" to "İlgili günün tarihi"
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
                    text = "Tablo: $tableName",
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
