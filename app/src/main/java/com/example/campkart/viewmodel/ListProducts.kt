package com.example.campkart.viewmodel

import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.compose.runtime.mutableStateListOf

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.campkart.model.Product

class ListProducts: ViewModel() {

    private val database = FirebaseDatabase.getInstance()
        .getReference("products")


    var productList = mutableStateListOf<Product>()
        private set

    init {
        fetchProducts()
    }

    private fun fetchProducts() {

        database.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                productList.clear()

                for (data in snapshot.children) {

                    val report = data.getValue(Product::class.java)
                    Log.d("fbd",report.toString())
                    report?.let {
                        productList.add(it)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}