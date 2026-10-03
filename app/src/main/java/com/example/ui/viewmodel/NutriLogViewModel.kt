package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.FoodEntryEntity
import com.example.data.local.NutriLogDatabase
import com.example.data.local.UserProfileEntity
import com.example.data.local.WaterEntryEntity
import com.example.data.parser.NutritionParserEngine
import com.example.data.parser.ParsedNutritionResult
import com.example.data.repository.NutritionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

data class DayCalorieSummary(
    val dateString: String,
    val dayLabel: String,
    val calories: Int,
    val target: Int,
    val isToday: Boolean
)

data class DashboardUiState(
    val selectedDate: String = NutritionRepository.getTodayDateString(),
    val displayDate: String = "Today",
    val profile: UserProfileEntity = UserProfileEntity(),
    val entries: List<FoodEntryEntity> = emptyList(),
    val waterEntries: List<WaterEntryEntity> = emptyList(),
    val totalCalories: Int = 0,
    val targetCalories: Int = 2250,
    val remainingCalories: Int = 2250,
    val totalProtein: Float = 0f,
    val totalCarbs: Float = 0f,
    val totalFat: Float = 0f,
    val totalFiber: Float = 0f,
    val totalWaterMl: Int = 0,
    val hourlyCalories: Map<Int, Int> = emptyMap(), // Hour 0..23 -> calories
    val breakfastEntries: List<FoodEntryEntity> = emptyList(),
    val lunchEntries: List<FoodEntryEntity> = emptyList(),
    val dinnerEntries: List<FoodEntryEntity> = emptyList(),
    val snackEntries: List<FoodEntryEntity> = emptyList()
)

data class LogSheetState(
    val isOpen: Boolean = false,
    val inputText: String = "",
    val mealType: String = "LUNCH",
    val timestampMillis: Long = System.currentTimeMillis(),
    val hourOfDay: Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
    val isParsing: Boolean = false,
    val parsedResult: ParsedNutritionResult? = null,
    val foodName: String = "",
    val portionDesc: String = "",
    val calories: String = "",
    val protein: String = "",
    val carbs: String = "",
    val fat: String = "",
    val fiber: String = "",
    val parsingStatusMessage: String? = null
)

class NutriLogViewModel(application: Application) : AndroidViewModel(application) {

    private val db = NutriLogDatabase.getInstance(application)
    private val repository = NutritionRepository(
        foodDao = db.foodEntryDao(),
        profileDao = db.userProfileDao(),
        waterDao = db.waterEntryDao(),
        parserEngine = NutritionParserEngine()
    )

    private val _selectedDate = MutableStateFlow(NutritionRepository.getTodayDateString())
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    private val _logSheetState = MutableStateFlow(LogSheetState())
    val logSheetState: StateFlow<LogSheetState> = _logSheetState.asStateFlow()

    val userProfile: StateFlow<UserProfileEntity> = repository.userProfile
        .combine(MutableStateFlow(UserProfileEntity())) { profile, default ->
            profile ?: default
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserProfileEntity()
        )

    val allLoggedEntries: StateFlow<List<FoodEntryEntity>> = repository.allFoodEntries
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val dashboardState: StateFlow<DashboardUiState> = _selectedDate
        .flatMapLatest { dateStr ->
            combine(
                repository.getFoodEntriesForDate(dateStr),
                repository.getWaterEntriesForDate(dateStr),
                userProfile
            ) { entries, waterList, profile ->
                val totalCals = entries.sumOf { it.calories }
                val target = profile.dailyCalorieTarget
                val remaining = target - totalCals

                val totalP = entries.sumOf { it.proteinGrams.toDouble() }.toFloat().roundTo1Decimal()
                val totalC = entries.sumOf { it.carbsGrams.toDouble() }.toFloat().roundTo1Decimal()
                val totalF = entries.sumOf { it.fatGrams.toDouble() }.toFloat().roundTo1Decimal()
                val totalFib = entries.sumOf { it.fiberGrams.toDouble() }.toFloat().roundTo1Decimal()

                val totalWater = waterList.sumOf { it.amountMl }

                val hourly = mutableMapOf<Int, Int>()
                for (h in 0..23) hourly[h] = 0
                for (e in entries) {
                    val h = e.hourOfDay.coerceIn(0, 23)
                    hourly[h] = (hourly[h] ?: 0) + e.calories
                }

                DashboardUiState(
                    selectedDate = dateStr,
                    displayDate = NutritionRepository.formatDateForDisplay(dateStr),
                    profile = profile,
                    entries = entries,
                    waterEntries = waterList,
                    totalCalories = totalCals,
                    targetCalories = target,
                    remainingCalories = remaining,
                    totalProtein = totalP,
                    totalCarbs = totalC,
                    totalFat = totalF,
                    totalFiber = totalFib,
                    totalWaterMl = totalWater,
                    hourlyCalories = hourly,
                    breakfastEntries = entries.filter { it.mealType.equals("BREAKFAST", ignoreCase = true) },
                    lunchEntries = entries.filter { it.mealType.equals("LUNCH", ignoreCase = true) },
                    dinnerEntries = entries.filter { it.mealType.equals("DINNER", ignoreCase = true) },
                    snackEntries = entries.filter { it.mealType.equals("SNACK", ignoreCase = true) }
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DashboardUiState()
        )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.ensureInitialDataSeeded()
        }
    }

    fun selectDate(dateStr: String) {
        _selectedDate.value = dateStr
    }

    fun navigateDateBy(days: Int) {
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val cal = Calendar.getInstance()
            val curDate = sdf.parse(_selectedDate.value) ?: Date()
            cal.time = curDate
            cal.add(Calendar.DAY_OF_YEAR, days)
            _selectedDate.value = sdf.format(cal.time)
        } catch (_: Exception) {}
    }

    fun resetToToday() {
        _selectedDate.value = NutritionRepository.getTodayDateString()
    }

    fun openLogSheet(defaultMealType: String? = null) {
        val nowCal = Calendar.getInstance()
        val hour = nowCal.get(Calendar.HOUR_OF_DAY)
        val determinedMeal = defaultMealType ?: when (hour) {
            in 5..10 -> "BREAKFAST"
            in 11..15 -> "LUNCH"
            in 16..20 -> "DINNER"
            else -> "SNACK"
        }
        _logSheetState.value = LogSheetState(
            isOpen = true,
            mealType = determinedMeal,
            timestampMillis = System.currentTimeMillis(),
            hourOfDay = hour
        )
    }

    fun closeLogSheet() {
        _logSheetState.value = LogSheetState(isOpen = false)
    }

    fun updateLogQuery(text: String) {
        _logSheetState.value = _logSheetState.value.copy(inputText = text)
    }

    fun updateMealType(meal: String) {
        _logSheetState.value = _logSheetState.value.copy(mealType = meal)
    }

    fun updateLogHour(hour: Int) {
        val cal = Calendar.getInstance()
        cal.timeInMillis = _logSheetState.value.timestampMillis
        cal.set(Calendar.HOUR_OF_DAY, hour)
        _logSheetState.value = _logSheetState.value.copy(
            hourOfDay = hour,
            timestampMillis = cal.timeInMillis
        )
    }

    fun updateEditedField(
        name: String? = null,
        calories: String? = null,
        protein: String? = null,
        carbs: String? = null,
        fat: String? = null,
        fiber: String? = null,
        portion: String? = null
    ) {
        val cur = _logSheetState.value
        _logSheetState.value = cur.copy(
            foodName = name ?: cur.foodName,
            calories = calories ?: cur.calories,
            protein = protein ?: cur.protein,
            carbs = carbs ?: cur.carbs,
            fat = fat ?: cur.fat,
            fiber = fiber ?: cur.fiber,
            portionDesc = portion ?: cur.portionDesc
        )
    }

    fun parseCurrentText() {
        val query = _logSheetState.value.inputText.trim()
        if (query.isBlank()) return

        _logSheetState.value = _logSheetState.value.copy(
            isParsing = true,
            parsingStatusMessage = "Analyzing ingredients & calculating macros..."
        )

        viewModelScope.launch {
            val result = repository.parseNutritionText(query)
            _logSheetState.value = _logSheetState.value.copy(
                isParsing = false,
                parsedResult = result,
                foodName = result.suggestedName,
                portionDesc = result.portionDescription,
                calories = result.totalCalories.toString(),
                protein = result.totalProtein.toString(),
                carbs = result.totalCarbs.toString(),
                fat = result.totalFat.toString(),
                fiber = result.totalFiber.toString(),
                parsingStatusMessage = null
            )
        }
    }

    fun commitFoodEntry() {
        val state = _logSheetState.value
        val cals = state.calories.toIntOrNull() ?: 0
        if (cals <= 0 && state.foodName.isBlank()) return

        val p = state.protein.toFloatOrNull() ?: 0f
        val c = state.carbs.toFloatOrNull() ?: 0f
        val f = state.fat.toFloatOrNull() ?: 0f
        val fib = state.fiber.toFloatOrNull() ?: 0f

        val entry = FoodEntryEntity(
            rawInputText = state.inputText.ifBlank { state.foodName },
            foodName = state.foodName.ifBlank { "Custom Meal" },
            portionDesc = state.portionDesc.ifBlank { "1 portion" },
            calories = cals,
            proteinGrams = p,
            carbsGrams = c,
            fatGrams = f,
            fiberGrams = fib,
            mealType = state.mealType,
            timestampMillis = state.timestampMillis,
            dateString = _selectedDate.value,
            hourOfDay = state.hourOfDay,
            parsedByAi = state.parsedResult?.engineUsed?.contains("AI", ignoreCase = true) == true
        )

        viewModelScope.launch(Dispatchers.IO) {
            repository.logFoodEntry(entry)
        }
        closeLogSheet()
    }

    fun deleteEntry(entry: FoodEntryEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFoodEntry(entry)
        }
    }

    fun logWater(amountMl: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.logWater(amountMl, _selectedDate.value)
        }
    }

    fun saveProfile(profile: UserProfileEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveProfile(profile)
        }
    }

    fun getPast7DaysSummaries(): List<DayCalorieSummary> {
        val entries = allLoggedEntries.value
        val profile = userProfile.value
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val dayLabelFmt = SimpleDateFormat("EEE", Locale.US)
        val todayStr = NutritionRepository.getTodayDateString()

        val summaries = mutableListOf<DayCalorieSummary>()
        val cal = Calendar.getInstance()

        for (i in 6 downTo 0) {
            val c = Calendar.getInstance()
            c.add(Calendar.DAY_OF_YEAR, -i)
            val dStr = sdf.format(c.time)
            val label = if (dStr == todayStr) "Today" else dayLabelFmt.format(c.time)
            val dayCals = entries.filter { it.dateString == dStr }.sumOf { it.calories }

            summaries.add(
                DayCalorieSummary(
                    dateString = dStr,
                    dayLabel = label,
                    calories = dayCals,
                    target = profile.dailyCalorieTarget,
                    isToday = (dStr == todayStr)
                )
            )
        }
        return summaries
    }

    private fun Float.roundTo1Decimal(): Float {
        return (this * 10f).roundToInt() / 10f
    }
}
