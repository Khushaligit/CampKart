package com.example.campkart.viewmodel

import androidx.lifecycle.ViewModel
import android.content.Context
import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.example.campkart.repo.RegistrationRepo
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

    private val repo = RegistrationRepo()

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
                delay(1200)

                val success =
                    useremail.isNotBlank() && password.isNotBlank() && contact.length >= 10

                if (success) {

                    repo.registerUser(
                        campName,
                        useremail,
                        password,
                        contact,
                        context,
                        onSuccess = {

                            campName = ""
                            useremail = ""
                            password = ""
                            contact = ""

                            _uiState.value = RegistrationUiState.Success
                        },
                        onFailure = {
                            _uiState.value =
                                RegistrationUiState.Error("Registration Failed")
                        }
                    )

                } else {
                    _uiState.value =
                        RegistrationUiState.Error("Please fill all fields correctly.")
                }

            } catch (e: Exception) {
                _uiState.value =
                    RegistrationUiState.Error(e.message ?: "Registration failed.")
            }
        }
    }

    fun resetState() {
        _uiState.value = RegistrationUiState.Idle
    }
}