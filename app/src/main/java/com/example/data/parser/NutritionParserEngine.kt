package com.example.data.parser

import kotlin.math.roundToInt

data class ParsedFoodItem(
    val foodName: String,
    val portionDescription: String,
    val calories: Int,
    val proteinGrams: Float,
    val carbsGrams: Float,
    val fatGrams: Float,
    val fiberGrams: Float = 0f
)

data class ParsedNutritionResult(
    val rawInput: String,
    val suggestedName: String,
    val portionDescription: String,
    val totalCalories: Int,
    val totalProtein: Float,
    val totalCarbs: Float,
    val totalFat: Float,
    val totalFiber: Float,
    val items: List<ParsedFoodItem>,
    val engineUsed: String,
    val confidence: String
)

class NutritionParserEngine(
    private val geminiService: GeminiNutritionService = GeminiNutritionService()
) {

    suspend fun parse(input: String): ParsedNutritionResult {
        val trimmed = input.trim()
        if (trimmed.isBlank()) {
            return emptyResult(trimmed)
        }

        // 1. Gemini AI ile parse etmeyi dene (Eğer API anahtarı yapılandırılmışsa)
        try {
            val aiResponse = geminiService.parseNutritionText(trimmed)
            if (aiResponse != null && aiResponse.totalCalories > 0) {
                return ParsedNutritionResult(
                    rawInput = trimmed,
                    suggestedName = aiResponse.summaryTitle,
                    portionDescription = if (aiResponse.items.size == 1) aiResponse.items.first().portionDescription else "${aiResponse.items.size} besin",
                    totalCalories = aiResponse.totalCalories,
                    totalProtein = aiResponse.totalProtein,
                    totalCarbs = aiResponse.totalCarbs,
                    totalFat = aiResponse.totalFat,
                    totalFiber = aiResponse.totalFiber,
                    items = aiResponse.items.map {
                        ParsedFoodItem(
                            foodName = it.foodName,
                            portionDescription = it.portionDescription,
                            calories = it.calories,
                            proteinGrams = it.proteinGrams,
                            carbsGrams = it.carbsGrams,
                            fatGrams = it.fatGrams,
                            fiberGrams = it.fiberGrams
                        )
                    },
                    engineUsed = "Gemini 3.5 Flash Yapay Zeka",
                    confidence = "Yüksek (Yapay Zeka Doğrulamalı)"
                )
            }
        } catch (_: Exception) {
            // Hata durumunda yerel kural motoruna geç
        }

        // 2. Yüksek hassasiyetli Türkçe destekli çevrimdışı kural motoru
        return parseWithRuleEngine(trimmed)
    }

    fun parseWithRuleEngine(input: String): ParsedNutritionResult {
        val trimmed = input.trim()
        // Bileşik girdileri böl: "200g tavuk göğsü ve 1 porsiyon pirinç pilavı + 1 yemek kaşığı zeytinyağı"
        val delimiters = Regex("\\s+(?:ve|ile|yanında|yaninda|artı|arti|and|with|\\+|&|plus)\\s+|[,;]\\s*", RegexOption.IGNORE_CASE)
        val rawSegments = trimmed.split(delimiters).map { it.trim() }.filter { it.isNotBlank() }

        val parsedItems = mutableListOf<ParsedFoodItem>()

        for (segment in rawSegments) {
            val item = parseSingleSegment(segment)
            if (item != null) {
                parsedItems.add(item)
            }
        }

        if (parsedItems.isEmpty()) {
            val single = parseSingleSegment(trimmed)
            if (single != null) {
                parsedItems.add(single)
            } else {
                val numMatch = Regex("(\\d+)").find(trimmed)
                val detectedCalories = numMatch?.value?.toIntOrNull()?.coerceIn(50, 2000) ?: 200
                return ParsedNutritionResult(
                    rawInput = trimmed,
                    suggestedName = trimmed.replaceFirstChar { it.uppercase() },
                    portionDescription = "1 standart porsiyon",
                    totalCalories = detectedCalories,
                    totalProtein = (detectedCalories * 0.15f / 4f).roundTo1Decimal(),
                    totalCarbs = (detectedCalories * 0.55f / 4f).roundTo1Decimal(),
                    totalFat = (detectedCalories * 0.30f / 9f).roundTo1Decimal(),
                    totalFiber = 2f,
                    items = listOf(
                        ParsedFoodItem(
                            foodName = trimmed.replaceFirstChar { it.uppercase() },
                            portionDescription = "1 standart porsiyon",
                            calories = detectedCalories,
                            proteinGrams = (detectedCalories * 0.15f / 4f).roundTo1Decimal(),
                            carbsGrams = (detectedCalories * 0.55f / 4f).roundTo1Decimal(),
                            fatGrams = (detectedCalories * 0.30f / 9f).roundTo1Decimal(),
                            fiberGrams = 2f
                        )
                    ),
                    engineUsed = "caloree Akıllı Kural Motoru",
                    confidence = "Tahmini"
                )
            }
        }

        val totalCalories = parsedItems.sumOf { it.calories }
        val totalProtein = parsedItems.map { it.proteinGrams }.sum().roundTo1Decimal()
        val totalCarbs = parsedItems.map { it.carbsGrams }.sum().roundTo1Decimal()
        val totalFat = parsedItems.map { it.fatGrams }.sum().roundTo1Decimal()
        val totalFiber = parsedItems.map { it.fiberGrams }.sum().roundTo1Decimal()

        val mainTitle = if (parsedItems.size == 1) {
            parsedItems.first().foodName
        } else {
            parsedItems.joinToString(" & ") { it.foodName }
        }

        val portionSummary = if (parsedItems.size == 1) {
            parsedItems.first().portionDescription
        } else {
            "${parsedItems.size} besin karışımı"
        }

        return ParsedNutritionResult(
            rawInput = trimmed,
            suggestedName = mainTitle,
            portionDescription = portionSummary,
            totalCalories = totalCalories,
            totalProtein = totalProtein,
            totalCarbs = totalCarbs,
            totalFat = totalFat,
            totalFiber = totalFiber,
            items = parsedItems,
            engineUsed = "caloree Akıllı Kural Motoru",
            confidence = "Yüksek (Kural Motoru)"
        )
    }

    private fun parseSingleSegment(segment: String): ParsedFoodItem? {
        var text = segment.lowercase()

        // Türkçe ve İngilizce sayı kelimelerini normalleştir
        text = text
            .replace(Regex("\\b(bir|a|an|one)\\b"), "1")
            .replace(Regex("\\b(iki|two)\\b"), "2")
            .replace(Regex("\\b(üç|uc|three)\\b"), "3")
            .replace(Regex("\\b(dört|dort|four)\\b"), "4")
            .replace(Regex("\\b(beş|bes|five)\\b"), "5")
            .replace(Regex("\\b(yarım|yarim|half)\\b"), "0.5")
            .replace(Regex("\\b(çeyrek|ceyrek|quarter)\\b"), "0.25")
            .replace("1/2", "0.5")
            .replace("1/4", "0.25")
            .replace("3/4", "0.75")

        // Türkçe & İngilizce ölçü birimleri regex deseni
        val quantityPattern = Regex(
            "(\\d+(?:[.,]\\d+)?)\\s*(gram|gr|g|kilo|kg|oz|ml|su\\s*bardağı|su\\s*bardagi|bardak|fincan|kupa|yemek\\s*kaşığı|yemek\\s*kasigi|tatlı\\s*kaşığı|tatli\\s*kasigi|çay\\s*kaşığı|cay\\s*kasigi|kaşık|kasik|dilim|adet|tane|ölçek|olcek|porsiyon|tabak|kase|avuç|avuc|kutu|fileto|cups?|tbsps?|tsps?|slices?|pieces?|scoops?|cans?|bowls?)?\\b",
            RegexOption.IGNORE_CASE
        )

        val match = quantityPattern.find(text)
        var quantity = 1f
        var unit: String? = null
        var foodTerm = text

        if (match != null) {
            val numStr = match.groupValues[1].replace(',', '.')
            quantity = numStr.toFloatOrNull() ?: 1f
            unit = match.groupValues.getOrNull(2)?.takeIf { it.isNotBlank() }
            foodTerm = text.removeRange(match.range).trim()
        }

        // Gereksiz bağlaçları temizle
        foodTerm = foodTerm
            .replace(Regex("^(tane|adet|porsiyon|dilim|kase|bardak|gram|gr)\\s+"), "")
            .replace(Regex("^of\\s+"), "")
            .replace(Regex("\\s+of\\s+"), " ")
            .trim()

        val foodDef = FoodDatabase.findBestMatch(foodTerm) ?: FoodDatabase.findBestMatch(text) ?: return null

        val cleanUnit = unit?.lowercase()?.replace(" ", "") ?: ""

        // Gram cinsinden ağırlık hesapla
        val gramWeight: Float = when {
            cleanUnit in listOf("g", "gr", "gram", "grams") -> quantity
            cleanUnit in listOf("kg", "kilo") -> quantity * 1000f
            cleanUnit in listOf("oz", "ounce") -> quantity * 28.35f
            cleanUnit in listOf("ml") -> quantity * 1.0f
            cleanUnit in listOf("subardağı", "subardagi", "bardak", "cup", "cups") -> quantity * foodDef.standardUnitWeightGrams
            cleanUnit in listOf("yemekkaşığı", "yemekkasigi", "tbsp", "tablespoon") -> quantity * 15f
            cleanUnit in listOf("tatlıkaşığı", "tatlikasigi") -> quantity * 10f
            cleanUnit in listOf("çaykaşığı", "caykasigi", "tsp", "teaspoon") -> quantity * 5f
            cleanUnit in listOf("kaşık", "kasik") -> quantity * 15f
            cleanUnit in listOf("dilim", "slice", "slices") -> quantity * foodDef.standardUnitWeightGrams
            cleanUnit in listOf("kase", "tabak", "porsiyon", "fincan", "kupa", "avuç", "avuc", "kutu", "fileto", "ölçek", "olcek", "scoop", "scoops") ->
                quantity * foodDef.standardUnitWeightGrams
            cleanUnit in listOf("adet", "tane", "piece", "pieces") -> quantity * foodDef.standardUnitWeightGrams
            else -> {
                if (quantity > 25 && foodDef.standardUnit == "g") {
                    quantity // Örneğin "150 tavuk" yazılmışsa 150 gram varsay
                } else {
                    quantity * foodDef.standardUnitWeightGrams
                }
            }
        }

        val multiplier = gramWeight / 100f
        val calories = (foodDef.caloriesPer100g * multiplier).roundToInt()
        val protein = (foodDef.proteinPer100g * multiplier).roundTo1Decimal()
        val carbs = (foodDef.carbsPer100g * multiplier).roundTo1Decimal()
        val fat = (foodDef.fatPer100g * multiplier).roundTo1Decimal()
        val fiber = (foodDef.fiberPer100g * multiplier).roundTo1Decimal()

        val displayPortion = if (unit != null) {
            "$quantity $unit"
        } else if (gramWeight != 100f) {
            "${gramWeight.roundToInt()}g"
        } else {
            "1 porsiyon (100g)"
        }

        return ParsedFoodItem(
            foodName = foodDef.canonicalName,
            portionDescription = displayPortion,
            calories = calories,
            proteinGrams = protein,
            carbsGrams = carbs,
            fatGrams = fat,
            fiberGrams = fiber
        )
    }

    private fun Float.roundTo1Decimal(): Float {
        return (this * 10f).roundToInt() / 10f
    }

    private fun emptyResult(raw: String): ParsedNutritionResult {
        return ParsedNutritionResult(
            rawInput = raw,
            suggestedName = "Özel Besin Girişi",
            portionDescription = "1 porsiyon",
            totalCalories = 0,
            totalProtein = 0f,
            totalCarbs = 0f,
            totalFat = 0f,
            totalFiber = 0f,
            items = emptyList(),
            engineUsed = "caloree Akıllı Kural Motoru",
            confidence = "Yok"
        )
    }
}
