package com.example.myapplication

import CartDatabaseHelper
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import com.example.myapplication.com.EXTRA_PRODUCT_ID.EditProductActivity.Companion.EXTRA_PRODUCT_ID

import com.example.myapplication.model.Cart
import com.example.myapplication.model.Product
import java.util.*

class SingleProductActivity : AppCompatActivity() {
    private lateinit var dbHelper: ProductDatabaseHelper
    private var userId: Int = -1
    private lateinit var product: Product

    @SuppressLint("StringFormatMatches")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_product)

        val productNameTextView = findViewById<TextView>(R.id.productNameTextView)
        val productDescriptionTextView = findViewById<TextView>(R.id.productDescriptionTextView)
        val productPriceTextView = findViewById<TextView>(R.id.productPriceTextView)
        val productImageView = findViewById<ImageView>(R.id.productImageView)
        val quantityTextView = findViewById<TextView>(R.id.quantityTextView)
        val totalPriceTextView = findViewById<TextView>(R.id.totalPriceTextView)
        val addToCartButton = findViewById<Button>(R.id.addToCartButton)

        val quantityEditText = findViewById<EditText>(R.id.quantityEditText)

        userId = intent.getIntExtra("userId", -1)
        val productId = intent.getLongExtra(EXTRA_PRODUCT_ID, -1)
        dbHelper = ProductDatabaseHelper(this)
        product = dbHelper.getProduct(productId)!!
        productImageView.setImageURI(product.imageUri)
        productNameTextView.text = product.name
        productDescriptionTextView.text = product.description
        productPriceTextView.text =  product.price




        quantityEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.isNotEmpty()) {
                    val quantity = s.toString().toIntOrNull() ?: 0
                    val totalPrice = product.price.toDouble() * quantity
                    totalPriceTextView.text = totalPrice.toString()
                } else {
                    totalPriceTextView.text = getString(R.string.price_format, 0)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        addToCartButton.setOnClickListener {
                val quantityString = quantityEditText.text.toString().trim()
                val quantity = if (quantityString.isNotEmpty()) quantityString.toInt() else 0
                val total = product.price.toDouble() * quantity


            val id = (Math.random() * 100).toInt()

            val cart = Cart(
                    id,
                    userId,
                    product.productId,
                    product.name,
                    product.price,
                    product.description,
                    quantity,
                    total,
                    product.imageUri,
                )
                val cartDbHelper = CartDatabaseHelper(this)
                 cartDbHelper.addToCart( cart.id, cart.userId, cart.productId , cart.name, cart.price, cart.description, cart.quantity, cart.total, cart.imageUri)

            // if (added) {
                 Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show()
            // } else {
              //     Toast.makeText(this, "Failed to add to cart", Toast.LENGTH_SHORT).show()
               // }
            }
        }

}
