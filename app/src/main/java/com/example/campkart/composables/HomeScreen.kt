package com.example.campkart.composables

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
fun CampKartHomeScreen() {
    Scaffold(
        topBar = { TopAppBarContent() },
        bottomBar = { BottomNavigationBar() }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { SearchBar() }
            item { LatestListingsGrid() }
        }
    }
}


@Composable
fun SearchBar() {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        placeholder = { Text("Search products...") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "search") }
    )
}

@Composable
fun LatestListingsGrid() {
    Column {
        Text(
            "Latest Listings",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .height(900.dp) // 👈 important: give it height
                .padding(8.dp),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(10) { ProductCard() }
        }
    }
}



@Composable
fun ProductCard() {
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
                modifier = Modifier.clickable(enabled = true, onClick = {

                })
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text("Image")
            }
            Text("Product Name", style = MaterialTheme.typography.bodyMedium)
            Text("Product Description", style = MaterialTheme.typography.bodySmall)
            Button(onClick = { /* TODO */ }, modifier = Modifier.fillMaxWidth()) {
                Text("View")

            }
        }
    }
}
