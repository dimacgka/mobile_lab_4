package com.example.mobil4lab

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
    @GET("search.php")
    suspend fun getMealsByFirstLetter(@Query("f") letter: String): MealResponse

    @GET("lookup.php")
    suspend fun getMealById(@Query("i") id: String): MealResponse
}

data class MealResponse(val meals: List<MealItem>)

data class MealItem(
    val idMeal: String,
    val strMeal: String,
    val strArea: String?,
    val strMealThumb: String?,
    val strInstructions: String?,
    val strCategory: String?,
    val strIngredient1: String?,
    val strIngredient2: String?,
    val strIngredient3: String?,
    val strIngredient4: String?,
    val strIngredient5: String?,
    val strIngredient6: String?,
    val strIngredient7: String?,
    val strIngredient8: String?,
    val strIngredient9: String?,
    val strIngredient10: String?,
    val strIngredient11: String?,
    val strIngredient12: String?,
    val strIngredient13: String?,
    val strIngredient14: String?,
    val strIngredient15: String?,
    val strIngredient16: String?,
    val strIngredient17: String?,
    val strIngredient18: String?,
    val strIngredient19: String?,
    val strIngredient20: String?,
    val strMeasure1: String?,
    val strMeasure2: String?,
    val strMeasure3: String?,
    val strMeasure4: String?,
    val strMeasure5: String?,
    val strMeasure6: String?,
    val strMeasure7: String?,
    val strMeasure8: String?,
    val strMeasure9: String?,
    val strMeasure10: String?,
    val strMeasure11: String?,
    val strMeasure12: String?,
    val strMeasure13: String?,
    val strMeasure14: String?,
    val strMeasure15: String?,
    val strMeasure16: String?,
    val strMeasure17: String?,
    val strMeasure18: String?,
    val strMeasure19: String?,
    val strMeasure20: String?
){
    fun getIngredientsWithMeasures(): List<Pair<String?, String?>> {
        return listOf(
            strIngredient1 to strMeasure1,
            strIngredient2 to strMeasure2,
            strIngredient3 to strMeasure3,
            strIngredient4 to strMeasure4,
            strIngredient5 to strMeasure5,
            strIngredient6 to strMeasure6,
            strIngredient7 to strMeasure7,
            strIngredient8 to strMeasure8,
            strIngredient9 to strMeasure9,
            strIngredient10 to strMeasure10,
            strIngredient11 to strMeasure11,
            strIngredient12 to strMeasure12,
            strIngredient13 to strMeasure13,
            strIngredient14 to strMeasure14,
            strIngredient15 to strMeasure15,
            strIngredient16 to strMeasure16,
            strIngredient17 to strMeasure17,
            strIngredient18 to strMeasure18,
            strIngredient19 to strMeasure19,
            strIngredient20 to strMeasure20
        ).filter { (ingredient, measure) ->
            !ingredient.isNullOrBlank() && !measure.isNullOrBlank()
        }.map { (ingredient, measure) ->
            ingredient to measure
        }
    }
}

class RecipeViewModel : ViewModel() {
    val recipes = MutableLiveData<List<MealItem>>()
    val recipeDetails = MutableLiveData<MealItem?>()
    val cookingSteps = MutableLiveData<List<String>>()

    private val api = Retrofit.Builder()
        .baseUrl("https://www.themealdb.com/api/json/v1/1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MealApi::class.java)

    fun loadRecipes() {
        viewModelScope.launch {
            try {
                val response = api.getMealsByFirstLetter("b")
                recipes.value = response.meals
            } catch (e: Exception) {
                recipes.value = emptyList()
            }
        }
    }

    fun loadRecipeDetails(recipeId: String) {
        viewModelScope.launch {
            try {
                val response = api.getMealById(recipeId)
                recipeDetails.value = response.meals.firstOrNull()
            } catch (e: Exception) {
                recipeDetails.value = null
            }
        }
    }

    fun loadCookingSteps(recipeId: String) {
        viewModelScope.launch {
            try {
                val response = api.getMealById(recipeId)
                val meal = response.meals.firstOrNull()
                cookingSteps.value = meal?.strInstructions?.split("\n")?.filter { it.isNotBlank() } ?: emptyList()
            } catch (e: Exception) {
                cookingSteps.value = listOf("Error loading steps")
            }
        }
    }
}