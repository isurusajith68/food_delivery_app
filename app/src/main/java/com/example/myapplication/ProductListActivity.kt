package com.example.myapplication

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.example.myapplication.adapter.ProductListAdapter
import com.example.myapplication.model.Product

class ProductListActivity : AppCompatActivity() {

    private lateinit var productListView: ListView
    private lateinit var dbHelper: ProductDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        productListView = findViewById(R.id.productListView)

        dbHelper = ProductDatabaseHelper(this)
        val db = dbHelper.readableDatabase

        val projection = arrayOf(ProductDatabaseHelper.COLUMN_ID, ProductDatabaseHelper.COLUMN_NAME,ProductDatabaseHelper.COLUMN_PRICE, ProductDatabaseHelper.COLUMN_DESCRIPTION, ProductDatabaseHelper.COLUMN_IMAGE)
        val cursor = db.query(ProductDatabaseHelper.TABLE_PRODUCTS, projection, null, null, null, null, null)

        val products = mutableListOf<Product>()

        while (cursor.moveToNext()) {
            val productId = cursor.getLong(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_ID))
            val productName = cursor.getString(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_NAME))
            val productPrice = cursor.getString(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_PRICE))
            val productDescription = cursor.getString(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_DESCRIPTION))
            val productImageUri = Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_IMAGE)))

            val product = Product(productId, productName,productPrice, productDescription, productImageUri)
            products.add(product)
        }

        val adapter = ProductListAdapter(this, products)
        productListView.adapter = adapter
    }
}
