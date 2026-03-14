package com.example.campkart.composables


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.campkart.R
import com.google.firebase.auth.FirebaseAuth


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarContent(navController: NavController) {
    TopAppBar(
        title = { Text("CampKart", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
        navigationIcon = {
            IconButton(onClick = {navController.popBackStack()}) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Menu")
            }
        },
        actions = {
            IconButton(onClick = { navController.navigate("signupscreen") }) {
                Icon(painter = painterResource(R.drawable.outline_person_add_24), contentDescription = "Person")
            }
        }
    )
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            selected = false,
            onClick = { navController.navigate("campkarthomescreen") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.List, contentDescription = "My Listing") },
            selected = false,
            onClick = { navController.navigate("listingscreen") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Categories") },
            selected = false,
            onClick = { navController.navigate("categoriesscreen") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            selected = false,
            onClick = { 
                if (auth.currentUser != null) {
                    navController.navigate("userdetailsscreen")
                } else {
                    navController.navigate("loginscreen")
                }
            }
        )

    }
}
