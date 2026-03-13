package com.example.campkart.repo

import android.util.Log
import com.example.campkart.model.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class UserProdListRepo {

    private val database = FirebaseDatabase.getInstance()
        .getReference("products")

    private val auth = FirebaseAuth.getInstance()

    fun fetchProducts(onResult: (List<Product>) -> Unit) {

        val currentUserId = auth.currentUser?.uid ?: return

        val query = database
            .orderByChild("createdBy")
            .equalTo(currentUserId)

        query.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val products = mutableListOf<Product>()

                for (data in snapshot.children) {

                    val product = data.getValue(Product::class.java)

                    product?.let {
                        products.add(it)
                    }

                    Log.d("products", product.toString())
                }

                onResult(products)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("firebase", error.message)
            }
        })
    }
}