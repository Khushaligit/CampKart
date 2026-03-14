import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.campkart.R
import com.example.campkart.composables.BottomNavigationBar
import com.example.campkart.composables.TopAppBarContent
import com.example.campkart.viewmodel.ListProducts
import com.example.campkart.model.Product
import com.example.campkart.viewmodel.UserProdList

@Composable
fun ListingScreen(navController: NavController) {

    val vm: UserProdList = viewModel()

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

            Image(
                painter = painterResource(id = R.drawable.designer_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )

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

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {

                    Text(
                        text = "My Listings",
                        modifier = Modifier.padding(16.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        color = Color.White
                    )

                    Button(
                        onClick = { navController.navigate("addscreen") },
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(top = 13.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("+ Add Product")
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
                    elevation = CardDefaults.cardElevation(6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xCCFFFFFF)
                    )
                ) {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        items(vm.productList) { product ->
                            ListingItems(product)
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun ListingItems(product: Product, vm : ListProducts = viewModel()) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Box(
                modifier = Modifier
                    .size(70.dp)
                    .background(Color(0xFFF1F1F1)),
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
                        contentDescription = "product",
                        modifier = Modifier.fillMaxSize()
                    )

                } else {

                    Image(
                        painter = painterResource(R.drawable.headphone),
                        contentDescription = "product",
                        modifier = Modifier.fillMaxSize()
                    )

                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    product.prodTitle,
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    "₹ ${product.prodPrice}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
//
//                IconButton(
//                    onClick = {},
//                    modifier = Modifier.padding(start = 30.dp)
//                ) {
//                    Icon(Icons.Default.Edit, "edit")
//                }

                IconButton(
                    onClick = {vm.deleteProduct(product.prodId)},
                    modifier = Modifier.padding(start = 30.dp)
                ) {
                    Icon(Icons.Default.Delete, "delete")
                }
            }
        }
    }
}