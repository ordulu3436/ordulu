package com.example.data.parser

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class GeminiParsedItem(
    val foodName: String,
    val portionDescription: String,
    val calories: Int,
    val proteinGrams: Float,
    val carbsGrams: Float,
    val fatGrams: Float,
    val fiberGrams: Float = 0f
)

data class GeminiNutritionResponse(
    val items: List<GeminiParsedItem>,
    val totalCalories: Int,
    val totalProtein: Float,
    val totalCarbs: Float,
    val totalFat: Float,
    val totalFiber: Float,
    val summaryTitle: String
)

class GeminiNutritionService {

    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .build()

    suspend fun parseNutritionText(rawText: String): GeminiNutritionResponse? = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            Log.d("GeminiNutritionService", "No real Gemini API key configured, falling back to smart local engine")
            return@withContext null
        }

        val prompt = """
            You are a certified nutrition analysis engine.
            Analyze the following natural language food description:
            "$rawText"

            Extract every item, determine quantity, weight or serving, and compute precise macronutrients and calories.
            Return STRICT JSON conforming to this schema:
            {
              "summaryTitle": "Short display title of meal/food",
              "totalCalories": 350,
              "totalProtein": 32.5,
              "totalCarbs": 24.0,
              "totalFat": 11.2,
              "totalFiber": 3.0,
              "items": [
                {
                  "foodName": "Chicken Breast",
                  "portionDescription": "200g",
                  "calories": 330,
                  "proteinGrams": 62.0,
                  "carbsGrams": 0.0,
                  "fatGrams": 7.2,
                  "fiberGrams": 0.0
                }
              ]
            }
        """.trimIndent()

        val jsonBody = JSONObject().apply {
            val contentsArray = JSONArray().apply {
                put(JSONObject().apply {
                    val partsArray = JSONArray().apply {
                        put(JSONObject().put("text", prompt))
                    }
                    put("parts", partsArray)
                })
            }
            put("contents", contentsArray)

            val generationConfig = JSONObject().apply {
                put("responseMimeType", "application/json")
                put("temperature", 0.2)
            }
            put("generationConfig", generationConfig)
        }

        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = jsonBody.toString().toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: return@withContext null
            if (!response.isSuccessful) {
                Log.w("GeminiNutritionService", "Gemini HTTP error ${response.code}: $responseBody")
                return@withContext null
            }

            val rootJson = JSONObject(responseBody)
            val candidates = rootJson.optJSONArray("candidates") ?: return@withContext null
            if (candidates.length() == 0) return@withContext null
            val content = candidates.getJSONObject(0).optJSONObject("content") ?: return@withContext null
            val parts = content.optJSONArray("parts") ?: return@withContext null
            if (parts.length() == 0) return@withContext null
            val textContent = parts.getJSONObject(0).optString("text") ?: return@withContext null

            val parsedJson = JSONObject(textContent)
            val summaryTitle = parsedJson.optString("summaryTitle", "Food Log")
            val totalCalories = parsedJson.optInt("totalCalories", 0)
            val totalProtein = parsedJson.optDouble("totalProtein", 0.0).toFloat()
            val totalCarbs = parsedJson.optDouble("totalCarbs", 0.0).toFloat()
            val totalFat = parsedJson.optDouble("totalFat", 0.0).toFloat()
            val totalFiber = parsedJson.optDouble("totalFiber", 0.0).toFloat()

            val itemsList = mutableListOf<GeminiParsedItem>()
            val itemsJsonArray = parsedJson.optJSONArray("items")
            if (itemsJsonArray != null) {
                for (i in 0 until itemsJsonArray.length()) {
                    val itemObj = itemsJsonArray.getJSONObject(i)
                    itemsList.add(
                        GeminiParsedItem(
                            foodName = itemObj.optString("foodName", "Unknown Food"),
                            portionDescription = itemObj.optString("portionDescription", "1 serving"),
                            calories = itemObj.optInt("calories", 0),
                            proteinGrams = itemObj.optDouble("proteinGrams", 0.0).toFloat(),
                            carbsGrams = itemObj.optDouble("carbsGrams", 0.0).toFloat(),
                            fatGrams = itemObj.optDouble("fatGrams", 0.0).toFloat(),
                            fiberGrams = itemObj.optDouble("fiberGrams", 0.0).toFloat()
                        )
                    )
                }
            }

            GeminiNutritionResponse(
                items = itemsList,
                totalCalories = totalCalories,
                totalProtein = totalProtein,
                totalCarbs = totalCarbs,
                totalFat = totalFat,
                totalFiber = totalFiber,
                summaryTitle = summaryTitle
            )
        } catch (e: Exception) {
            Log.e("GeminiNutritionService", "Gemini call failed", e)
            null
        }
    }
}
