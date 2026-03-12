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
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.campkart.R
import com.example.campkart.viewmodel.RegistrationUiState
import com.example.campkart.viewmodel.RegistrationVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavController) {

    var modifier = Modifier
    val vm: RegistrationVM = viewModel()
    val context = LocalContext.current
    val uiState by vm.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    val campuses = listOf("Main Campus", "North Campus", "South Campus", "Tech Park")
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedCampus by rememberSaveable { mutableStateOf<String?>(null) }

    // -------- Scaffold remains the same; only visuals are added below --------
    Scaffold(
        topBar = { TopAppBarContent(navController) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Transparent // allow background image to show through
    ) { padding ->

        // React to registration result (unchanged)
        LaunchedEffect(uiState) {
            when (uiState) {
                is RegistrationUiState.Success -> {
                    vm.resetState()
                    navController.navigate("loginscreen") {
                        popUpTo("signupscreen") { inclusive = true }
                        launchSingleTop = true
                    }
                }
                is RegistrationUiState.Error -> {
                    snackbarHostState.showSnackbar((uiState as RegistrationUiState.Error).message)
                }
                else -> Unit
            }
        }

        // ---------- BEAUTIFIED LAYOUT STARTS HERE ----------
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 1) Background image (rename your file to drawable: designer_bg.png)
            Image(
                painter = painterResource(id = R.drawable.designer_bg),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // 2) Gradient overlay for readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0x66000000), // top: translucent dark
                                Color(0x22000000), // middle: lighter
                                Color(0x66000000)  // bottom: translucent dark
                            )
                        )
                    )
            )

            // 3) Foreground content on a Card
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    shape = RoundedCornerShape(18.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xCCFFFFFF) // slightly translucent white
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 20.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Spacer(Modifier.height(8.dp))

                        // Logo with slight transparency (kept as-is)
                        Image(
                            painter = painterResource(R.drawable.logo1),
                            contentDescription = null,
                            modifier = Modifier.size(90.dp)
                        )

                        Text(
                            text = "CampKart",
                            style = MaterialTheme.typography.headlineMedium
                                .copy(fontWeight = FontWeight.Bold),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, bottom = 4.dp)
                        )

                        // Optional subtitle for polish (purely visual)
                        Text(
                            text = "Create your account",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF5E5E5E),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            textAlign = TextAlign.Center
                        )

                        // ---------- FORM (FUNCTIONALITY UNCHANGED) ----------
                        OutlinedTextField(
                            value = vm.useremail,
                            onValueChange = { vm.useremail = it },
                            label = { Text("Email") },
                            leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = "Email") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(
                            value = vm.password,
                            onValueChange = { vm.password = it },
                            label = { Text("Password") },
                            leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = "Password") },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(
                            value = vm.contact,
                            onValueChange = { input -> vm.contact = input.filter { it.isDigit() } },
                            label = { Text("Phone Number") },
                            leadingIcon = { Icon(Icons.Outlined.Phone, contentDescription = "Phone") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(12.dp))

                        // Campus dropdown (unchanged, just inside Card)
                        var expanded by rememberSaveable { mutableStateOf(expanded) } // local reuse is fine
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = selectedCampus ?: "",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Select Campus") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                campuses.forEach { campus ->
                                    DropdownMenuItem(
                                        text = { Text(campus) },
                                        onClick = {
                                            selectedCampus = campus
                                            vm.campName = selectedCampus ?: ""
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(20.dp))

                        val isLoading = uiState is RegistrationUiState.Loading

                        OutlinedButton(
                            onClick = { vm.registerUser(context) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            enabled = !isLoading,
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp
                                )
                                Spacer(Modifier.width(8.dp))
                                Text("Signing Up…")
                            } else {
                                Text("Sign Up")
                            }
                        }

                        Spacer(Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                "Already have an account",
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.clickable { navController.navigate("loginscreen") }
                            )
                        }

                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }
        // ---------- BEAUTIFIED LAYOUT ENDS HERE ----------
    }
}
