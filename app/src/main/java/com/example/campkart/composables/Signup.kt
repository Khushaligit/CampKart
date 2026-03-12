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
import com.example.campkart.viewmodel.RegistrationVM


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavController) {

    //signup

    val modifier: Modifier = Modifier
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }

    val campuses = listOf("Main Campus", "North Campus", "South Campus", "Tech Park")
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedCampus by rememberSaveable { mutableStateOf<String?>(null) }
    val vm: RegistrationVM=viewModel()
    val context= LocalContext.current

        Scaffold(
            topBar = { TopAppBarContent(navController) }

        ) {

            padding ->
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
                    value = vm.useremail,
                    onValueChange = { vm.useremail = it },
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
                    value = vm.password,
                    onValueChange = { vm.password = it },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = "Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                // Password field
                OutlinedTextField(
                    value = vm.contact,
                    onValueChange = { vm.contact = it },
                    label = { Text("Phone Number") },
                    leadingIcon = { Icon(Icons.Outlined.Phone, contentDescription = "Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                // Campus dropdown (Material3 Exposed Dropdown)
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    // Readonly text field that shows the selected campus
                    OutlinedTextField(

                        value = selectedCampus ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Campus") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            //.menuAnchor()
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
                                    vm.campName=campus
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Log In button (outlined + red stroke, like the sketch)
                OutlinedButton(
                    onClick = {
                        vm.registerUser(context)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("Sign Up")
                }

                Spacer(Modifier.height(10.dp))

                // Sign Up button (outlined + red stroke)

                Row (horizontalArrangement = Arrangement.Start){
                    Text("Already have an account",modifier.clickable(enabled = true,
                        onClick = {navController.navigate("loginscreen")}))

                }


                Spacer(Modifier.height(24.dp))
            }
        }
    }

