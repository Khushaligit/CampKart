package com.example.campkart.viewmodel


import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.example.campkart.model.Login
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
class LoginVM : ViewModel(){

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val adminsRef = database.getReference("admins")
    private val usersRef = database.getReference("users")
    private val _loginState = MutableStateFlow(Login(userId = ""))
    val loginState: StateFlow<Login> = _loginState


    fun onEmailChange(email: String) {
        _loginState.value = _loginState.value.copy(userId = email)
    }

    fun onPasswordChange(password: String) {
        _loginState.value = _loginState.value.copy(userPassword = password)
    }



    fun login(onAdminLogin: ()-> Unit, onUserLogin: ()-> Unit,context: Context) {

        val email = _loginState.value.userId
        val password = _loginState.value.userPassword

        auth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                val uid = auth.currentUser!!.uid
                adminsRef.child(uid).get()
                    .addOnSuccessListener { adminSnapshot->
                        if (adminSnapshot.exists()){
                            Toast.makeText(context, "Admin Login", Toast.LENGTH_SHORT).show()
                            onAdminLogin()
                        }
                        else{
                            usersRef.child(uid).get()
                                .addOnSuccessListener { userSnapshot->
                                    if (userSnapshot.exists()){
                                        Toast.makeText(context, "User Login", Toast.LENGTH_SHORT).show()
                                        onUserLogin()

                                    }
                                    else{
                                        Toast.makeText(context, "User Not Found", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }

                    }

            }
            .addOnFailureListener {
                Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
            }

    }
}

