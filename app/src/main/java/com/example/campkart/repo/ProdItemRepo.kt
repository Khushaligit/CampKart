package com.example.campkart.repo

import android.util.Log
import com.example.campkart.model.Product
import com.google.firebase.database.*

class ProdItemRepo {

    private val database = FirebaseDatabase.getInstance()
        .getReference("products")

    fun fetchProduct(productId: String, onResult: (Product?) -> Unit) {

        database.child(productId)
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    val fetchedProduct = snapshot.getValue(Product::class.java)

                    Log.d("product", fetchedProduct.toString())

                    onResult(fetchedProduct)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("firebase", error.message)
                }
            })
    }
}