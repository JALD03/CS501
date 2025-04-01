package com.example.recipesearch

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MealCollection (private val api: ApiService){

    //I was very confused on how to handle a list from an API so I used the documentation and AI
    //and we use this to search within the search of the ViewModel, it is sort of like a wrapper
    // and this is what does the actual searching
     fun searchMeals(query: String): Flow<Result<List<Meal>>> = flow {
            emit(Result.Loading)
            try {
                val response = api.searchMeals(query)
                if (response.mealList != null) {
                    emit(Result.Success(response.mealList))
                } else {
                    emit(Result.Error("No results found"))
                }
            } catch (e: Exception) {
                emit(Result.Error(e.message ?: "Unknown error"))
            }
        }
    }sealed class Result<out T> {
        object Loading : Result<Nothing>()
        data class Success<T>(val data: T) : Result<T>()
        data class Error(val message: String) : Result<Nothing>()
    }

