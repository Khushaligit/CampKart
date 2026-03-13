package com.example.campkart.repo

import com.example.campkart.model.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class ProductsRepo(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
) {

    private val productsRef = database.getReference("products")

    suspend fun addProduct(product: Product) {

        val newRef = productsRef.child(product.prodId)

        newRef.setValue(product).await()
    }

    fun generateProductId(): String {

        val newRef = productsRef.push()

        return newRef.key
            ?: throw IllegalStateException("Failed to generate product id")
    }

    fun getCurrentUserId(): String {

        return auth.currentUser?.uid
            ?: throw IllegalStateException("User not authenticated")
    }
}