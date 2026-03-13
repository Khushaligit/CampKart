package com.example.campkart.composables

import android.R.attr.name
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.campkart.R

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.campkart.model.Category
import com.example.campkart.model.CatrgoriesData


//@Preview(showBackground = true)
@Composable
fun CategoriesScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBarContent(navController) },
        bottomBar = { BottomNavigationBar(navController) },
        containerColor = Color.Transparent
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxWidth()
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
                    .fillMaxWidth()
                    .background(
                        androidx.compose.ui.graphics.Brush.verticalGradient(
                            listOf(
                                Color(0x66000000),
                                Color(0x22000000),
                                Color(0x66000000)
                            )
                        )
                    )
            )

            /* ---------- Content ---------- */
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                item {
                    Text(
                        text = "Categories",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp, bottom = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }

                item {
                    CategoriesListingsGrid2()
                }
            }
        }
    }
}

@Composable
fun CategoriesListingsGrid2() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xCCFFFFFF))
    ) {
        // Use items(categories) to access the data objects directly
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                // Consider removing fixed height so it wraps content or uses weight
                .height(900.dp)
                .padding(12.dp),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                // Pass both the name and the image resource ID
                ProductCard2(name = category.category, imageRes = category.image)
            }
        }
    }
}
//

val categories = mutableListOf<CatrgoriesData>(
    CatrgoriesData("Accessories", R.drawable.accessories),
    CatrgoriesData("Books", R.drawable.books),
    CatrgoriesData("Clothing", R.drawable.clothing),
    CatrgoriesData("Electronics", R.drawable.electronics),
    CatrgoriesData("Vehicle", R.drawable.vehicle),
    CatrgoriesData("Stationary", R.drawable.stationary),
    CatrgoriesData("Food", R.drawable.food),
    CatrgoriesData("Sports", R.drawable.sports),
    CatrgoriesData("Instruments", R.drawable.instruments),
    CatrgoriesData("Beauty", R.drawable.beauty),
    CatrgoriesData("Furniture", R.drawable.furniture),
    CatrgoriesData("Others", R.drawable.others),
)

@Composable
fun ProductCard2(name: String, imageRes: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { /* Handle click */ }),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(
                        Color(0xFFEDE7F6),
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    // Use the passed imageRes here
                    painter = painterResource(id = imageRes),
                    contentDescription = name,
                    modifier = Modifier.padding(12.dp)
                )
            }

            Spacer(Modifier.height(6.dp))

            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}