package com.example.multipaneshop

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import com.example.multipaneshop.ui.theme.MultiPaneShopTheme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import com.example.multipaneshop.ProductList.products
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalConfiguration


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MultiPaneShopTheme {
                MultiPaneShop()
                }
            }
        }
    }


@Composable
fun MultiPaneShop (){
    val config = LocalConfiguration.current
    var currentProduct by remember { mutableStateOf<Product?>(null) }
    var detailsOf by remember { mutableStateOf(false) }
    if (config.orientation == Configuration.ORIENTATION_LANDSCAPE){
        Row(modifier = Modifier.fillMaxWidth()){
            Box(Modifier.fillMaxWidth(0.5f)){
            ProductDisplayScreen { product -> currentProduct = product } }
            Box(Modifier.fillMaxWidth(0.5f)){
            ProductDescriptionScreen(product = currentProduct)}
        }
    } else {
        if (detailsOf) {
            Column(modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center){

                ProductDescriptionScreen(product = currentProduct)
                Button(onClick = { detailsOf = false }) { Text("Back to List") }

            }
        } else {
            ProductDisplayScreen { product -> currentProduct = product
                                    detailsOf = true }
        }
    }
}