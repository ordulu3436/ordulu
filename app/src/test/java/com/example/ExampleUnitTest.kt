package com.example

import com.example.data.local.UserProfileEntity
import com.example.data.parser.NutritionParserEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testNutritionParserSingleItem() {
        val parser = NutritionParserEngine()
        val result = parser.parseWithRuleEngine("200g chicken breast")

        assertEquals("Chicken Breast", result.suggestedName)
        assertEquals(330, result.totalCalories)
        assertEquals(62.0f, result.totalProtein, 0.5f)
        assertEquals(0.0f, result.totalCarbs, 0.5f)
        assertEquals(7.2f, result.totalFat, 0.5f)
    }

    @Test
    fun testNutritionParserCompositeMeal() {
        val parser = NutritionParserEngine()
        val result = parser.parseWithRuleEngine("2 eggs and 1 slice whole wheat bread")

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
