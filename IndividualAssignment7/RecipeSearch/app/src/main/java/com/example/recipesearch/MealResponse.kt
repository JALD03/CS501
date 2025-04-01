package com.example.recipesearch

import com.squareup.moshi.Json

//Based off of the forecastcompose but made to handle a list
data class MealResponse(
    @Json(name = "meals") val mealList: List<Meal>?
)

data class Meal(
    @Json(name = "idMeal") val id: String,
    @Json(name = "strMeal") val name: String,
    @Json(name = "strMealThumb") val imageUrl: String?,
    @Json(name = "strInstructions") val instructions: String?
)

