package com.example.campkart.viewmodel

import androidx.lifecycle.ViewModel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.example.campkart.model.Users


class RegistrationVM : ViewModel(){
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val usersRef = database.getReference("users")

    var campName by mutableStateOf("")
    var useremail by mutableStateOf("")
    var password by mutableStateOf("")

    var contact by mutableStateOf("user")

    fun registerUser(context: Context) {

        auth.createUserWithEmailAndPassword(useremail, password)
            .addOnSuccessListener {

                val id = auth.currentUser!!.uid

                val user = Users(
                    id = id,
                    campusName = campName,
                    userId = useremail,
                    userContact = contact
                )

                usersRef.child(id).setValue(user)
                    .addOnSuccessListener {

                        Toast.makeText(
                            context,
                            "Account Created Successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        campName = ""
                        useremail = ""
                        password = ""
                    }
                    .addOnFailureListener {

                        Toast.makeText(
                            context,
                            "Failed to save user data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
            .addOnFailureListener {

                Toast.makeText(
                    context,
                    "Registration Failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }


}