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
    val engineUsed: String, // "Gemini AI" or "NutriLog Smart Rule Engine"
    val confidence: String // "High", "Medium", "Estimated"
)

class NutritionParserEngine(
    private val geminiService: GeminiNutritionService = GeminiNutritionService()
) {

    suspend fun parse(input: String): ParsedNutritionResult {
        val trimmed = input.trim()
        if (trimmed.isBlank()) {
            return emptyResult(trimmed)
        }

        // 1. Try Gemini AI if available
        try {
            val aiResponse = geminiService.parseNutritionText(trimmed)
            if (aiResponse != null && aiResponse.totalCalories > 0) {
                return ParsedNutritionResult(
                    rawInput = trimmed,
                    suggestedName = aiResponse.summaryTitle,
                    portionDescription = if (aiResponse.items.size == 1) aiResponse.items.first().portionDescription else "${aiResponse.items.size} items",
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
                    engineUsed = "Gemini 3.5 Flash AI",
                    confidence = "High (AI Verified)"
                )
            }
        } catch (_: Exception) {
            // Fallback to local rule engine
        }

        // 2. High-precision offline rule engine fallback
        return parseWithRuleEngine(trimmed)
    }

    fun parseWithRuleEngine(input: String): ParsedNutritionResult {
        val trimmed = input.trim()
        // Split composite inputs: "200g chicken breast and 1 cup rice + 1 tbsp olive oil"
        val delimiters = Regex("\\s+(?:and|with|\\+|&|plus)\\s+|[,;]\\s*", RegexOption.IGNORE_CASE)
        val rawSegments = trimmed.split(delimiters).map { it.trim() }.filter { it.isNotBlank() }

        val parsedItems = mutableListOf<ParsedFoodItem>()

        for (segment in rawSegments) {
            val item = parseSingleSegment(segment)
            if (item != null) {
                parsedItems.add(item)
            }
        }

        if (parsedItems.isEmpty()) {
            // Attempt whole string fallback
            val single = parseSingleSegment(trimmed)
            if (single != null) {
                parsedItems.add(single)
            } else {
                // Generic heuristic: extract any number as calorie or estimate standard portion
                val numMatch = Regex("(\\d+)").find(trimmed)
                val detectedCalories = numMatch?.value?.toIntOrNull()?.coerceIn(50, 2000) ?: 200
                return ParsedNutritionResult(
                    rawInput = trimmed,
                    suggestedName = trimmed.replaceFirstChar { it.uppercase() },
                    portionDescription = "1 standard serving",
                    totalCalories = detectedCalories,
                    totalProtein = (detectedCalories * 0.15f / 4f).roundTo1Decimal(),
                    totalCarbs = (detectedCalories * 0.55f / 4f).roundTo1Decimal(),
                    totalFat = (detectedCalories * 0.30f / 9f).roundTo1Decimal(),
                    totalFiber = 2f,
                    items = listOf(
                        ParsedFoodItem(
                            foodName = trimmed.replaceFirstChar { it.uppercase() },
                            portionDescription = "1 standard serving",
                            calories = detectedCalories,
                            proteinGrams = (detectedCalories * 0.15f / 4f).roundTo1Decimal(),
                            carbsGrams = (detectedCalories * 0.55f / 4f).roundTo1Decimal(),
                            fatGrams = (detectedCalories * 0.30f / 9f).roundTo1Decimal(),
                            fiberGrams = 2f
                        )
                    ),
                    engineUsed = "NutriLog Smart Rule Engine",
                    confidence = "Estimated"
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
            "${parsedItems.size} items combined"
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
            engineUsed = "NutriLog Smart Rule Engine",
            confidence = "High (Rule Engine)"
        )
    }

    private fun parseSingleSegment(segment: String): ParsedFoodItem? {
        var text = segment.lowercase()

        // Normalize verbal numbers
        text = text
            .replace(Regex("\\b(a|an)\\b"), "1")
            .replace(Regex("\\bone\\b"), "1")
            .replace(Regex("\\btwo\\b"), "2")
            .replace(Regex("\\bthree\\b"), "3")
            .replace(Regex("\\bfour\\b"), "4")
            .replace(Regex("\\bfive\\b"), "5")
            .replace(Regex("\\bhalf\\b"), "0.5")
            .replace(Regex("\\bquarter\\b"), "0.25")
            .replace("1/2", "0.5")
            .replace("1/4", "0.25")
            .replace("3/4", "0.75")

        // Regex patterns for quantity and units
        // Examples: "200g chicken", "200 grams chicken", "2 slices bread", "1.5 cup rice", "1 scoop whey"
        val quantityPattern = Regex(
            "(\\d+(?:\\.\\d+)?)\\s*(g|grams|gram|kg|oz|ounces|ounce|lbs?|pounds?|ml|cups?|tbsps?|tablespoons?|tsps?|teaspoons?|slices?|pieces?|eggs?|fillets?|scoops?|cans?|bowls?|handfuls?)?\\b",
            RegexOption.IGNORE_CASE
        )

        val match = quantityPattern.find(text)
        var quantity = 1f
        var unit: String? = null
        var foodTerm = text

        if (match != null) {
            quantity = match.groupValues[1].toFloatOrNull() ?: 1f
            unit = match.groupValues.getOrNull(2)?.takeIf { it.isNotBlank() }
            foodTerm = text.removeRange(match.range).trim()
        }

        // Clean extra prepositions like "of" ("1 cup of oats" -> "oats")
        foodTerm = foodTerm.replace(Regex("^of\\s+"), "").replace(Regex("\\s+of\\s+"), " ").trim()

        val foodDef = FoodDatabase.findBestMatch(foodTerm) ?: FoodDatabase.findBestMatch(text)

        if (foodDef == null) {
            return null
        }

        // Calculate gram weight equivalent
        val gramWeight: Float = when (unit?.lowercase()) {
            "g", "gram", "grams" -> quantity
            "kg" -> quantity * 1000f
            "oz", "ounce", "ounces" -> quantity * 28.35f
            "lb", "lbs", "pound", "pounds" -> quantity * 453.59f
            "ml" -> quantity * 1.0f // approx for liquids/water
            "cup", "cups" -> quantity * foodDef.standardUnitWeightGrams
            "tbsp", "tbsps", "tablespoon", "tablespoons" -> quantity * 15f
            "tsp", "tsps", "teaspoon", "teaspoons" -> quantity * 5f
            "slice", "slices" -> quantity * foodDef.standardUnitWeightGrams
            "piece", "pieces", "egg", "eggs", "fillet", "fillets", "scoop", "scoops", "can", "cans", "bowl", "bowls", "handful", "handfuls" ->
                quantity * foodDef.standardUnitWeightGrams
            else -> {
                // If no unit matched, but a quantity exists: assume unit is standard unit of food (e.g. 2 eggs -> 2 * 50g)
                if (quantity > 20 && foodDef.standardUnit == "g") {
                    quantity // assume user typed "150 chicken" -> 150 grams
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
            "1 serving (100g)"
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
            suggestedName = "Custom Entry",
            portionDescription = "1 serving",
            totalCalories = 0,
            totalProtein = 0f,
            totalCarbs = 0f,
            totalFat = 0f,
            totalFiber = 0f,
            items = emptyList(),
            engineUsed = "NutriLog Smart Rule Engine",
            confidence = "None"
        )
    }
}
