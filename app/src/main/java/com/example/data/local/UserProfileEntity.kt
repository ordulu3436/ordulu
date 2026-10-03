package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "Alex Rivera",
    val gender: String = "Male", // "Male", "Female", "Other"
    val age: Int = 28,
    val heightCm: Float = 178f,
    val weightKg: Float = 75f,
    val activityLevel: String = "MODERATE", // SEDENTARY, LIGHT, MODERATE, ACTIVE, VERY_ACTIVE
    val goalType: String = "MAINTAIN", // LOSE_FAST, LOSE_MILD, MAINTAIN, GAIN_MILD, GAIN_FAST
    val dailyCalorieTarget: Int = 2250,
    val proteinGramsTarget: Int = 160,
    val carbsGramsTarget: Int = 230,
    val fatGramsTarget: Int = 65,
    val waterMlTarget: Int = 2500,
    val isSetupComplete: Boolean = true
) {
    /**
     * Calculates Basal Metabolic Rate (BMR) using the Mifflin-St Jeor formula
     */
    fun calculateBmr(): Int {
        val s = if (gender.equals("Female", ignoreCase = true)) -161 else 5
        val bmr = (10f * weightKg) + (6.25f * heightCm) - (5f * age) + s
        return bmr.toInt().coerceAtLeast(1000)
    }

    /**
     * Calculates Total Daily Energy Expenditure (TDEE) based on activity level
     */
    fun calculateTdee(): Int {
        val multiplier = when (activityLevel.uppercase()) {
            "SEDENTARY" -> 1.2f
            "LIGHT" -> 1.375f
            "MODERATE" -> 1.55f
            "ACTIVE" -> 1.725f
            "VERY_ACTIVE" -> 1.9f
            else -> 1.55f
        }
        return (calculateBmr() * multiplier).toInt()
    }

    /**
     * Calculates recommended calorie target based on goal
     */
    fun calculateRecommendedCalories(): Int {
        val tdee = calculateTdee()
        return when (goalType.uppercase()) {
            "LOSE_FAST" -> (tdee - 500).coerceAtLeast(1200)
            "LOSE_MILD" -> (tdee - 250).coerceAtLeast(1200)
            "MAINTAIN" -> tdee
            "GAIN_MILD" -> tdee + 250
            "GAIN_FAST" -> tdee + 500
            else -> tdee
        }
    }
}
