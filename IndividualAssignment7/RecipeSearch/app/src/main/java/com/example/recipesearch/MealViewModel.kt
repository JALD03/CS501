package com.example.recipesearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.recipesearch.MealCollection
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart

class MealViewModel(private val collection: MealCollection): ViewModel() {
    private val _mealState = MutableStateFlow<MealState>(MealState.Initial)
    val mealState: StateFlow<MealState> = _mealState

    //Based on fetch weather but used AI and documentation to figure out how to handle the mealDBs form of
    // responses
    fun searchMeals(query: String) {
        viewModelScope.launch {
            collection.searchMeals(query)
                .onStart { _mealState.value = MealState.Loading }
                .catch { e -> _mealState.value = MealState.Error(e.message ?: "Unknown error") }
                .collect { result ->
                    if (result is Result.Success) {
                        _mealState.value = MealState.Success(result.data)
                    } else if (result is Result.Error) {
                        _mealState.value = MealState.Error(result.message)
                    } else {
                        _mealState.value = MealState.Initial
                    }
                }
        }
    }
    sealed class MealState {
        object Initial : MealState()
        object Loading : MealState()
        data class Success(val meals: List<Meal>) : MealState()
        data class Error(val errorMessage: String) : MealState()
    }
}




