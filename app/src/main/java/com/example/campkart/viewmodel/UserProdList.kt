package com.example.campkart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import com.example.campkart.model.Product
import com.example.campkart.repo.UserProdListRepo

class UserProdList : ViewModel() {

    private val repo = UserProdListRepo()

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