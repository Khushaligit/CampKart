package com.example.campkart.repo

import android.content.Context
import android.widget.Toast
import com.example.campkart.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegistrationRepo {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val usersRef = database.getReference("users")

    fun registerUser(
        campName: String,
        useremail: String,
        password: String,
        contact: String,
        context: Context,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {

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

                        onSuccess()
                    }
                    .addOnFailureListener {

                        Toast.makeText(
                            context,
                            "Failed to save user data",
                            Toast.LENGTH_SHORT
                        ).show()

                        onFailure()
                    }
            }
            .addOnFailureListener {

                Toast.makeText(
                    context,
                    "Registration Failed",
                    Toast.LENGTH_SHORT
                ).show()

                onFailure()
            }
    }
}