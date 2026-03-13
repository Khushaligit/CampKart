package com.example.campkart.repo

import com.example.campkart.composables.Comment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CommentRepo {

        private val database = FirebaseDatabase.getInstance()
            .getReference("comments")

        fun postComment(productId: String, comment: Comment) {
            val commentId = database.child(productId).push().key ?: return
            val newComment = comment.copy(id = commentId)

            database.child(productId).child(commentId).setValue(newComment)
        }

        fun fetchComments(productId: String, onResult: (List<Comment>) -> Unit) {
            database.child(productId).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val comments = snapshot.children.mapNotNull {
                        it.getValue(Comment::class.java)
                    }
                    onResult(comments)
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }

