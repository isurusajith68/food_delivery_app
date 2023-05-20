package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.database.PlaceOderDatabaseHelper

class PlaceOrderActivity : AppCompatActivity() {

    private lateinit var productNameTextView: TextView
    private lateinit var totalPriceTextView: TextView
    private lateinit var addressEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var placeOrderButton: Button
    private lateinit var productImageView: ImageView
    private lateinit var dbHelper: PlaceOderDatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_order)

        val productName = intent.getStringExtra("order_name")
        val userId = intent.getIntExtra("user_id", -1)
        val imageUriString = intent.getStringExtra("image_uri")
        val totalPrice = intent.getDoubleExtra("order_total", 0.0)
        val productImageUri = Uri.parse(imageUriString)

        productImageView = findViewById(R.id.productImageView)
        productNameTextView = findViewById(R.id.productNameTextView)
        totalPriceTextView = findViewById(R.id.totalPriceTextView)
        addressEditText = findViewById(R.id.addressEditText)
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText)
        placeOrderButton = findViewById(R.id.placeOrderButton)

        productNameTextView.text = productName
        totalPriceTextView.text = totalPrice.toString()
        productImageView.setImageURI(productImageUri)

        placeOrderButton.isEnabled = addressEditText.text.isNotEmpty() && phoneNumberEditText.text.isNotEmpty()

        addressEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                placeOrderButton.isEnabled = s?.isNotEmpty() == true && phoneNumberEditText.text.isNotEmpty()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        phoneNumberEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                placeOrderButton.isEnabled = s?.isNotEmpty() == true && addressEditText.text.isNotEmpty()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
        dbHelper = PlaceOderDatabaseHelper(this)
        placeOrderButton.setOnClickListener {
            val address = addressEditText.text.toString()
            val phoneNumber = phoneNumberEditText.text.toString()
            if (address.isNotEmpty() && phoneNumber.isNotEmpty()) {
                if (productName?.let { it1 ->
                        dbHelper.addOrder(userId,
                            it1, totalPrice, address, phoneNumber)
                    } == true) {
//                    val intent = Intent(this, PlaceOrderListActivity::class.java)
//                    startActivity(intent)
                    finish()
                } else {
                }
            } else {
            }
        }
    }
}
