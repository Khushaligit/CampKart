package com.example.campkart.composables

import android.R.attr.name
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.campkart.R   // 👈 this is the auto‑generated R class

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


//@Preview(showBackground = true)
@Composable
fun CategoriesScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBarContent(navController) },
        bottomBar = { BottomNavigationBar(navController) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            //item { SearchBar() }
            item {CategoriesListingsGrid2()}
        }
    }
}

@Composable
fun Searchbar2() {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        placeholder = { Text("Search for the Category...") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
    )
}



@Composable
fun CategoriesListingsGrid2() {

    Column {
        Text(
            "Categories", textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .height(900.dp) // height proper
                .padding(8.dp),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories.size) { index ->
                val name = categories[index]   //  string ko extract
                 ProductCard2(name)             // 👈 pass it to your card
            }


        }
    }
}

val categories = listOf(
    "Electronics",
    "Clothing",
    "Books",
    "Sports",
    "Home",
    "Toys",
    "Beauty",
    "Groceries",
    "Automotive"
)


@Composable
fun ProductCard2(name: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp), // instead of aspectRatio
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
                Image(
                    painter = painterResource(R.drawable.ic_launcher_background),
                    contentDescription = ""
                )
            }
            Text(
                name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
            )

//            Text(
//                description,
//                style = MaterialTheme.typography.bodySmall,
//                modifier = Modifier
//                    .padding(bottom = 8.dp)
//                    .height(40.dp), // 👈 fixed height for description
//                maxLines = 2
//            )

//            Button(onClick = { /* TODO */ }, modifier = Modifier.fillMaxWidth()) {
//                Text("View")
//            }


        }
    }
}


