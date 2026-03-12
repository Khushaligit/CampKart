package com.example.campkart.composables



import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.campkart.R
import com.example.campkart.viewmodel.LoginVM


@Composable
fun LoginScreen(navController: NavController) {

    val modifier: Modifier = Modifier

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val vm: LoginVM= viewModel()
    val ls by vm.loginState.collectAsState()
    val context= LocalContext.current
    Scaffold(
        topBar = { TopAppBarContent(navController) }
    ) { padding ->
        Column(
            modifier = modifier

                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 20.dp) // page side padding
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(24.dp))


            Image(
                painter = painterResource(R.drawable.logo1),
                contentDescription = null,
                modifier.size(90.dp)
            )

            // Big title like the sketch
            Text(
                text = "CampKart",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Spacer(Modifier.height(16.dp))

            // Email field (green-ish look can be themed or colored via container/indicator if desired)
            OutlinedTextField(
                value = ls.userId,
                onValueChange = { vm.onEmailChange(it) },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = "Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            // Password field
            OutlinedTextField(
                value = ls.userPassword,
                onValueChange = { vm.onPasswordChange(it) },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = "Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))


            // Log In button (outlined + red stroke, like the sketch)
            OutlinedButton(
                onClick = { vm.login(onUserLogin = {navController.navigate("campkarthomescreen")},context)},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Login")
            }

            Spacer(Modifier.height(30.dp))

            // Sign Up button (outlined + red stroke)

            Row {
                Text("Sign Up", Modifier.clickable(enabled = true,
                    onClick ={navController.navigate("signupscreen")} ))

                Spacer(modifier.width(180.dp))

                Text("Forgot Password")


            }

        }
    }
}
