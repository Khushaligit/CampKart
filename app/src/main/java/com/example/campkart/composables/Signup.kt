package com.example.campkart.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
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




    Scaffold(
        topBar = { TopAppBarContent(navController) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        // 🔁 React to one-time navigation on success
        LaunchedEffect(uiState) {
            when (uiState) {
                is RegistrationUiState.Success -> {
                    // Optional: clear state to avoid re-trigger on recomposition
                    vm.resetState()
                    navController.navigate("loginscreen") {
                        popUpTo("signupscreen") { inclusive = true } // Prevent back to signup
                        launchSingleTop = true
                    }
                }
                is RegistrationUiState.Error -> {
                    snackbarHostState.showSnackbar((uiState as RegistrationUiState.Error).message)
                }
                else -> Unit
            }
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ... your existing UI (logo, title)


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

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                // Readonly text field that shows the selected campus
                OutlinedTextField(
                    value = selectedCampus ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select Campus") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
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
                                vm.campName = selectedCampus?:""
                                expanded = false
                            }
                        )
                    }
                }
            }



            // ... your campus dropdown

            Spacer(Modifier.height(24.dp))

            val isLoading = uiState is RegistrationUiState.Loading

            OutlinedButton(
                onClick = { vm.registerUser(context) },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                enabled = !isLoading
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

            Row(horizontalArrangement = Arrangement.Start) {
                Text(
                    "Already have an account",
                    modifier = Modifier.clickable { navController.navigate("loginscreen") }
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
