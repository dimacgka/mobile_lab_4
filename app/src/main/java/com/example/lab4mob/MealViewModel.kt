package com.example.lab4mob

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory

data class MealState(
    val meals: List<MealItem> = emptyList(),
    val selectedMeal: MealItem? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class MealViewModel : ViewModel() {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.themealdb.com/api/json/v1/1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(MealApi::class.java)

    private val _state = MutableStateFlow(MealState())
    val state = _state.asStateFlow()

    init {
        loadMeals()
    }

    private fun loadMeals() {
        viewModelScope.launch {
            _state.value = MealState(isLoading = true)
            try {
                val response = api.getMealsByFirstLetter("b").await()
                _state.value = MealState(meals = response.meals)
            } catch (e: Exception) {
                _state.value = MealState(error = e.message)
            }
        }
    }

    fun loadMealDetails(mealId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, selectedMeal = null)
            try {
                val response = api.getMealById(mealId).await()
                val meal = response.meals.firstOrNull()
                _state.value = _state.value.copy(selectedMeal = meal, isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message, isLoading = false)
            }
        }
    }
}
