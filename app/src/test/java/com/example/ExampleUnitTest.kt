package com.example

import com.example.data.local.UserProfileEntity
import com.example.data.parser.NutritionParserEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testNutritionParserTurkishSingleItem() {
        val parser = NutritionParserEngine()
        val result = parser.parseWithRuleEngine("200g tavuk göğsü")

        assertEquals("Tavuk Göğsü", result.suggestedName)
        assertEquals(330, result.totalCalories)
        assertEquals(62.0f, result.totalProtein, 0.5f)
        assertEquals(0.0f, result.totalCarbs, 0.5f)
        assertEquals(7.2f, result.totalFat, 0.5f)
    }

    @Test
    fun testNutritionParserTurkishCompositeMeal() {
        val parser = NutritionParserEngine()
        val result = parser.parseWithRuleEngine("2 yumurta ve 1 dilim tam buğday ekmeği")

        assertTrue(result.items.size >= 2)
        assertTrue(result.totalCalories > 200)
        assertTrue(result.totalProtein > 12f)
    }

    @Test
    fun testBmrAndTdeeFormulas() {
        val profile = UserProfileEntity(
            gender = "Male",
            age = 28,
            heightCm = 178f,
            weightKg = 75f,
            activityLevel = "MODERATE",
            goalType = "MAINTAIN"
        )

        // Mifflin-St Jeor: (10 * 75) + (6.25 * 178) - (5 * 28) + 5
        // = 750 + 1112.5 - 140 + 5 = 1727.5 -> 1727
        val bmr = profile.calculateBmr()
        assertEquals(1727, bmr)

        // TDEE = 1727 * 1.55 = 2676
        val tdee = profile.calculateTdee()
        assertEquals(2676, tdee)
    }
}
