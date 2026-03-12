package com.example.campkart.composables

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.campkart.R
import com.example.campkart.viewmodel.ProductsUiState
import com.example.campkart.viewmodel.ProductsVM
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

// --- Factory to safely create ProductsVM
class ProductsVMFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductsVM::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductsVM(
                auth = FirebaseAuth.getInstance(),
                database = FirebaseDatabase.getInstance()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class ${modelClass.name}")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBarContent(navController) },
        bottomBar = { BottomNavigationBar(navController) },
        containerColor = Color.Transparent
    ) { paddingValues ->
        AddProductScreen(
            modifier = Modifier.padding(paddingValues),
            navController = navController
        )
    }
}

@Composable
fun AddProductScreen(
    modifier: Modifier,
    navController: NavController,
    vm: ProductsVM = viewModel(factory = ProductsVMFactory())
) {
    val form by vm.form.collectAsState()
    val uiState by vm.uiState.collectAsState()
    val message by vm.message.collectAsState()

    val context = LocalContext.current
    var categoryMenuExpanded by remember { mutableStateOf(false) }

    // The dropdown options (displayed uppercase)
    val categoryOptions = listOf(
        "ACCESSORIES",
        "BOOKS",
        "CLOTHING",
        "ELECTRONICS",
        "VEHICLE",
        "STATIONARY",
        "FOOD",
        "SPORTS",
        "INSTRUMENTS",
        "BEAUTY",
        "FURNITURE",
        "OTHERS"
    )

    // Ensure default deal type and category for first render
    LaunchedEffect(Unit) {
        if (form.deal.isBlank()) vm.onDealChange("Sell")
        if (form.category.isBlank()) vm.onCategoryChange("others")
    }

    // Show messages via Toast
    LaunchedEffect(message) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            vm.resetState()
        }
    }

    // Show errors via Toast
    LaunchedEffect(uiState) {
        if (uiState is ProductsUiState.Error) {
            Toast.makeText(context, (uiState as ProductsUiState.Error).message, Toast.LENGTH_SHORT).show()
        }
    }

    val isLoading = uiState is ProductsUiState.Loading
    val selectedDeal = form.deal.ifBlank { "Sell" }
    val displayCategory = form.category.ifBlank { "others" }.uppercase()

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // ---------- Background Image ----------
        Image(
            painter = painterResource(R.drawable.designer_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // ---------- Gradient Overlay ----------
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

        // ---------- Foreground Content ----------
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Put the whole form inside a translucent Card for readability
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xCCFFFFFF)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    // Upload Photo Section (placeholder)
                    Box(
                        modifier = Modifier
                            .size(160.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color(0xFFF5F5F5))
                            .border(2.dp, Color(0xFFE0E0E0), RoundedCornerShape(24.dp))
                            .clickable { /* TODO: Pick Image */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFE8F5E9)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddAPhoto,
                                    contentDescription = "Upload",
                                    tint = Color(0xFF4CAF50),
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                "Upload Photo",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    // Item Name -> vm.form.title
                    CustomTextField(
                        label = "Item Name",
                        value = form.title,
                        onValueChange = vm::onTitleChange,
                        placeholder = "What are you selling?"
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Description -> vm.form.description
                    CustomTextField(
                        label = "Description",
                        value = form.description,
                        onValueChange = vm::onDescriptionChange,
                        placeholder = "Describe your item...",
                        minLines = 3
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Price -> vm.form.price
                    CustomTextField(
                        label = "Price",
                        value = form.price,
                        onValueChange = vm::onPriceChange,
                        placeholder = "0.00",
                        trailingIcon = {
                            Icon(
                                Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = Color(0xFF1976D2)
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Deal Type -> vm.form.deal
                    Text(
                        "Deal Type",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF333333),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 4.dp, bottom = 8.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        listOf("Sell", "Rent").forEach { option ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { vm.onDealChange(option) }
                            ) {
                                RadioButton(
                                    selected = selectedDeal == option,
                                    onClick = { vm.onDealChange(option) }
                                )
                                Text(option)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Category -> vm.form.category (dropdown of strings)
                    Text(
                        "Category",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF333333),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 4.dp, bottom = 8.dp)
                    )
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = displayCategory, // Show uppercase to the user
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = {
                                IconButton(onClick = { categoryMenuExpanded = !categoryMenuExpanded }) {
                                    Icon(Icons.Default.ChevronRight, contentDescription = null)
                                }
                            }
                        )
                        DropdownMenu(
                            expanded = categoryMenuExpanded,
                            onDismissRequest = { categoryMenuExpanded = false }
                        ) {
                            categoryOptions.forEach { optionUpper ->
                                DropdownMenuItem(
                                    text = { Text(optionUpper) },
                                    onClick = {
                                        // Send lowercase to VM to satisfy its validator set
                                        vm.onCategoryChange(optionUpper.lowercase())
                                        categoryMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    // Post Item -> vm.addProduct()
                    Button(
                        onClick = { vm.addProduct() },
                        enabled = !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50),
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(22.dp)
                            )
                        } else {
                            Text("Post Item", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun CustomTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    minLines: Int = 1,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF333333),
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color(0x99000000)) },
            trailingIcon = trailingIcon,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            minLines = minLines,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedBorderColor = Color(0xFF1976D2),
                unfocusedContainerColor = Color(0xFFFAFAFA),
                focusedContainerColor = Color.White,
                focusedTextColor = Color(0xFF1F1F1F),
                unfocusedTextColor = Color(0xFF1F1F1F)
            )
        )
    }
}