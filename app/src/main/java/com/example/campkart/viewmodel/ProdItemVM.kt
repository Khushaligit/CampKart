package com.example.campkart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import com.example.campkart.model.Product
import com.example.campkart.repo.ProdItemRepo

class ProdItemVM : ViewModel() {

    private val repo = ProdItemRepo()

    var product = mutableStateOf<Product?>(null)
        private set

    fun fetchProduct(productId: String) {

        repo.fetchProduct(productId) { fetchedProduct ->

            product.value = fetchedProduct
        }
    }
}