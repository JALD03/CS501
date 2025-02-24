package com.example.multipaneshop

import androidx.compose.runtime.MutableState

data class Product(val name: String, val price: String, val description: String) :
        () -> MutableState<Product?> {
    override fun invoke(): MutableState<Product?> {
        TODO("Not yet implemented")
    }
}

object ProductList {
    val products = listOf(
        Product("Product 1", "$3", "This is a great product."),
        Product("Product 2", "$2000", "This is a product with more features."),
        Product("Product 3", "$1", "Premium product.")
    )
}