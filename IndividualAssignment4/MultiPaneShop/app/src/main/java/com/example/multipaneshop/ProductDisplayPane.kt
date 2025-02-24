package com.example.multipaneshop

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import com.example.multipaneshop.ProductList.products

@Composable
fun ProductDisplayScreen(onSelected: (Product) -> Unit){
    LazyColumn (modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally){
        items(products) { product ->
            Text(text = product.name,
                modifier = Modifier.fillMaxSize().clickable{ onSelected(product) },
                fontSize = 50.sp
                )

        }
    }
}