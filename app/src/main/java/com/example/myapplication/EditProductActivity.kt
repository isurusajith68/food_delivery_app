package com.example.myapplication.com.EXTRA_PRODUCT_ID

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.myapplication.ProductDatabaseHelper
import com.example.myapplication.ProductListActivity
import com.example.myapplication.R
import com.example.myapplication.model.Product
class EditProductActivity : AppCompatActivity() {

    private lateinit var imageUri: Uri
    private lateinit var product: Product
    private lateinit var productNameEditText: EditText
    private lateinit var productPriceEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var selectImageButton: Button
    private lateinit var productDescriptionEditText: EditText
    private lateinit var productImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)

         var productId = intent.getLongExtra(EXTRA_PRODUCT_ID, -1)
        productNameEditText = findViewById(R.id.productNameEditText)
        productPriceEditText = findViewById(R.id.productPriceEditText)
        productDescriptionEditText = findViewById(R.id.productDescriptionEditText)
        saveButton = findViewById(R.id.saveButton)
        productImageView = findViewById(R.id.productImageView)
        selectImageButton = findViewById(R.id.selectImageButton)
        saveButton = findViewById(R.id.saveButton)

        selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        imageUri = Uri.parse("android.resource://com.example.myapp/drawable/default_product_image")
        product = ProductDatabaseHelper(this).getProduct(productId)!!

        productNameEditText.setText(product.name)
        productPriceEditText.setText(product.price)
        productDescriptionEditText.setText(product.description)

        imageUri = product.imageUri

        Glide.with(this)
            .load(imageUri)
            .into(productImageView)

            saveButton.setOnClickListener {
                saveChanges()
            }
        }

    private fun saveChanges() {

        product.name = productNameEditText.text.toString()
        product.price = productPriceEditText.text.toString()
        product.description = productDescriptionEditText.text.toString()

        product.imageUri = imageUri

        ProductDatabaseHelper(this).updateProduct(product)
        val intent =  Intent(this, ProductListActivity::class.java);
        startActivity(intent);
        finish()

    }

//    private fun selectImage() {
//        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
//            addCategory(Intent.CATEGORY_OPENABLE)
//            type = "image/*"
//        }
//        startActivityForResult(intent, REQUEST_IMAGE)
//    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            imageUri = data.data!!
            productImageView.setImageURI(imageUri)
        }
    }
    companion object {
        const val EXTRA_PRODUCT_ID = "com.example.myapplication.EXTRA_PRODUCT_ID"
        const val REQUEST_IMAGE = 1
    }
}
