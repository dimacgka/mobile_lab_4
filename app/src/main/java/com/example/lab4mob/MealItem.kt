package com.example.lab4mob

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call

interface MealApi {
    @GET("search.php")
    fun getMealsByFirstLetter(@Query("f") letter: String): Call<MealResponse>

    @GET("lookup.php")
    fun getMealById(@Query("i") id: String): Call<MealResponse>
}


data class MealResponse(
    @SerializedName("meals") val meals: List<MealItem>
)

data class MealItem(
    @SerializedName("idMeal") val idMeal: String,
    @SerializedName("strMeal") val strMeal: String,
    @SerializedName("strArea") val strArea: String?,
    @SerializedName("strMealThumb") val strMealThumb: String?,
    @SerializedName("strInstructions") val strInstructions: String?,
    @SerializedName("strCategory") val strCategory: String?
)
