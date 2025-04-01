package com.example.recipesearch

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory // Added import
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

//Pretty much the same as forecastcompose
interface ApiService {
        @GET("search.php")
        suspend fun searchMeals(@Query("s") query: String): MealResponse
}

object ApiClient {
    private const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/" // Base URL for the OpenWeather API
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory()) // Explicitly add KotlinJsonAdapterFactory
        .build() // Build the Moshi instance

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL) // Set the base URL
        .addConverterFactory(MoshiConverterFactory.create(moshi)) // Add the Moshi converter factory
        .build() // Build the Retrofit instance

    val apiService: ApiService = retrofit.create(ApiService::class.java) // Create an instance of the ApiService
}
