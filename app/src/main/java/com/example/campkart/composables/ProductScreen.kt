package com.example.campkart.composables

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.campkart.R
import com.example.campkart.model.Product
import com.example.campkart.viewmodel.CommentVM
import com.example.campkart.viewmodel.CommentVMFactory
import com.example.campkart.viewmodel.LoginVM
import com.example.campkart.viewmodel.ProdItemVM

@Composable
fun ProductDetailScreen(navController: NavController, prodId: String) {

    val loginVM: LoginVM = viewModel()
    val prodItemVM: ProdItemVM = viewModel()
    val product by prodItemVM.product

    LaunchedEffect(Unit) {
        prodItemVM.fetchProduct(prodId)
    }

    LaunchedEffect(product) {
        product?.createdBy?.let { uid ->
            loginVM.fetchSellerDetails(uid)
        }
    }

    Scaffold(
        topBar = { TopAppBarContent(navController) },
        bottomBar = { BottomNavigationBar(navController) },
        containerColor = Color.Transparent
    ) { innerPadding ->

        ProductDetailContent(
            modifier = Modifier.padding(innerPadding),
            product = product,
            productId = prodId,
            loginVM = loginVM
        )
    }
}

@Composable
fun ProductDetailContent(
    modifier: Modifier = Modifier,
    product: Product? = null,
    productId: String,
    loginVM: LoginVM,
    commentVM: CommentVM = viewModel(factory = CommentVMFactory(loginVM))
) {

    val context = LocalContext.current
    val sellerDetails by loginVM.sellerDetails.collectAsState()
    val sellerPhone = sellerDetails?.userContact ?: ""

    Box(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
    ) {
        Image(
            painter = painterResource(id = R.drawable.designer_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Product image card
            Card(
                shape = RoundedCornerShape(14.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xCCFFFFFF))
            ) {
                if (!product?.prodImageBase64.isNullOrEmpty()) {
                    val imageBytes = Base64.decode(product?.prodImageBase64, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Product Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(14.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.products),
                        contentDescription = "Product Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(14.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Product details card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xCCFFFFFF))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = product?.prodTitle ?: "Product Name",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F1F1F)
                        )
                        Text(
                            text = "₹ ${product?.prodPrice ?: ""}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Category: ${product?.prodCategory ?: ""}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF3D3D3D)
                        )
                        Text(
                            text = "Seller: ${sellerDetails?.userId?.substringBefore("@") ?: "Seller"}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF3D3D3D)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            if (sellerPhone.isNotEmpty()) {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:$sellerPhone")
                                }
                                context.startActivity(intent)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = sellerPhone.isNotEmpty()
                    ) {
                        Text(text = if (sellerPhone.isNotEmpty()) "CONTACT SELLER" else "FETCHING SELLER...")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Product description card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xCCFFFFFF))
            ) {
                Text(
                    text = product?.prodDesc ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF1F1F1F),
                    modifier = Modifier.padding(14.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Comments",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(horizontal = 4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Comments section card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(14.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xCCFFFFFF))
            ) {
                Box(modifier = Modifier.padding(10.dp)) {
                    CommentSection(
                        productId = productId,
                        commentVM = commentVM
                    )
                }
            }
        }
    }
}

data class Comment(
    val id: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val text: String = "",
    val replies: MutableList<Comment> = mutableListOf()
)

@Composable
fun CommentSection(productId: String, commentVM: CommentVM) {
    val comments by commentVM.comments.collectAsState()

    var newComment by remember { mutableStateOf("") }

    LaunchedEffect(productId) {
        commentVM.fetchComments(productId)
    }

    Column {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(comments) { comment ->
                CommentItem(comment = comment, onReply = { replyText ->
                    if (replyText.isNotBlank()) {
                        commentVM.postComment(
                            productId,
                            replyText
                        )
                    }
                })
            }
        }

        Row {
            OutlinedTextField(
                value = newComment,
                onValueChange = { newComment = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Add a comment...") }
            )
            Button(onClick = {
                if (newComment.isNotBlank()) {
                    commentVM.postComment(productId, newComment)
                    newComment = ""
                }
            }) {
                Text("Post")
            }
        }
    }
}

@Composable
fun CommentItem(comment: Comment, onReply: (String) -> Unit) {

    var replyText by remember { mutableStateOf("") }
    var showReplyBox by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {

            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "${comment.authorName}:",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F1F1F)
                )

                Text(
                    text = comment.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF333333)
                )

                Spacer(modifier = Modifier.height(4.dp))

                TextButton(onClick = { showReplyBox = !showReplyBox }) {
                    Text("Reply")
                }

                if (showReplyBox) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        OutlinedTextField(
                            value = replyText,
                            onValueChange = { replyText = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("Write a reply...") }
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {

                                if (replyText.isNotBlank()) {

                                    onReply(replyText)

                                    replyText = ""
                                    showReplyBox = false
                                }
                            }
                        ) {
                            Text("Send")
                        }
                    }
                }
            }
        }

        if (comment.replies.isNotEmpty()) {

            Column(modifier = Modifier.padding(start = 24.dp, top = 4.dp)) {

                comment.replies.forEach { reply ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        shape = RoundedCornerShape(10.dp),
                        elevation = CardDefaults.cardElevation(1.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFDFDFD))
                    ) {

                        Column(modifier = Modifier.padding(8.dp)) {

                            Text(
                                text = "${reply.authorName}:",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1F1F1F)
                            )

                            Text(
                                text = reply.text,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF333333)
                            )
                        }
                    }
                }
            }
        }
    }
}
