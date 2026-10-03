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
        // Et ve Kümes Hayvanları
        NutritionItemDef(
            id = "chicken_breast",
            canonicalName = "Tavuk Göğsü",
            keywords = listOf("tavuk göğsü", "tavuk gogsu", "tavuk", "ızgara tavuk", "izgara tavuk", "haşlanmış tavuk", "haslanmis tavuk", "chicken breast", "chicken", "grilled chicken"),
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
            canonicalName = "Tavuk But / Kalça",
            keywords = listOf("tavuk but", "tavuk kalça", "tavuk baget", "chicken thigh", "chicken leg"),
            caloriesPer100g = 209f,
            proteinPer100g = 26f,
            carbsPer100g = 0f,
            fatPer100g = 10.9f,
            standardUnit = "adet",
            standardUnitWeightGrams = 120f
        ),
        NutritionItemDef(
            id = "ground_beef_lean",
            canonicalName = "Yağsız Dana Kıyma",
            keywords = listOf("kıyma", "kiyma", "dana kıyma", "dana eti", "köfte", "kofte", "et", "ground beef", "beef"),
            caloriesPer100g = 217f,
            proteinPer100g = 26.1f,
            carbsPer100g = 0f,
            fatPer100g = 11.8f
        ),
        NutritionItemDef(
            id = "steak_sirloin",
            canonicalName = "Dana Biftek / Antrikot",
            keywords = listOf("biftek", "antrikot", "bonfile", "steak", "sirloin"),
            caloriesPer100g = 244f,
            proteinPer100g = 27f,
            carbsPer100g = 0f,
            fatPer100g = 14f
        ),
        NutritionItemDef(
            id = "turkey_breast",
            canonicalName = "Hindi Göğsü",
            keywords = listOf("hindi göğsü", "hindi gogsu", "hindi", "turkey breast", "turkey"),
            caloriesPer100g = 135f,
            proteinPer100g = 30f,
            carbsPer100g = 0f,
            fatPer100g = 1.6f
        ),
        NutritionItemDef(
            id = "salmon",
            canonicalName = "Somon Balığı",
            keywords = listOf("somon", "somon balığı", "fırında somon", "somon fileto", "salmon"),
            caloriesPer100g = 208f,
            proteinPer100g = 20.4f,
            carbsPer100g = 0f,
            fatPer100g = 13.4f,
            standardUnit = "fileto",
            standardUnitWeightGrams = 150f
        ),
        NutritionItemDef(
            id = "tuna_canned",
            canonicalName = "Konserve Ton Balığı",
            keywords = listOf("ton balığı", "ton baligi", "konserve ton", "tuna"),
            caloriesPer100g = 116f,
            proteinPer100g = 25.5f,
            carbsPer100g = 0f,
            fatPer100g = 0.8f,
            standardUnit = "kutu",
            standardUnitWeightGrams = 140f
        ),
        NutritionItemDef(
            id = "shrimp",
            canonicalName = "Karides",
            keywords = listOf("karides", "prawns", "shrimp"),
            caloriesPer100g = 99f,
            proteinPer100g = 24f,
            carbsPer100g = 0.2f,
            fatPer100g = 0.3f
        ),
        NutritionItemDef(
            id = "tofu_firm",
            canonicalName = "Tofu",
            keywords = listOf("tofu", "soya peyniri"),
            caloriesPer100g = 144f,
            proteinPer100g = 15.6f,
            carbsPer100g = 2.8f,
            fatPer100g = 8.7f,
            fiberPer100g = 2.3f
        ),

        // Yumurta ve Süt Ürünleri
        NutritionItemDef(
            id = "egg_whole",
            canonicalName = "Haşlanmış / Bütün Yumurta",
            keywords = listOf("yumurta", "haşlanmış yumurta", "haslanmis yumurta", "sahanda yumurta", "kırılmış yumurta", "egg", "eggs"),
            caloriesPer100g = 143f,
            proteinPer100g = 12.6f,
            carbsPer100g = 0.7f,
            fatPer100g = 9.5f,
            standardUnit = "adet",
            standardUnitWeightGrams = 50f // 1 orta boy yumurta ~50g -> ~72 kcal
        ),
        NutritionItemDef(
            id = "egg_white",
            canonicalName = "Yumurta Beyazı / Akı",
            keywords = listOf("yumurta beyazı", "yumurta akı", "egg white", "egg whites"),
            caloriesPer100g = 52f,
            proteinPer100g = 10.9f,
            carbsPer100g = 0.7f,
            fatPer100g = 0.2f,
            standardUnit = "adet",
            standardUnitWeightGrams = 33f
        ),
        NutritionItemDef(
            id = "greek_yogurt_plain",
            canonicalName = "Süzme Yoğurt",
            keywords = listOf("yoğurt", "yogurt", "süzme yoğurt", "suzme yogurt", "grek yoğurt", "greek yogurt"),
            caloriesPer100g = 59f,
            proteinPer100g = 10.2f,
            carbsPer100g = 3.6f,
            fatPer100g = 0.4f,
            standardUnit = "kase",
            standardUnitWeightGrams = 170f
        ),
        NutritionItemDef(
            id = "milk_whole",
            canonicalName = "Süt",
            keywords = listOf("süt", "sut", "inek sütü", "tam yağlı süt", "milk"),
            caloriesPer100g = 62f,
            proteinPer100g = 3.2f,
            carbsPer100g = 4.8f,
            fatPer100g = 3.3f,
            standardUnit = "bardak",
            standardUnitWeightGrams = 200f
        ),
        NutritionItemDef(
            id = "milk_almond",
            canonicalName = "Badem Sütü",
            keywords = listOf("badem sütü", "badem sutu", "almond milk"),
            caloriesPer100g = 15f,
            proteinPer100g = 0.6f,
            carbsPer100g = 0.3f,
            fatPer100g = 1.1f,
            standardUnit = "bardak",
            standardUnitWeightGrams = 200f
        ),
        NutritionItemDef(
            id = "cheddar_cheese",
            canonicalName = "Kaşar / Beyaz Peynir",
            keywords = listOf("kaşar", "kasar", "peynir", "beyaz peynir", "kaşar peyniri", "cheese", "cheddar"),
            caloriesPer100g = 402f,
            proteinPer100g = 25f,
            carbsPer100g = 1.3f,
            fatPer100g = 33f,
            standardUnit = "dilim",
            standardUnitWeightGrams = 30f
        ),
        NutritionItemDef(
            id = "cottage_cheese",
            canonicalName = "Lor Peyniri",
            keywords = listOf("lor", "lor peyniri", "cottage cheese"),
            caloriesPer100g = 81f,
            proteinPer100g = 11.1f,
            carbsPer100g = 4.7f,
            fatPer100g = 2.3f,
            standardUnit = "kase",
            standardUnitWeightGrams = 150f
        ),
        NutritionItemDef(
            id = "whey_protein",
            canonicalName = "Whey Protein Tozu",
            keywords = listOf("protein tozu", "whey", "whey protein", "protein shake", "protein tozu shake"),
            caloriesPer100g = 400f,
            proteinPer100g = 80f,
            carbsPer100g = 8f,
            fatPer100g = 4f,
            standardUnit = "ölçek",
            standardUnitWeightGrams = 30f // 1 ölçek ~120 kcal, 24g protein
        ),

        // Tahıllar, Ekmek ve Karbonhidratlar
        NutritionItemDef(
            id = "white_rice_cooked",
            canonicalName = "Pirinç Pilavı",
            keywords = listOf("pirinç pilavı", "pirinc pilavi", "pilav", "pirinç", "pirinc", "beyaz pirinç", "white rice", "rice"),
            caloriesPer100g = 130f,
            proteinPer100g = 2.7f,
            carbsPer100g = 28.2f,
            fatPer100g = 0.3f,
            fiberPer100g = 0.4f,
            standardUnit = "porsiyon",
            standardUnitWeightGrams = 150f
        ),
        NutritionItemDef(
            id = "brown_rice_cooked",
            canonicalName = "Esmer Pirinç / Kepekli Pilav",
            keywords = listOf("esmer pirinç", "kepekli pirinç", "brown rice"),
            caloriesPer100g = 112f,
            proteinPer100g = 2.6f,
            carbsPer100g = 23.5f,
            fatPer100g = 0.9f,
            fiberPer100g = 1.8f,
            standardUnit = "porsiyon",
            standardUnitWeightGrams = 150f
        ),
        NutritionItemDef(
            id = "rolled_oats",
            canonicalName = "Yulaf Ezmesi",
            keywords = listOf("yulaf", "yulaf ezmesi", "yulaf lapası", "oats", "oatmeal"),
            caloriesPer100g = 379f,
            proteinPer100g = 13.2f,
            carbsPer100g = 67.7f,
            fatPer100g = 6.5f,
            fiberPer100g = 10.1f,
            standardUnit = "su bardağı",
            standardUnitWeightGrams = 80f
        ),
        NutritionItemDef(
            id = "whole_wheat_bread",
            canonicalName = "Tam Buğday Ekmeği",
            keywords = listOf("ekmek", "tam buğday ekmeği", "tam bugday ekmegi", "dilim ekmek", "tost", "kepek ekmeği", "bread", "toast"),
            caloriesPer100g = 247f,
            proteinPer100g = 13f,
            carbsPer100g = 41.3f,
            fatPer100g = 3.4f,
            fiberPer100g = 6f,
            standardUnit = "dilim",
            standardUnitWeightGrams = 32f // 1 dilim ~80 kcal
        ),
        NutritionItemDef(
            id = "pasta_cooked",
            canonicalName = "Makarna",
            keywords = listOf("makarna", "spagetti", "erişte", "pasta"),
            caloriesPer100g = 158f,
            proteinPer100g = 5.8f,
            carbsPer100g = 30.9f,
            fatPer100g = 0.9f,
            fiberPer100g = 1.8f,
            standardUnit = "tabak",
            standardUnitWeightGrams = 150f
        ),
        NutritionItemDef(
            id = "potato_baked",
            canonicalName = "Haşlanmış / Fırın Patates",
            keywords = listOf("patates", "fırın patates", "haşlanmış patates", "patates haşlama", "potato"),
            caloriesPer100g = 93f,
            proteinPer100g = 2.5f,
            carbsPer100g = 21.2f,
            fatPer100g = 0.1f,
            fiberPer100g = 2.2f,
            standardUnit = "adet",
            standardUnitWeightGrams = 150f
        ),
        NutritionItemDef(
            id = "sweet_potato",
            canonicalName = "Tatlı Patates",
            keywords = listOf("tatlı patates", "sweet potato"),
            caloriesPer100g = 90f,
            proteinPer100g = 2.0f,
            carbsPer100g = 20.7f,
            fatPer100g = 0.2f,
            fiberPer100g = 3.3f,
            standardUnit = "adet",
            standardUnitWeightGrams = 150f
        ),
        NutritionItemDef(
            id = "quinoa_cooked",
            canonicalName = "Kinoa",
            keywords = listOf("kinoa", "quinoa"),
            caloriesPer100g = 120f,
            proteinPer100g = 4.4f,
            carbsPer100g = 21.3f,
            fatPer100g = 1.9f,
            fiberPer100g = 2.8f,
            standardUnit = "porsiyon",
            standardUnitWeightGrams = 150f
        ),

        // Meyveler
        NutritionItemDef(
            id = "banana",
            canonicalName = "Muz",
            keywords = listOf("muz", "orta boy muz", "banana"),
            caloriesPer100g = 89f,
            proteinPer100g = 1.1f,
            carbsPer100g = 22.8f,
            fatPer100g = 0.3f,
            fiberPer100g = 2.6f,
            standardUnit = "adet",
            standardUnitWeightGrams = 118f // 1 orta muz ~105 kcal
        ),
        NutritionItemDef(
            id = "apple",
            canonicalName = "Elma",
            keywords = listOf("elma", "kırmızı elma", "yeşil elma", "orta boy elma", "apple"),
            caloriesPer100g = 52f,
            proteinPer100g = 0.3f,
            carbsPer100g = 13.8f,
            fatPer100g = 0.2f,
            fiberPer100g = 2.4f,
            standardUnit = "adet",
            standardUnitWeightGrams = 180f // 1 orta boy elma ~95 kcal
        ),
        NutritionItemDef(
            id = "blueberries",
            canonicalName = "Yaban Mersini",
            keywords = listOf("yaban mersini", "blueberry", "blueberries"),
            caloriesPer100g = 57f,
            proteinPer100g = 0.7f,
            carbsPer100g = 14.5f,
            fatPer100g = 0.3f,
            fiberPer100g = 2.4f,
            standardUnit = "kase",
            standardUnitWeightGrams = 140f
        ),
        NutritionItemDef(
            id = "strawberries",
            canonicalName = "Çilek",
            keywords = listOf("çilek", "cilek", "strawberry", "strawberries"),
            caloriesPer100g = 32f,
            proteinPer100g = 0.7f,
            carbsPer100g = 7.7f,
            fatPer100g = 0.3f,
            fiberPer100g = 2.0f,
            standardUnit = "kase",
            standardUnitWeightGrams = 150f
        ),
        NutritionItemDef(
            id = "orange",
            canonicalName = "Portakal",
            keywords = listOf("portakal", "orange"),
            caloriesPer100g = 47f,
            proteinPer100g = 0.9f,
            carbsPer100g = 11.8f,
            fatPer100g = 0.1f,
            fiberPer100g = 2.4f,
            standardUnit = "adet",
            standardUnitWeightGrams = 130f
        ),
        NutritionItemDef(
            id = "avocado",
            canonicalName = "Avokado",
            keywords = listOf("avokado", "avocado"),
            caloriesPer100g = 160f,
            proteinPer100g = 2.0f,
            carbsPer100g = 8.5f,
            fatPer100g = 14.7f,
            fiberPer100g = 6.7f,
            standardUnit = "adet",
            standardUnitWeightGrams = 150f
        ),

        // Sebzeler
        NutritionItemDef(
            id = "broccoli",
            canonicalName = "Brokoli",
            keywords = listOf("brokoli", "broccoli"),
            caloriesPer100g = 35f,
            proteinPer100g = 2.4f,
            carbsPer100g = 7.2f,
            fatPer100g = 0.4f,
            fiberPer100g = 3.3f,
            standardUnit = "kase",
            standardUnitWeightGrams = 100f
        ),
        NutritionItemDef(
            id = "spinach",
            canonicalName = "Ispanak",
            keywords = listOf("ıspanak", "ispanak", "spinach"),
            caloriesPer100g = 23f,
            proteinPer100g = 2.9f,
            carbsPer100g = 3.6f,
            fatPer100g = 0.4f,
            fiberPer100g = 2.2f,
            standardUnit = "porsiyon",
            standardUnitWeightGrams = 100f
        ),
        NutritionItemDef(
            id = "salad_mixed",
            canonicalName = "Mevsim Salata",
            keywords = listOf("salata", "yeşil salata", "mevsim salata", "çoban salata", "salad"),
            caloriesPer100g = 17f,
            proteinPer100g = 1.4f,
            carbsPer100g = 3.3f,
            fatPer100g = 0.2f,
            fiberPer100g = 1.3f,
            standardUnit = "kase",
            standardUnitWeightGrams = 150f
        ),

        // Sağlıklı Yağlar ve Kuruyemişler
        NutritionItemDef(
            id = "peanut_butter",
            canonicalName = "Fıstık Ezmesi",
            keywords = listOf("fıstık ezmesi", "fistik ezmesi", "peanut butter", "pb"),
            caloriesPer100g = 588f,
            proteinPer100g = 25.1f,
            carbsPer100g = 20.0f,
            fatPer100g = 50.4f,
            fiberPer100g = 6.0f,
            standardUnit = "yemek kaşığı",
            standardUnitWeightGrams = 16f // 1 yemek kaşığı ~94 kcal
        ),
        NutritionItemDef(
            id = "olive_oil",
            canonicalName = "Zeytinyağı",
            keywords = listOf("zeytinyağı", "zeytinyagi", "sıvı yağ", "sivi yag", "yağ", "yag", "olive oil"),
            caloriesPer100g = 884f,
            proteinPer100g = 0f,
            carbsPer100g = 0f,
            fatPer100g = 100f,
            standardUnit = "yemek kaşığı",
            standardUnitWeightGrams = 14f // 1 yemek kaşığı ~120 kcal
        ),
        NutritionItemDef(
            id = "butter",
            canonicalName = "Tereyağı",
            keywords = listOf("tereyağı", "tereyagi", "butter"),
            caloriesPer100g = 717f,
            proteinPer100g = 0.9f,
            carbsPer100g = 0.1f,
            fatPer100g = 81f,
            standardUnit = "tatlı kaşığı",
            standardUnitWeightGrams = 10f
        ),
        NutritionItemDef(
            id = "almonds",
            canonicalName = "Çiğ Badem",
            keywords = listOf("badem", "çiğ badem", "cig badem", "almonds"),
            caloriesPer100g = 579f,
            proteinPer100g = 21.2f,
            carbsPer100g = 21.6f,
            fatPer100g = 49.9f,
            fiberPer100g = 12.5f,
            standardUnit = "avuç",
            standardUnitWeightGrams = 30f
        ),
        NutritionItemDef(
            id = "walnuts",
            canonicalName = "Ceviz",
            keywords = listOf("ceviz", "ceviz içi", "walnuts"),
            caloriesPer100g = 654f,
            proteinPer100g = 15.2f,
            carbsPer100g = 13.7f,
            fatPer100g = 65.2f,
            fiberPer100g = 6.7f,
            standardUnit = "avuç",
            standardUnitWeightGrams = 30f
        ),

        // Popüler Yiyecekler ve İçecekler
        NutritionItemDef(
            id = "pizza_slice",
            canonicalName = "Pizza Dilimi",
            keywords = listOf("pizza", "dilim pizza", "pizza dilimi", "karışık pizza"),
            caloriesPer100g = 266f,
            proteinPer100g = 11.4f,
            carbsPer100g = 33.3f,
            fatPer100g = 10.1f,
            fiberPer100g = 2.3f,
            standardUnit = "dilim",
            standardUnitWeightGrams = 107f // 1 dilim ~285 kcal
        ),
        NutritionItemDef(
            id = "burger_beef",
            canonicalName = "Hamburger",
            keywords = listOf("hamburger", "burger", "cheeseburger"),
            caloriesPer100g = 250f,
            proteinPer100g = 14f,
            carbsPer100g = 24f,
            fatPer100g = 11f,
            standardUnit = "adet",
            standardUnitWeightGrams = 200f // ~500 kcal
        ),
        NutritionItemDef(
            id = "black_coffee",
            canonicalName = "Filtre Kahve / Sade Kahve",
            keywords = listOf("kahve", "filtre kahve", "türk kahvesi", "turk kahvesi", "sade kahve", "espresso", "americano"),
            caloriesPer100g = 2f,
            proteinPer100g = 0.3f,
            carbsPer100g = 0f,
            fatPer100g = 0f,
            standardUnit = "fincan",
            standardUnitWeightGrams = 150f
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
        val tokens = clean.split(Regex("[^a-zA-ZçğıöşüÇĞİÖŞÜ]+")).filter { it.length > 2 }
        var bestItem: NutritionItemDef? = null
        var bestScore = 0
        for (item in items) {
            var score = 0
            for (kw in item.keywords) {
                val kwTokens = kw.split(Regex("[^a-zA-ZçğıöşüÇĞİÖŞÜ]+")).filter { it.length > 2 }
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
