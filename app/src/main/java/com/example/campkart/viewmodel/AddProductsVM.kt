package com.example.campkart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campkart.model.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// --- UI State for add product ---
sealed class ProductsUiState {
    data object Idle : ProductsUiState()
    data object Loading : ProductsUiState()
    data object Success : ProductsUiState()
    data class Error(val message: String) : ProductsUiState()
}

// --- Form Model ---
data class ProductForm(
    val title: String = "",
    val price: String = "",        // keep as text for input; parse to Double before save
    val description: String = "",
    val deal: String = "",
    val category: String = "others"
)

class ProductsVM(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
) : ViewModel() {

    private val productsRef = database.getReference("products")

    // Single source of truth for form
    private val _form = MutableStateFlow(ProductForm())
    val form: StateFlow<ProductForm> = _form

    // Screen UI state
    private val _uiState = MutableStateFlow<ProductsUiState>(ProductsUiState.Idle)
    val uiState: StateFlow<ProductsUiState> = _uiState

    // One-time message channel (for snackbar/toast in UI layer)
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    // --- Form updaters (called from TextFields) ---
    fun onTitleChange(v: String) = setForm(_form.value.copy(title = v))
    fun onPriceChange(v: String) = setForm(_form.value.copy(price = v))
    fun onDescriptionChange(v: String) = setForm(_form.value.copy(description = v))
    fun onDealChange(v: String) = setForm(_form.value.copy(deal = v))
    fun onCategoryChange(v: String) = setForm(_form.value.copy(category = v))

    private fun setForm(updated: ProductForm) {
        _form.value = updated
    }

    /**
     * Adds a product to Realtime Database under /products/{productId}
     * productId is generated via push().
     */
    fun addProduct() {
        val current = _form.value

        // 1) Validate early
        validate(current)?.let { error ->
            _uiState.value = ProductsUiState.Error(error)
            return
        }

        // 2) Network work in coroutine
        viewModelScope.launch {
            _uiState.value = ProductsUiState.Loading
            try {
                val uid = auth.currentUser?.uid
                    ?: throw IllegalStateException("User not authenticated")

                // parse price safely (we validated already)
                val priceDouble = current.price.trim().toDouble()

                // Generate a product id via push()
                val newRef = productsRef.push()
                val productId = newRef.key
                    ?: throw IllegalStateException("Failed to generate product id")

                val now = System.currentTimeMillis()
                val product = Product(
                    prodId = productId,
                    prodTitle = current.title.trim(),
                    prodPrice = current.price,
                    prodDesc = current.description.trim(),
                    prodDeal = current.deal.trim(),
                    prodCategory = current.category.trim().ifEmpty { "others" },
                    createdBy = uid,
//                    createdAt = now,
//                    updatedAt = now
                )

                // Write to DB
                newRef.setValue(product).await()

                _uiState.value = ProductsUiState.Success
                _message.value = "Product added successfully"
                _form.value = ProductForm() // clear form after success
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

    // --- Validation rules for form ---
    private fun validate(form: ProductForm): String? {
        if (form.title.isBlank()) return "Title is required."
        if (form.price.isBlank()) return "Price is required."
        val price = form.price.trim()
        if (!price.matches(Regex("^[0-9]+(\\.[0-9]{1,2})?\$")))
            return "Enter a valid price (e.g., 199 or 199.99)."
        if (price.toDoubleOrNull()?.let { it < 0.0 } == true)
            return "Price cannot be negative."
        if (form.description.isBlank()) return "Description is required."
        // Optional: restrict categories to a predefined list
        val allowed = setOf("others", "tents", "backpacks", "gear", "clothing", "footwear")
        if (form.category.isBlank() || !allowed.contains(form.category.lowercase()))
            return "Please select a valid category."
        return null
    }
}
