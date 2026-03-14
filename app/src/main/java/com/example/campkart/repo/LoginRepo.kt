package com.example.campkart.repo

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.campkart.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginRepo {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val usersRef = database.getReference("users")

    fun loginUser(
        email: String,
        password: String,
        onUserLogin: () -> Unit,
        context: Context
    ) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {

                val uid = auth.currentUser!!.uid

                usersRef.child(uid).get()
                    .addOnSuccessListener { userSnapshot ->

                        if (userSnapshot.exists()) {

                            Toast.makeText(context, "User Login", Toast.LENGTH_SHORT).show()
                            Log.d("TAG", "login success ")

                            onUserLogin()
                        }
                    }
            }

            .addOnFailureListener {

                Toast.makeText(context, "Login Failed..", Toast.LENGTH_SHORT).show()
                Log.d("TAG", "login: failed ")
            }
    }

    fun fetchUserDetails(onResult: (Users?) -> Unit) {
        val uid = auth.currentUser?.uid ?: return onResult(null)
        usersRef.child(uid).get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.getValue(Users::class.java)
                onResult(user)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }
}
