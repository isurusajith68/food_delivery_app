package com.example.myapplication

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class AddProductActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var imageButton:Button
    private lateinit var productImageView: ImageView
    private lateinit var saveButton: Button
    private lateinit var productImageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        nameEditText = findViewById(R.id.nameEditTextproduct)
        priceEditText = findViewById(R.id.productpriceedit)
        descriptionEditText = findViewById(R.id.productDescriptionEditText)
        imageButton = findViewById(R.id.selectImageButton)
        saveButton = findViewById(R.id.addProductButton)
        productImageView = findViewById(R.id.productImageView)

        imageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
        productImageUri = Uri.parse("android.resource://com.example.myapp/drawable/default_product_image")
        saveButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val price = priceEditText.text.toString()
            val image = productImageUri.toString()

            // Save the product to the database
            val dbHelper = ProductDatabaseHelper(this)
            val db = dbHelper.writableDatabase

            val values = ContentValues().apply {
                put(ProductDatabaseHelper.COLUMN_NAME, name)
                put(ProductDatabaseHelper.COLUMN_PRICE, price)
                put(ProductDatabaseHelper.COLUMN_DESCRIPTION, description)
                put(ProductDatabaseHelper.COLUMN_IMAGE, image)
            }

            val newRowId = db.insert(ProductDatabaseHelper.TABLE_PRODUCTS, null, values)

            if (newRowId == -1L) {
                Toast.makeText(this, "Error saving product", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(this, "Product saved", Toast.LENGTH_SHORT).show()
                val intent =  Intent(this, ProductListActivity::class.java);
                startActivity(intent);
                finish()
            }
        }
    }
    fun onViewProductButtonClicked(view: View) {
        val intent = Intent(this, ProductListActivity::class.java)
        startActivity(intent)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            productImageUri = data.data!!
            productImageView.setImageURI(productImageUri)
        }
    }
}