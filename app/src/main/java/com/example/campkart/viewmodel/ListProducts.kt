package com.example.campkart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import com.example.campkart.model.Product
import com.example.campkart.repo.ListProductsRepo

class ListProducts : ViewModel() {

    private val repo = ListProductsRepo()

    var productList = mutableStateListOf<Product>()
        private set

    init {
        fetchProducts()
    }

    private fun fetchProducts() {

        repo.fetchProducts { products ->

            productList.clear()

            productList.addAll(products)
        }
    }
}