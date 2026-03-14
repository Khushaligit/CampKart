package com.example.campkart.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.campkart.model.Login
import com.example.campkart.model.Users
import com.example.campkart.repo.LoginRepo
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginVM : ViewModel() {

    private val repo = LoginRepo()
    private val auth = FirebaseAuth.getInstance()

    private val _loginState = MutableStateFlow(Login(userId = ""))
    val loginState: StateFlow<Login> = _loginState

    private val _userDetails = MutableStateFlow<Users?>(null)
    val userDetails: StateFlow<Users?> = _userDetails

    private val _sellerDetails = MutableStateFlow<Users?>(null)
    val sellerDetails: StateFlow<Users?> = _sellerDetails


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

    fun fetchUserDetails() {
        repo.fetchUserDetails { user ->
            _userDetails.value = user
        }
    }

    fun fetchSellerDetails(uid: String) {
        repo.fetchUserDetailsById(uid) { user ->
            _sellerDetails.value = user
        }
    }

    fun logout() {
        auth.signOut()
    }
}
