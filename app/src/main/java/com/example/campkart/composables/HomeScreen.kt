package com.example.campkart.composables




import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.campkart.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/* -------------------------------------------------------------------------- */
/*  MODEL (if you already have this, remove this duplicate from your project)  */
/* -------------------------------------------------------------------------- */
data class Product(
    val prodId: String = "",
    val prodTitle: String = "",
    val prodPrice: String = "",     // kept as String to match your ViewModel
    val prodDesc: String = "",
    val prodDeal: String = "",
    val prodCategory: String = "others",
    val createdBy: String = "",
    // Optional image URL if you add images later:
    val imageUrl: String = ""
)

/* -------------------------------------------------------------------------- */
/*  VIEWMODEL: Fetch products from Realtime Database                          */
/* -------------------------------------------------------------------------- */
class ListProducts : ViewModel() {

    private val productsRef = FirebaseDatabase.getInstance().getReference("products")

    var productList = mutableStateListOf<Product>()
        private set

    private var productsListener: ValueEventListener? = null

    init {
        attachListener()
    }

    private fun attachListener() {
        if (productsListener != null) return

        productsListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()
                for (child in snapshot.children) {
                    val product = child.getValue(Product::class.java)
                    product?.let { productList.add(it) }
                }
                Log.d("ListProducts", "Loaded ${productList.size} products")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ListProducts", "Failed to read products: ${error.message}")
            }
        }

        productsRef.addValueEventListener(productsListener as ValueEventListener)
    }

    override fun onCleared() {
        super.onCleared()
        productsListener?.let { productsRef.removeEventListener(it) }
        productsListener = null
    }
}

/* -------------------------------------------------------------------------- */
/*  UI: Home Screen + Grid + Product Card                                     */
/* -------------------------------------------------------------------------- */

@Composable
fun CampKartHomeScreen(
    navController: NavController,
    listVM: ListProducts = viewModel()   // ✅ use VM and observe SnapshotStateList directly
) {
    val products = listVM.productList

    Scaffold(
        topBar = { TopAppBarContent(navController) },
        bottomBar = { BottomNavigationBar(navController) },
        containerColor = Color.Transparent
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            /* ---------- Background Image ---------- */
            Image(
                painter = androidx.compose.ui.res.painterResource(id = R.drawable.designer_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
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

            /* ---------- Content ---------- */
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { SearchBar() }
                item {
                    LatestListingsGrid(
                        navController = navController,
                        products = products
                    )
                }
            }
        }
    }
}

@Composable
fun SearchBar() {
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
                focusedTextColor = Color(0xFF1F1F1F),
                unfocusedTextColor = Color(0xFF1F1F1F),
                focusedPlaceholderColor = Color(0x7A1F1F1F),
                unfocusedPlaceholderColor = Color(0x7A1F1F1F),
                focusedLeadingIconColor = Color(0xFF5A5A5A),
                unfocusedLeadingIconColor = Color(0xFF7A7A7A),
                focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                unfocusedBorderColor = Color(0xFFCCCCCC),
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
        Text(
            "Latest Listings",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = Color.White,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
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
                    .height(900.dp) // keep your fixed height
                    .padding(12.dp),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (products.isEmpty()) {
                    item(span = { GridItemSpan(2) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No products yet", color = Color(0xFF666666))
                        }
                    }
                } else {
                    items(
                        items = products,
                        key = { it.prodId }
                    ) { product ->
                        ProductCard(navController = navController, product = product)
                    }
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
            // If you have product.imageUrl, use AsyncImage; else keep placeholder
            if (product.imageUrl.isNotBlank()) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.prodTitle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(
                            Color(0xFFEDE7F6),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp)
                        )
                        .clickable {
                            // navController.navigate("productdetailscreen/${product.prodId}")
                        },
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(
                            Color(0xFFEDE7F6),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp)
                        )
                        .clickable {
                            // navController.navigate("productdetailscreen/${product.prodId}")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Image")
                }
            }

            Text(
                text = product.prodTitle.ifBlank { "Untitled" },
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1
            )

            val priceLine = product.prodPrice.takeIf { it.isNotBlank() }?.let { "₹$it" }
            Text(
                text = priceLine ?: product.prodDesc.ifBlank { "No description" },
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2
            )

            Button(
                onClick = {
                    navController.navigate("productdetailscreen")
                    // Or pass id: navController.navigate("productdetailscreen/${product.prodId}")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View")
            }
        }
    }
}

/* -------------------------------------------------------------------------- */
/*  PLACEHOLDER BARS (remove if you already have them defined elsewhere)      */
/* -------------------------------------------------------------------------- */

//@Composable
//fun TopAppBarContent(navController: NavController) {
//    androidx.compose.material3.TopAppBar(
//        title = { Text("CampKart") }
//    )
//}

//@Composable
//fun BottomNavigationBar(navController: NavController) {
//    NavigationBar {
//        NavigationBarItem(
//            selected = true,
//            onClick = { /* navigate home */ },
//            icon = { /* your icon */ },
//            label = { Text("Home") }
//        )
//        NavigationBarItem(
//            selected = false,
//            onClick = { /* navigate add */ },
//            icon = { /* your icon */ },
//            label = { Text("Add") }
//        )
//        NavigationBarItem(
//            selected = false,
//            onClick = { /* navigate profile */ },
//            icon = { /* your icon */ },
//            label = { Text("Profile") }
//        )
//    }
//}
