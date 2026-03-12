package com.example.campkart.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.campkart.R
import com.example.campkart.model.Product
import com.example.campkart.viewmodel.ListProducts

//@Preview(showBackground = true)
@Composable
fun CampKartHomeScreen(navController: NavController) {
    // --- Hook up your ViewModel (uses default constructor) ---
    val vm: ListProducts = viewModel()

    Scaffold(
        topBar = { TopAppBarContent(navController) },
        bottomBar = { BottomNavigationBar(navController) },
        containerColor = Color.Transparent // IMPORTANT for bg
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
                modifier = Modifier.fillMaxSize(),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )

            /* ---------- Gradient Overlay ---------- */
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(
                                Color(0x66000000),
                                Color(0x22000000),
                                Color(0x66000000)
                            )
                        )
                    )
            )

            /* ---------- Content ---------- */
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { SearchBar() }
                item { LatestListingsGrid(navController = navController, products = vm.productList) }
            }
        }
    }
}


@Composable
fun SearchBar() {
    // A translucent card to make the input readable over the background
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xCCFFFFFF) // slightly translucent white
        )
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Search products...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "search") },
            colors = OutlinedTextFieldDefaults.colors(
                // Text & icons darker to contrast on light card
                focusedTextColor = Color(0xFF1F1F1F),
                unfocusedTextColor = Color(0xFF1F1F1F),
                focusedPlaceholderColor = Color(0x7A1F1F1F),
                unfocusedPlaceholderColor = Color(0x7A1F1F1F),
                focusedLeadingIconColor = Color(0xFF5A5A5A),
                unfocusedLeadingIconColor = Color(0xFF7A7A7A),

                // Outlined border colors
                focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                unfocusedBorderColor = Color(0xFFCCCCCC),

                // Container should match the Card (transparent so card shows through)
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
            )
        )
    }
}


@Composable
fun LatestListingsGrid(
    navController: NavController,
    products: List<Product>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        // Title over dark background should be light/contrasty OR placed inside a card.
        // Here we keep it above the card but make it readable:
        Text(
            "Latest Listings",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = Color.White, // ensure good contrast on your gradient bg
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
        )

        // Grid inside a translucent card for readability
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xCCFFFFFF) // slightly translucent white
            )
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(900.dp) // keep your height
                    .padding(12.dp),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // --- Minimal change: feed fetched list instead of hardcoded 10 ---
                items(products.size) { index ->
                    val product = products[index]
                    ProductCard(
                        navController = navController,
                        product = product
                    )
                }
            }
        }
    }
}


@Composable
fun ProductCard(
    navController: NavController,
    product: Product
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .clickable(enabled = true, onClick = {
                        // Keep your existing click behavior (navigate or open details if needed)
                    })
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(
                        Color(0xFFEDE7F6),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Keeping the placeholder "Image" as in your layout.
                // (If you later add image URLs to Product, replace this with AsyncImage/Coil)
                Text("Image")
            }

            // --- Replace hardcoded texts with product fields, keep styling unchanged ---
            Text(product.prodTitle ?: "Product Name", style = MaterialTheme.typography.bodyMedium)
            Text(product.prodDesc ?: "Product Description", style = MaterialTheme.typography.bodySmall)

            Button(
                onClick = { navController.navigate("productdetailscreen") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View")
            }
        }
    }
}