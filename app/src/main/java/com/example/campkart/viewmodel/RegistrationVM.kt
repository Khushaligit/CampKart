package com.example.campkart.viewmodel

import androidx.lifecycle.ViewModel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.example.campkart.model.Users
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


sealed class RegistrationUiState {
    object Idle : RegistrationUiState()
    object Loading : RegistrationUiState()
    object Success : RegistrationUiState()
    data class Error(val message: String) : RegistrationUiState()
}


class RegistrationVM : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val usersRef = database.getReference("users")

    var campName by mutableStateOf("")
    var useremail by mutableStateOf("")
    var password by mutableStateOf("")

    var contact by mutableStateOf("")

    private val _uiState = MutableStateFlow<RegistrationUiState>(RegistrationUiState.Idle)
    val uiState: StateFlow<RegistrationUiState> = _uiState


    fun registerUser(context: Context) {

        viewModelScope.launch {
            _uiState.value = RegistrationUiState.Loading
            try {
                // TODO: Replace with your actual registration logic
                // simulate API
                delay(1200)

                val success =
                    useremail.isNotBlank() && password.isNotBlank() && contact.length >= 10
                if (success) {
                    _uiState.value = RegistrationUiState.Success
                } else {
                    _uiState.value = RegistrationUiState.Error("Please fill all fields correctly.")
                }
            } catch (e: Exception) {
                _uiState.value = RegistrationUiState.Error(e.message ?: "Registration failed.")
            }

        }


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
                        contact = ""
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

    fun resetState() {
        _uiState.value = RegistrationUiState.Idle
    }

}