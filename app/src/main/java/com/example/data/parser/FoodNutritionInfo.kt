package com.example.data.parser

data class NutritionItemDef(
    val id: String,
    val canonicalName: String,
    val keywords: List<String>,
    val caloriesPer100g: Float,
    val proteinPer100g: Float,
    val carbsPer100g: Float,
    val fatPer100g: Float,
    val fiberPer100g: Float = 0f,
    val standardUnit: String = "g",
    val standardUnitWeightGrams: Float = 100f
)

object FoodDatabase {
    val items = listOf(
        // Poultry & Meats
        NutritionItemDef(
            id = "chicken_breast",
            canonicalName = "Chicken Breast",
            keywords = listOf("chicken breast", "chicken", "grilled chicken", "boiled chicken"),
            caloriesPer100g = 165f,
            proteinPer100g = 31f,
            carbsPer100g = 0f,
            fatPer100g = 3.6f,
            fiberPer100g = 0f,
            standardUnit = "g",
            standardUnitWeightGrams = 100f
        ),
        NutritionItemDef(
            id = "chicken_thigh",
            canonicalName = "Chicken Thigh",
            keywords = listOf("chicken thigh", "chicken leg"),
            caloriesPer100g = 209f,
            proteinPer100g = 26f,
            carbsPer100g = 0f,
            fatPer100g = 10.9f,
            standardUnit = "piece",
            standardUnitWeightGrams = 120f
        ),
        NutritionItemDef(
            id = "ground_beef_lean",
            canonicalName = "Lean Ground Beef (90/10)",
            keywords = listOf("ground beef", "beef mince", "minced beef", "beef"),
            caloriesPer100g = 217f,
            proteinPer100g = 26.1f,
            carbsPer100g = 0f,
            fatPer100g = 11.8f
        ),
        NutritionItemDef(
            id = "steak_sirloin",
            canonicalName = "Sirloin Steak",
            keywords = listOf("sirloin steak", "steak", "beef steak"),
            caloriesPer100g = 244f,
            proteinPer100g = 27f,
            carbsPer100g = 0f,
            fatPer100g = 14f
        ),
        NutritionItemDef(
            id = "turkey_breast",
            canonicalName = "Turkey Breast",
            keywords = listOf("turkey breast", "turkey"),
            caloriesPer100g = 135f,
            proteinPer100g = 30f,
            carbsPer100g = 0f,
            fatPer100g = 1.6f
        ),
        NutritionItemDef(
            id = "salmon",
            canonicalName = "Atlantic Salmon",
            keywords = listOf("salmon", "grilled salmon", "baked salmon", "salmon fillet"),
            caloriesPer100g = 208f,
            proteinPer100g = 20.4f,
            carbsPer100g = 0f,
            fatPer100g = 13.4f,
            standardUnit = "fillet",
            standardUnitWeightGrams = 150f
        ),
        NutritionItemDef(
            id = "tuna_canned",
            canonicalName = "Canned Tuna in Water",
            keywords = listOf("tuna", "canned tuna", "tuna in water"),
            caloriesPer100g = 116f,
            proteinPer100g = 25.5f,
            carbsPer100g = 0f,
            fatPer100g = 0.8f,
            standardUnit = "can",
            standardUnitWeightGrams = 140f
        ),
        NutritionItemDef(
            id = "shrimp",
            canonicalName = "Cooked Shrimp",
            keywords = listOf("shrimp", "prawns", "prawn"),
            caloriesPer100g = 99f,
            proteinPer100g = 24f,
            carbsPer100g = 0.2f,
            fatPer100g = 0.3f
        ),
        NutritionItemDef(
            id = "tofu_firm",
            canonicalName = "Firm Tofu",
            keywords = listOf("tofu", "firm tofu", "bean curd"),
            caloriesPer100g = 144f,
            proteinPer100g = 15.6f,
            carbsPer100g = 2.8f,
            fatPer100g = 8.7f,
            fiberPer100g = 2.3f
        ),

        // Eggs & Dairy
        NutritionItemDef(
            id = "egg_whole",
            canonicalName = "Whole Egg",
            keywords = listOf("egg", "eggs", "whole egg", "boiled egg", "fried egg"),
            caloriesPer100g = 143f,
            proteinPer100g = 12.6f,
            carbsPer100g = 0.7f,
            fatPer100g = 9.5f,
            standardUnit = "piece",
            standardUnitWeightGrams = 50f // 1 medium/large egg ~50g -> ~72 kcal
        ),
        NutritionItemDef(
            id = "egg_white",
            canonicalName = "Egg Whites",
            keywords = listOf("egg white", "egg whites"),
            caloriesPer100g = 52f,
            proteinPer100g = 10.9f,
            carbsPer100g = 0.7f,
            fatPer100g = 0.2f,
            standardUnit = "piece",
            standardUnitWeightGrams = 33f
        ),
        NutritionItemDef(
            id = "greek_yogurt_plain",
            canonicalName = "Greek Yogurt (Nonfat Plain)",
            keywords = listOf("greek yogurt", "yogurt", "nonfat greek yogurt"),
            caloriesPer100g = 59f,
            proteinPer100g = 10.2f,
            carbsPer100g = 3.6f,
            fatPer100g = 0.4f,
            standardUnit = "cup",
            standardUnitWeightGrams = 170f
        ),
        NutritionItemDef(
            id = "milk_whole",
            canonicalName = "Whole Milk",
            keywords = listOf("milk", "whole milk", "cow milk"),
            caloriesPer100g = 62f,
            proteinPer100g = 3.2f,
            carbsPer100g = 4.8f,
            fatPer100g = 3.3f,
            standardUnit = "cup",
            standardUnitWeightGrams = 244f
        ),
        NutritionItemDef(
            id = "milk_almond",
            canonicalName = "Unsweetened Almond Milk",
            keywords = listOf("almond milk", "almond beverage"),
            caloriesPer100g = 15f,
            proteinPer100g = 0.6f,
            carbsPer100g = 0.3f,
            fatPer100g = 1.1f,
            standardUnit = "cup",
            standardUnitWeightGrams = 240f
        ),
        NutritionItemDef(
            id = "cheddar_cheese",
            canonicalName = "Cheddar Cheese",
            keywords = listOf("cheddar", "cheddar cheese", "cheese"),
            caloriesPer100g = 402f,
            proteinPer100g = 25f,
            carbsPer100g = 1.3f,
            fatPer100g = 33f,
            standardUnit = "slice",
            standardUnitWeightGrams = 28f
        ),
        NutritionItemDef(
            id = "cottage_cheese",
            canonicalName = "Low Fat Cottage Cheese",
            keywords = listOf("cottage cheese", "curd cheese"),
            caloriesPer100g = 81f,
            proteinPer100g = 11.1f,
            carbsPer100g = 4.7f,
            fatPer100g = 2.3f,
            standardUnit = "cup",
            standardUnitWeightGrams = 226f
        ),
        NutritionItemDef(
            id = "whey_protein",
            canonicalName = "Whey Protein Powder",
            keywords = listOf("whey protein", "protein powder", "whey", "protein shake"),
            caloriesPer100g = 400f,
            proteinPer100g = 80f,
            carbsPer100g = 8f,
            fatPer100g = 4f,
            standardUnit = "scoop",
            standardUnitWeightGrams = 30f // 1 scoop = ~120 kcal, 24g protein
        ),

        // Grains, Breads & Carbs
        NutritionItemDef(
            id = "white_rice_cooked",
            canonicalName = "Cooked White Rice",
            keywords = listOf("white rice", "rice", "cooked rice", "jasmine rice", "basmati rice"),
            caloriesPer100g = 130f,
            proteinPer100g = 2.7f,
            carbsPer100g = 28.2f,
            fatPer100g = 0.3f,
            fiberPer100g = 0.4f,
            standardUnit = "cup",
            standardUnitWeightGrams = 158f // 1 cup cooked rice ~205 kcal
        ),
        NutritionItemDef(
            id = "brown_rice_cooked",
            canonicalName = "Cooked Brown Rice",
            keywords = listOf("brown rice", "wholegrain rice"),
            caloriesPer100g = 112f,
            proteinPer100g = 2.6f,
            carbsPer100g = 23.5f,
            fatPer100g = 0.9f,
            fiberPer100g = 1.8f,
            standardUnit = "cup",
            standardUnitWeightGrams = 195f
        ),
        NutritionItemDef(
            id = "rolled_oats",
            canonicalName = "Rolled Oats (Dry)",
            keywords = listOf("oats", "oatmeal", "rolled oats", "porridge"),
            caloriesPer100g = 379f,
            proteinPer100g = 13.2f,
            carbsPer100g = 67.7f,
            fatPer100g = 6.5f,
            fiberPer100g = 10.1f,
            standardUnit = "cup",
            standardUnitWeightGrams = 80f // 1 cup dry rolled oats ~80g ~300 kcal
        ),
        NutritionItemDef(
            id = "whole_wheat_bread",
            canonicalName = "Whole Wheat Bread",
            keywords = listOf("whole wheat bread", "wheat bread", "bread", "toast", "slice of bread"),
            caloriesPer100g = 247f,
            proteinPer100g = 13f,
            carbsPer100g = 41.3f,
            fatPer100g = 3.4f,
            fiberPer100g = 6f,
            standardUnit = "slice",
            standardUnitWeightGrams = 32f // 1 slice ~80 kcal
        ),
        NutritionItemDef(
            id = "pasta_cooked",
            canonicalName = "Cooked Pasta",
            keywords = listOf("pasta", "spaghetti", "macaroni", "noodles", "penne"),
            caloriesPer100g = 158f,
            proteinPer100g = 5.8f,
            carbsPer100g = 30.9f,
            fatPer100g = 0.9f,
            fiberPer100g = 1.8f,
            standardUnit = "cup",
            standardUnitWeightGrams = 140f
        ),
        NutritionItemDef(
            id = "potato_baked",
            canonicalName = "Baked Potato",
            keywords = listOf("potato", "baked potato", "russet potato"),
            caloriesPer100g = 93f,
            proteinPer100g = 2.5f,
            carbsPer100g = 21.2f,
            fatPer100g = 0.1f,
            fiberPer100g = 2.2f,
            standardUnit = "piece",
            standardUnitWeightGrams = 173f
        ),
        NutritionItemDef(
            id = "sweet_potato",
            canonicalName = "Baked Sweet Potato",
            keywords = listOf("sweet potato", "baked sweet potato", "yam"),
            caloriesPer100g = 90f,
            proteinPer100g = 2.0f,
            carbsPer100g = 20.7f,
            fatPer100g = 0.2f,
            fiberPer100g = 3.3f,
            standardUnit = "piece",
            standardUnitWeightGrams = 150f
        ),
        NutritionItemDef(
            id = "quinoa_cooked",
            canonicalName = "Cooked Quinoa",
            keywords = listOf("quinoa", "cooked quinoa"),
            caloriesPer100g = 120f,
            proteinPer100g = 4.4f,
            carbsPer100g = 21.3f,
            fatPer100g = 1.9f,
            fiberPer100g = 2.8f,
            standardUnit = "cup",
            standardUnitWeightGrams = 185f
        ),

        // Fruits
        NutritionItemDef(
            id = "banana",
            canonicalName = "Banana",
            keywords = listOf("banana", "bananas"),
            caloriesPer100g = 89f,
            proteinPer100g = 1.1f,
            carbsPer100g = 22.8f,
            fatPer100g = 0.3f,
            fiberPer100g = 2.6f,
            standardUnit = "piece",
            standardUnitWeightGrams = 118f // 1 medium banana ~105 kcal
        ),
        NutritionItemDef(
            id = "apple",
            canonicalName = "Apple",
            keywords = listOf("apple", "apples", "red apple", "green apple"),
            caloriesPer100g = 52f,
            proteinPer100g = 0.3f,
            carbsPer100g = 13.8f,
            fatPer100g = 0.2f,
            fiberPer100g = 2.4f,
            standardUnit = "piece",
            standardUnitWeightGrams = 182f // 1 medium apple ~95 kcal
        ),
        NutritionItemDef(
            id = "blueberries",
            canonicalName = "Fresh Blueberries",
            keywords = listOf("blueberries", "blueberry", "berries"),
            caloriesPer100g = 57f,
            proteinPer100g = 0.7f,
            carbsPer100g = 14.5f,
            fatPer100g = 0.3f,
            fiberPer100g = 2.4f,
            standardUnit = "cup",
            standardUnitWeightGrams = 148f
        ),
        NutritionItemDef(
            id = "strawberries",
            canonicalName = "Fresh Strawberries",
            keywords = listOf("strawberries", "strawberry"),
            caloriesPer100g = 32f,
            proteinPer100g = 0.7f,
            carbsPer100g = 7.7f,
            fatPer100g = 0.3f,
            fiberPer100g = 2.0f,
            standardUnit = "cup",
            standardUnitWeightGrams = 152f
        ),
        NutritionItemDef(
            id = "orange",
            canonicalName = "Orange",
            keywords = listOf("orange", "oranges"),
            caloriesPer100g = 47f,
            proteinPer100g = 0.9f,
            carbsPer100g = 11.8f,
            fatPer100g = 0.1f,
            fiberPer100g = 2.4f,
            standardUnit = "piece",
            standardUnitWeightGrams = 131f
        ),
        NutritionItemDef(
            id = "avocado",
            canonicalName = "Avocado",
            keywords = listOf("avocado", "avocados"),
            caloriesPer100g = 160f,
            proteinPer100g = 2.0f,
            carbsPer100g = 8.5f,
            fatPer100g = 14.7f,
            fiberPer100g = 6.7f,
            standardUnit = "piece",
            standardUnitWeightGrams = 150f
        ),

        // Vegetables
        NutritionItemDef(
            id = "broccoli",
            canonicalName = "Broccoli",
            keywords = listOf("broccoli", "steamed broccoli"),
            caloriesPer100g = 35f,
            proteinPer100g = 2.4f,
            carbsPer100g = 7.2f,
            fatPer100g = 0.4f,
            fiberPer100g = 3.3f,
            standardUnit = "cup",
            standardUnitWeightGrams = 91f
        ),
        NutritionItemDef(
            id = "spinach",
            canonicalName = "Fresh Spinach",
            keywords = listOf("spinach", "baby spinach"),
            caloriesPer100g = 23f,
            proteinPer100g = 2.9f,
            carbsPer100g = 3.6f,
            fatPer100g = 0.4f,
            fiberPer100g = 2.2f,
            standardUnit = "cup",
            standardUnitWeightGrams = 30f
        ),
        NutritionItemDef(
            id = "salad_mixed",
            canonicalName = "Mixed Greens Salad",
            keywords = listOf("salad", "mixed salad", "green salad", "lettuce"),
            caloriesPer100g = 17f,
            proteinPer100g = 1.4f,
            carbsPer100g = 3.3f,
            fatPer100g = 0.2f,
            fiberPer100g = 1.3f,
            standardUnit = "bowl",
            standardUnitWeightGrams = 120f
        ),

        // Healthy Fats & Nuts
        NutritionItemDef(
            id = "peanut_butter",
            canonicalName = "Peanut Butter",
            keywords = listOf("peanut butter", "pb"),
            caloriesPer100g = 588f,
            proteinPer100g = 25.1f,
            carbsPer100g = 20.0f,
            fatPer100g = 50.4f,
            fiberPer100g = 6.0f,
            standardUnit = "tbsp",
            standardUnitWeightGrams = 16f // 1 tbsp ~94 kcal
        ),
        NutritionItemDef(
            id = "olive_oil",
            canonicalName = "Extra Virgin Olive Oil",
            keywords = listOf("olive oil", "oil", "vegetable oil"),
            caloriesPer100g = 884f,
            proteinPer100g = 0f,
            carbsPer100g = 0f,
            fatPer100g = 100f,
            standardUnit = "tbsp",
            standardUnitWeightGrams = 14f // 1 tbsp ~120 kcal
        ),
        NutritionItemDef(
            id = "butter",
            canonicalName = "Butter",
            keywords = listOf("butter", "salted butter"),
            caloriesPer100g = 717f,
            proteinPer100g = 0.9f,
            carbsPer100g = 0.1f,
            fatPer100g = 81f,
            standardUnit = "tbsp",
            standardUnitWeightGrams = 14f
        ),
        NutritionItemDef(
            id = "almonds",
            canonicalName = "Almonds",
            keywords = listOf("almonds", "raw almonds", "roasted almonds"),
            caloriesPer100g = 579f,
            proteinPer100g = 21.2f,
            carbsPer100g = 21.6f,
            fatPer100g = 49.9f,
            fiberPer100g = 12.5f,
            standardUnit = "handful",
            standardUnitWeightGrams = 30f
        ),
        NutritionItemDef(
            id = "walnuts",
            canonicalName = "Walnuts",
            keywords = listOf("walnuts"),
            caloriesPer100g = 654f,
            proteinPer100g = 15.2f,
            carbsPer100g = 13.7f,
            fatPer100g = 65.2f,
            fiberPer100g = 6.7f,
            standardUnit = "handful",
            standardUnitWeightGrams = 30f
        ),

        // Popular Quick Meals & Beverages
        NutritionItemDef(
            id = "pizza_slice",
            canonicalName = "Pepperoni / Cheese Pizza Slice",
            keywords = listOf("pizza", "slice of pizza", "pepperoni pizza", "pizza slice"),
            caloriesPer100g = 266f,
            proteinPer100g = 11.4f,
            carbsPer100g = 33.3f,
            fatPer100g = 10.1f,
            fiberPer100g = 2.3f,
            standardUnit = "slice",
            standardUnitWeightGrams = 107f // 1 slice ~285 kcal
        ),
        NutritionItemDef(
            id = "burger_beef",
            canonicalName = "Beef Hamburger",
            keywords = listOf("burger", "cheeseburger", "hamburger"),
            caloriesPer100g = 250f,
            proteinPer100g = 14f,
            carbsPer100g = 24f,
            fatPer100g = 11f,
            standardUnit = "piece",
            standardUnitWeightGrams = 200f // ~500 kcal
        ),
        NutritionItemDef(
            id = "black_coffee",
            canonicalName = "Black Coffee",
            keywords = listOf("black coffee", "espresso", "americano"),
            caloriesPer100g = 2f,
            proteinPer100g = 0.3f,
            carbsPer100g = 0f,
            fatPer100g = 0f,
            standardUnit = "cup",
            standardUnitWeightGrams = 240f
        )
    )

    fun findBestMatch(rawFoodTerm: String): NutritionItemDef? {
        val clean = rawFoodTerm.trim().lowercase()
        // Exact keyword match
        items.forEach { def ->
            if (def.keywords.any { it.equals(clean, ignoreCase = true) }) return def
        }
        // Substring / contained match
        items.forEach { def ->
            if (def.keywords.any { clean.contains(it) || it.contains(clean) }) return def
        }
        // Word token overlap
        val tokens = clean.split(Regex("[^a-zA-Z]+")).filter { it.length > 2 }
        var bestItem: NutritionItemDef? = null
        var bestScore = 0
        for (item in items) {
            var score = 0
            for (kw in item.keywords) {
                val kwTokens = kw.split(Regex("[^a-zA-Z]+")).filter { it.length > 2 }
                val overlap = tokens.intersect(kwTokens.toSet()).size
                if (overlap > score) {
                    score = overlap
                }
            }
            if (score > bestScore) {
                bestScore = score
                bestItem = item
            }
        }
        return if (bestScore > 0) bestItem else null
    }
}
