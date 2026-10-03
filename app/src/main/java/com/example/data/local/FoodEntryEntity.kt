package com.example.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "food_entries",
    indices = [
        Index(value = ["dateString"]),
        Index(value = ["dateString", "hourOfDay"]),
        Index(value = ["timestampMillis"])
    ]
)
data class FoodEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val rawInputText: String,
    val foodName: String,
    val portionDesc: String,
    val calories: Int,
    val proteinGrams: Float,
    val carbsGrams: Float,
    val fatGrams: Float,
    val fiberGrams: Float = 0f,
    val mealType: String, // "BREAKFAST", "LUNCH", "DINNER", "SNACK"
    val timestampMillis: Long,
    val dateString: String, // "yyyy-MM-dd"
    val hourOfDay: Int, // 0..23
    val parsedByAi: Boolean = false,
    val notes: String? = null
)
