package com.example.campkart.viewmodel

import androidx.lifecycle.ViewModel
import com.example.campkart.composables.Comment
import com.example.campkart.repo.CommentRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.ViewModelProvider


class CommentVM(
        private val loginVM: LoginVM // inject or pass reference
    ) : ViewModel() {

        private val repo = CommentRepo()

        private val _comments = MutableStateFlow<List<Comment>>(emptyList())
        val comments: StateFlow<List<Comment>> = _comments

        fun fetchComments(productId: String) {
            repo.fetchComments(productId) { fetched ->
                _comments.value = fetched
            }
        }

        fun postComment(productId: String, text: String) {
            val user = loginVM.loginState.value
            val comment = Comment(
                authorId = user.userId,
                authorName = user.userId, // or map to display name
                text = text
            )
            repo.postComment(productId, comment)
        }
    }


class CommentVMFactory(
    private val loginVM: LoginVM
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommentVM::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CommentVM(loginVM) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}



