package com.example.campkart.repo

import android.util.Log
import com.example.campkart.model.Product
import com.google.firebase.database.*

class ListProductsRepo {

    private val database = FirebaseDatabase.getInstance()
        .getReference("products")

    fun fetchProducts(onResult: (List<Product>) -> Unit) {

        database.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val products = mutableListOf<Product>()

                for (data in snapshot.children) {

                    val report = data.getValue(Product::class.java)

                    Log.d("fbd", report.toString())

                    report?.let {
                        products.add(it)
                    }
                }

                onResult(products)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}