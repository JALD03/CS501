package com.example.recipesearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.recipesearch.*
import com.example.recipesearch.MealViewModel.MealState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiService = ApiClient.apiService
        val mealCollection = MealCollection(apiService)
        val mealViewModel = MealViewModel(mealCollection)

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MealSearchScreen(viewModel = mealViewModel)
                }
            }
        }
    }
}

@Composable
fun MealSearchScreen(viewModel: MealViewModel) {
    var query by remember { mutableStateOf("") }
    val mealState by viewModel.mealState.collectAsState()
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("Search here!") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { viewModel.searchMeals(query) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Press to search")
        }
        when (mealState) {
            is MealState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            is MealState.Success -> MealList((mealState as MealState.Success).meals)
            is MealState.Error -> Text("Error: ${(mealState as MealState.Error).errorMessage}")
            MealState.Initial -> Text("Type in an ingredient or meal to find it!")
        }
    }
}
@Composable
fun MealList(meals: List<Meal>) {
    LazyColumn {
        items(meals) { meal ->
            MealItem(meal)
        }
    }
}
@Composable
fun MealItem(meal: Meal) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
    ) {
        Row(modifier = Modifier.padding(4.dp)) {
            meal.imageUrl?.let {
                Image(
                    painter = rememberAsyncImagePainter(it), //I used Ai and documentation to figure out that I need to use coil for images
                    contentDescription = meal.name,
                    modifier = Modifier.size(100.dp)
                )
            }
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(meal.name)
                meal.instructions?.let { Text(it.take(200) + "...") } //here I set a maximum on character count
            }
        }
    }
}