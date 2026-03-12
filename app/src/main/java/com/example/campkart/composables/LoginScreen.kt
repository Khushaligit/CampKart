package com.example.campkart.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.campkart.R
import com.example.campkart.viewmodel.LoginVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {

    val vm: LoginVM = viewModel()
    val ls by vm.loginState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = { TopAppBarContent(navController) },
        containerColor = Color.Transparent // allow background to show
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            /* ---------- Background Image ---------- */
            Image(
                painter = painterResource(id = R.drawable.designer_bg),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            /* ---------- Gradient Overlay ---------- */
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0x66000000),
                                Color(0x22000000),
                                Color(0x66000000)
                            )
                        )
                    )
            )

            /* ---------- Foreground Card ---------- */
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    elevation = CardDefaults.cardElevation(6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xCCFFFFFF)
                    )
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Image(
                            painter = painterResource(R.drawable.logo1),
                            contentDescription = null,
                            modifier = Modifier.size(90.dp)
                        )

                        Text(
                            text = "CampKart",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        Text(
                            text = "Welcome back",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF5E5E5E),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        /* ---------- Email ---------- */
                        OutlinedTextField(
                            value = ls.userId,
                            onValueChange = { vm.onEmailChange(it) },
                            label = { Text("Email") },
                            leadingIcon = {
                                Icon(Icons.Outlined.Email, contentDescription = "Email")
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(12.dp))

                        /* ---------- Password ---------- */
                        OutlinedTextField(
                            value = ls.userPassword,
                            onValueChange = { vm.onPasswordChange(it) },
                            label = { Text("Password") },
                            leadingIcon = {
                                Icon(Icons.Outlined.Lock, contentDescription = "Password")
                            },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(24.dp))

                        /* ---------- Login Button ---------- */
                        OutlinedButton(
                            onClick = {
                                vm.login(
                                    onUserLogin = {
                                        navController.navigate("campkarthomescreen")
                                    },
                                    context = context
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Text("Login")
                        }

                        Spacer(Modifier.height(20.dp))

                        /* ---------- Bottom Actions ---------- */
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Sign Up",
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.clickable {
                                    navController.navigate("signupscreen")
                                }
                            )

                            Text(
                                text = "Forgot Password",
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}
