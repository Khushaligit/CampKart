package com.example.campkart.composables

import android.content.ClipData
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(navController: NavController){
    Scaffold(
        topBar = { TopAppBarContent() },
        bottomBar = { BottomNavigationBar(navController) }
    ){paddingValues ->

        AddProductScreen(modifier = Modifier.padding(paddingValues))
    }
}
@Composable
fun AddProductScreen(modifier: Modifier) {
    var itemName by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 30.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Lowered the layout slightly more to accommodate external TopBar
            Spacer(modifier = Modifier.height(56.dp))

            // Upload Photo Section (Clickable Icon)
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFFF5F5F5)) // Very light gray background
                    .border(2.dp, Color(0xFFE0E0E0), RoundedCornerShape(24.dp))
                    .clickable { /* TODO: Pick Image */ },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFE8F5E9)), // Subtle green
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddAPhoto,
                            contentDescription = "Upload",
                            tint = Color(0xFF4CAF50), // Solid green
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

            Spacer(modifier = Modifier.height(40.dp))

            // Item Name Field
            CustomTextField(
                label = "Item Name",
                value = itemName,
                onValueChange = { itemName = it },
                placeholder = "What are you selling?"
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Description Field
            CustomTextField(
                label = "Description",
                value = description,
                onValueChange = { description = it },
                placeholder = "Describe your item...",
                minLines = 3
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Price Field
            CustomTextField(
                label = "Price",
                value = price,
                onValueChange = { price = it },
                placeholder = "0.00",
                trailingIcon = {
                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Color(0xFF1976D2)
                    )
                }
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Post Item Button (Filled Button for better UX)
            Button(
                onClick = { /* TODO: Post logic */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50), // Green
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    "Post Item",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Added extra spacer at the bottom for BottomBar clearance
            Spacer(modifier = Modifier.height(80.dp))
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
            placeholder = { Text(placeholder, color = Color.LightGray) },
            trailingIcon = trailingIcon,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            minLines = minLines,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE0E0E0), // Neutral border
                focusedBorderColor = Color(0xFF1976D2), // Blue focus
                unfocusedContainerColor = Color(0xFFFAFAFA), // Slightly off-white
                focusedContainerColor = Color.White
            )
        )
    }
}


