package com.example.campkart.viewmodel

import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.compose.runtime.mutableStateListOf

import com.example.campkart.model.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class UserProdList: ViewModel() {


    private val database = FirebaseDatabase.getInstance()
        .getReference("products")

    private val auth = FirebaseAuth.getInstance()

    var productList = mutableStateListOf<Product>()
        private set

    init {
        fetchProducts()
    }

    private fun fetchProducts() {

        val currentUserId = auth.currentUser?.uid ?: return

        val query = database
            .orderByChild("createdBy")
            .equalTo(currentUserId)

        query.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                productList.clear()

                for (data in snapshot.children) {

                    val product = data.getValue(Product::class.java)

                    product?.let {
                        productList.add(it)
                    }

                    Log.d("products", product.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("firebase", error.message)
            }
        })
    }
}