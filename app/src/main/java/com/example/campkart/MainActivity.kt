package com.example.campkart

import ListingScreen
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.campkart.composables.AddProductScreen
import com.example.campkart.composables.AddScreen
import com.example.campkart.ui.theme.CampKartTheme
import com.example.campkart.composables.CampKartHomeScreen
import com.example.campkart.composables.CategoriesScreen

import com.example.campkart.composables.LoginScreen
import com.example.campkart.composables.ProductDetailScreen
import com.example.campkart.composables.SignupScreen
import com.example.campkart.composables.SplashScreen



class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CampKartTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {  innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )

//                    CampKartHomeScreen()
//                    ProductDetailScreen()
//                    LoginScreen()
//                    AddProductScreen()
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name! this is new name Helooo om j",
//
//        modifier = modifier
//    )
//    Text("om")
}

@Composable
fun AppNavigation(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splashscreen"
    ){

        composable("SplashScreen"){
            SplashScreen(navController)
        }

        composable("campkarthomescreen"){
            CampKartHomeScreen(navController)
        }

        composable("listingscreen"){
            ListingScreen(navController)
        }

        composable("categoriesscreen"){
            CategoriesScreen(navController)
        }

        composable("loginscreen"){
            LoginScreen(navController)
        }

        composable("addscreen"){
            AddScreen(navController)
        }

        composable("productdetailscreen"){
            ProductDetailScreen(navController)
        }
        composable("signupscreen"){
            SignupScreen(navController)
        }

        composable("splashscreen"){
            SplashScreen(navController)
        }

    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CampKartTheme {
//        Greeting("Android")
//        ProductDetailScreen()
    }
}
 