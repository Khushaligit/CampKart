package com.example.campkart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campkart.model.Product
import com.example.campkart.repo.ProductsRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ProductsUiState {
    data object Idle : ProductsUiState()
    data object Loading : ProductsUiState()
    data object Success : ProductsUiState()
    data class Error(val message: String) : ProductsUiState()
}

data class ProductForm(
    val title: String = "",
    val price: String = "",
    val description: String = "",
    val deal: String = "",
    val category: String = "others"
)

class ProductsVM(
    private val repo: ProductsRepo = ProductsRepo(),
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase
) : ViewModel() {

    private val _form = MutableStateFlow(ProductForm())
    val form: StateFlow<ProductForm> = _form

    private val _uiState = MutableStateFlow<ProductsUiState>(ProductsUiState.Idle)
    val uiState: StateFlow<ProductsUiState> = _uiState

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    fun onTitleChange(v: String) = setForm(_form.value.copy(title = v))
    fun onPriceChange(v: String) = setForm(_form.value.copy(price = v))
    fun onDescriptionChange(v: String) = setForm(_form.value.copy(description = v))
    fun onDealChange(v: String) = setForm(_form.value.copy(deal = v))
    fun onCategoryChange(v: String) = setForm(_form.value.copy(category = v))

    private fun setForm(updated: ProductForm) {
        _form.value = updated
    }

    fun addProduct() {

        val current = _form.value

        validate(current)?.let { error ->
            _uiState.value = ProductsUiState.Error(error)
            return
        }

        viewModelScope.launch {

            _uiState.value = ProductsUiState.Loading

            try {

                val uid = repo.getCurrentUserId()

                val productId = repo.generateProductId()

                val product = Product(
                    prodId = productId,
                    prodTitle = current.title.trim(),
                    prodPrice = current.price,
                    prodDesc = current.description.trim(),
                    prodDeal = current.deal.trim(),
                    prodCategory = current.category.trim().ifEmpty { "others" },
                    createdBy = uid
                )

                repo.addProduct(product)

                _uiState.value = ProductsUiState.Success
                _message.value = "Product added successfully"
                _form.value = ProductForm()

            } catch (e: Exception) {

                _uiState.value = ProductsUiState.Error(
                    e.message ?: "Failed to add product. Please try again."
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = ProductsUiState.Idle
        _message.value = null
    }

    private fun validate(form: ProductForm): String? {

        if (form.title.isBlank()) return "Title is required."
        if (form.price.isBlank()) return "Price is required."

        val price = form.price.trim()

        if (!price.matches(Regex("^[0-9]+(\\.[0-9]{1,2})?\$")))
            return "Enter a valid price (e.g., 199 or 199.99)."

        if (price.toDoubleOrNull()?.let { it < 0.0 } == true)
            return "Price cannot be negative."

        if (form.description.isBlank()) return "Description is required."

        val allowed = setOf("others", "tents", "backpacks", "gear", "clothing", "footwear")

        if (form.category.isBlank() || !allowed.contains(form.category.lowercase()))
            return "Please select a valid category."

        return null
    }
}