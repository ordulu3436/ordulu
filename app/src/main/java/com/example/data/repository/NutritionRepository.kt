package com.example.data.repository

import com.example.data.local.FoodEntryDao
import com.example.data.local.FoodEntryEntity
import com.example.data.local.UserProfileDao
import com.example.data.local.UserProfileEntity
import com.example.data.local.WaterEntryDao
import com.example.data.local.WaterEntryEntity
import com.example.data.parser.NutritionParserEngine
import com.example.data.parser.ParsedNutritionResult
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class NutritionRepository(
    private val foodDao: FoodEntryDao,
    private val profileDao: UserProfileDao,
    private val waterDao: WaterEntryDao,
    private val parserEngine: NutritionParserEngine = NutritionParserEngine()
) {

    val userProfile: Flow<UserProfileEntity?> = profileDao.getUserProfile()
    val allFoodEntries: Flow<List<FoodEntryEntity>> = foodDao.getAllEntries()
    val recentEntries: Flow<List<FoodEntryEntity>> = foodDao.getRecentEntries(10)
    val loggedDates: Flow<List<String>> = foodDao.getLoggedDates()

    fun getFoodEntriesForDate(dateString: String): Flow<List<FoodEntryEntity>> {
        return foodDao.getEntriesForDate(dateString)
    }

    fun getWaterEntriesForDate(dateString: String): Flow<List<WaterEntryEntity>> {
        return waterDao.getWaterEntriesForDate(dateString)
    }

    suspend fun saveProfile(profile: UserProfileEntity) {
        profileDao.insertOrUpdateProfile(profile)
    }

    suspend fun logFoodEntry(entry: FoodEntryEntity): Long {
        return foodDao.insertEntry(entry)
    }

    suspend fun logWater(amountMl: Int, dateString: String, timestampMillis: Long = System.currentTimeMillis()) {
        waterDao.insertWater(
            WaterEntryEntity(
                amountMl = amountMl,
                timestampMillis = timestampMillis,
                dateString = dateString
            )
        )
    }

    suspend fun deleteFoodEntry(entry: FoodEntryEntity) {
        foodDao.deleteEntry(entry)
    }

    suspend fun deleteFoodEntryById(id: Long) {
        foodDao.deleteEntryById(id)
    }

    suspend fun deleteWaterEntryById(id: Long) {
        waterDao.deleteWaterById(id)
    }

    suspend fun parseNutritionText(rawText: String): ParsedNutritionResult {
        return parserEngine.parse(rawText)
    }

    suspend fun ensureInitialDataSeeded() {
        val existingProfile = profileDao.getProfileDirect()
        if (existingProfile == null) {
            val initialProfile = UserProfileEntity(
                id = 1,
                name = "Alex Rivera",
                gender = "Male",
                age = 28,
                heightCm = 178f,
                weightKg = 75f,
                activityLevel = "MODERATE",
                goalType = "MAINTAIN",
                dailyCalorieTarget = 2250,
                proteinGramsTarget = 160,
                carbsGramsTarget = 230,
                fatGramsTarget = 65,
                waterMlTarget = 2500,
                isSetupComplete = true
            )
            profileDao.insertOrUpdateProfile(initialProfile)

            // Seed realistic hourly meal logs for today to give immediate value
            val todayStr = getTodayDateString()
            val cal = Calendar.getInstance()

            // 1. Breakfast at 8:30 AM
            cal.set(Calendar.HOUR_OF_DAY, 8)
            cal.set(Calendar.MINUTE, 30)
            foodDao.insertEntry(
                FoodEntryEntity(
                    rawInputText = "2 boiled eggs and 1 slice whole wheat toast",
                    foodName = "Whole Egg & Whole Wheat Toast",
                    portionDesc = "2 eggs + 1 slice",
                    calories = 225,
                    proteinGrams = 17.5f,
                    carbsGrams = 14.0f,
                    fatGrams = 10.5f,
                    fiberGrams = 2.0f,
                    mealType = "BREAKFAST",
                    timestampMillis = cal.timeInMillis,
                    dateString = todayStr,
                    hourOfDay = 8,
                    parsedByAi = false
                )
            )

            // 2. Morning Coffee & Snack at 10:45 AM
            cal.set(Calendar.HOUR_OF_DAY, 10)
            cal.set(Calendar.MINUTE, 45)
            foodDao.insertEntry(
                FoodEntryEntity(
                    rawInputText = "1 medium banana and 1 tbsp peanut butter",
                    foodName = "Banana & Peanut Butter",
                    portionDesc = "1 fruit + 1 tbsp",
                    calories = 199,
                    proteinGrams = 5.1f,
                    carbsGrams = 30.8f,
                    fatGrams = 8.3f,
                    fiberGrams = 3.6f,
                    mealType = "SNACK",
                    timestampMillis = cal.timeInMillis,
                    dateString = todayStr,
                    hourOfDay = 10,
                    parsedByAi = false
                )
            )

            // 3. Lunch at 13:15 (1:15 PM)
            cal.set(Calendar.HOUR_OF_DAY, 13)
            cal.set(Calendar.MINUTE, 15)
            foodDao.insertEntry(
                FoodEntryEntity(
                    rawInputText = "200g chicken breast and 1 cup cooked brown rice",
                    foodName = "Chicken Breast & Cooked Brown Rice",
                    portionDesc = "200g + 1 cup",
                    calories = 548,
                    proteinGrams = 67.1f,
                    carbsGrams = 45.8f,
                    fatGrams = 9.0f,
                    fiberGrams = 3.5f,
                    mealType = "LUNCH",
                    timestampMillis = cal.timeInMillis,
                    dateString = todayStr,
                    hourOfDay = 13,
                    parsedByAi = false
                )
            )

            // Seed some water logs
            waterDao.insertWater(WaterEntryEntity(amountMl = 500, timestampMillis = System.currentTimeMillis() - 14000000, dateString = todayStr))
            waterDao.insertWater(WaterEntryEntity(amountMl = 500, timestampMillis = System.currentTimeMillis() - 7000000, dateString = todayStr))
            waterDao.insertWater(WaterEntryEntity(amountMl = 250, timestampMillis = System.currentTimeMillis() - 2000000, dateString = todayStr))
        }
    }

    companion object {
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        private val displayFormat = SimpleDateFormat("EEE, MMM d", Locale.US)
        private val timeFormat = SimpleDateFormat("h:mm a", Locale.US)

        fun getTodayDateString(): String = dateFormat.format(Date())

        fun formatDateForDisplay(dateStr: String): String {
            return try {
                val date = dateFormat.parse(dateStr) ?: return dateStr
                val today = dateFormat.format(Date())
                val cal = Calendar.getInstance()
                cal.add(Calendar.DAY_OF_YEAR, -1)
                val yesterday = dateFormat.format(cal.time)

                when (dateStr) {
                    today -> "Today (${displayFormat.format(date)})"
                    yesterday -> "Yesterday (${displayFormat.format(date)})"
                    else -> displayFormat.format(date)
                }
            } catch (_: Exception) {
                dateStr
            }
        }

        fun formatTimestampTime(millis: Long): String {
            return timeFormat.format(Date(millis))
        }
    }
}
