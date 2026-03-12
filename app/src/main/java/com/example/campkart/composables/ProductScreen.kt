package com.example.campkart.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.campkart.R

@Composable
fun ProductDetailScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBarContent(navController) },
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        ProductDetailContent(
            modifier = Modifier.padding(innerPadding))
    }
}

@Preview(showBackground = true)
@Composable
fun ProductDetailContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Spacer(modifier = Modifier.height(32.dp))

        // Wider product image with fixed height
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background), // replace with your drawable
            contentDescription = "Product Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Product details styled texts
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Product Name",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "₹ 999",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Available Qty: 10",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Seller: John’s Store",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        // Contact Seller button
        Button(
            onClick = { /* TODO: Handle contact seller */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "CONTACT SELLER")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Product description block
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Text(
                text = "This product is designed for outdoor camping enthusiasts. It is durable, lightweight, and easy to carry. Perfect for weekend trips or long adventures.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(12.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Comments section
        Text(
            text = "Comments (Many Online)",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Start)
        )
        CommentSection()
    }
}

data class Comment(
    val id: Int,
    val author: String,
    val text: String,
    val replies: MutableList<Comment> = mutableListOf()
)

@Composable
fun CommentSection() {
    val comments = remember {
        mutableStateListOf(
            Comment(1, "Alice", "Great product!"),
            Comment(2, "Bob", "Is it waterproof?")
        )
    }
    var newComment by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            items(comments) { comment ->
                CommentItem(comment = comment, onReply = { replyText ->
                    if (replyText.isNotBlank()) {
                        comment.replies.add(
                            Comment(
                                id = comment.replies.size + 1,
                                author = "Seller",
                                text = replyText
                            )
                        )
                    }
                })
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newComment,
                onValueChange = { newComment = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Add a comment...") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (newComment.isNotBlank()) {
                        comments.add(
                            Comment(
                                id = comments.size + 1,
                                author = "You",
                                text = newComment
                            )
                        )
                        newComment = ""
                    }
                }
            ) {
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
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = "${comment.author}:", fontWeight = FontWeight.Bold)
                Text(text = comment.text, style = MaterialTheme.typography.bodyMedium)

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
                        Button(onClick = {
                            if (replyText.isNotBlank()) {
                                onReply(replyText)
                                replyText = ""
                                showReplyBox = false
                            }
                        }) {
                            Text("Send")
                        }
                    }
                }
            }
        }

        // Show replies indented
        if (comment.replies.isNotEmpty()) {
            Column(modifier = Modifier.padding(start = 24.dp, top = 4.dp)) {
                comment.replies.forEach { reply ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(text = "${reply.author}:", fontWeight = FontWeight.Bold)
                            Text(text = reply.text, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}








