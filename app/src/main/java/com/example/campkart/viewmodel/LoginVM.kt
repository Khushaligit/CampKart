package com.example.campkart.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.campkart.model.Login
import com.example.campkart.repo.LoginRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginVM : ViewModel() {

    private val repo = LoginRepo()

    private val _loginState = MutableStateFlow(Login(userId = ""))
    val loginState: StateFlow<Login> = _loginState


    fun onEmailChange(email: String) {
        _loginState.value = _loginState.value.copy(userId = email)
    }

    fun onPasswordChange(password: String) {
        _loginState.value = _loginState.value.copy(userPassword = password)
    }


    fun login(onUserLogin: () -> Unit, context: Context) {

        val email = _loginState.value.userId
        val password = _loginState.value.userPassword

        repo.loginUser(
            email,
            password,
            onUserLogin,
            context
        )
    }
}



