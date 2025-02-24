package com.example.multipaneshop

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import com.example.multipaneshop.ProductList.products
import androidx.compose.ui.unit.sp

@Composable
fun ProductDescriptionScreen (product: Product?) {
    if (product == null) {
        Text( text = "No product selected yo!", modifier = Modifier.fillMaxWidth())
    } else {
        Column(){
            Text( text = product.name, fontSize = 48.sp)
            Text( text = "Price is ${product.price} dollars !", fontSize = 24.sp)
            Text( text = product.description, fontSize = 16.sp)
        }
    }

}