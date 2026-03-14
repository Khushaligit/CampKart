package com.example.campkart.composables

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.campkart.R
import com.example.campkart.model.Product
import com.example.campkart.viewmodel.ListProducts

@Composable
fun CampKartHomeScreen(navController: NavController) {
    val vm: ListProducts = viewModel()

    var searchQuery by remember { mutableStateOf("") }


    val filteredProducts = remember(searchQuery, vm.productList.size) {
        if (searchQuery.isEmpty()) {
            vm.productList
        } else {
            vm.productList.filter {
                it.prodTitle?.contains(searchQuery, ignoreCase = true) == true ||
                        it.prodDesc?.contains(searchQuery, ignoreCase = true) == true
            }
        }
    }

    Scaffold(
        topBar = { TopAppBarContent(navController) },
        bottomBar = { BottomNavigationBar(navController) },
        containerColor = Color.Transparent
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {

            Image(
                painter = painterResource(id = R.drawable.designer_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )


            Box(modifier = Modifier.fillMaxSize().background(
                androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(Color(0x66000000), Color(0x22000000), Color(0x66000000))
                )
            ))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                item {
                    SearchBar(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it }
                    )
                }

                item {
                    LatestListingsGrid(
                        navController = navController,
                        products = filteredProducts
                    )
                }
            }
        }
    }
}

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xCCFFFFFF))
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Search products...") },
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "search") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color(0xFF1F1F1F),
                unfocusedTextColor = Color(0xFF1F1F1F),
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.LightGray,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
    }
}

@Composable
fun LatestListingsGrid(navController: NavController, products: List<Product>) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp)) {
        Text(
            "Latest Listings",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = Color.White,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xCCFFFFFF))
        ) {
            if (products.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().height(150.dp), contentAlignment = Alignment.Center) {
                    Text("No items found", color = Color.Gray)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxWidth().height(900.dp).padding(12.dp),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(products.size) { index ->
                        ProductCard(navController = navController, product = products[index])
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(navController: NavController, product: Product) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable {
                navController.navigate("productdetailscreen/${product.prodId}")
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color(0xFFEDE7F6), shape = RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {

                if (!product.prodImageBase64.isNullOrEmpty()) {

                    val imageBytes = Base64.decode(product.prodImageBase64, Base64.DEFAULT)

                    val bitmap = BitmapFactory.decodeByteArray(
                        imageBytes,
                        0,
                        imageBytes.size
                    )

                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                } else {

                    Text("Image")

                }
            }

            Text(product.prodTitle ?: "No Title", style = MaterialTheme.typography.bodyMedium, maxLines = 1)
            Text(product.prodDesc ?: "No Description", style = MaterialTheme.typography.bodySmall, maxLines = 1)

            Button(
                onClick = { navController.navigate("productdetailscreen/${product.prodId}") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("View", style = MaterialTheme.typography.bodyLarge)
            }
        }

    }
}