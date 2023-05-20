package com.example.myapplication

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import com.example.myapplication.model.Product


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        const val DATABASE_NAME = "my_database.db"
        const val DATABASE_VERSION = 1
        const val TABLE_USERS = "users"
        const val COLUMN_ID = "_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_ROLE = "role"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_USERS ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_NAME TEXT, $COLUMN_EMAIL TEXT UNIQUE, $COLUMN_PASSWORD TEXT, $COLUMN_ROLE TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }
}



class ProductDatabaseHelper(context: Context) : SQLiteOpenHelper(context, PRODUCT_DATABASE_NAME, null, PRODUCT_DATABASE_VERSION) {

    companion object {
        const val PRODUCT_DATABASE_NAME = "product_database.db"
        const val PRODUCT_DATABASE_VERSION = 1
        const val TABLE_PRODUCTS = "products"
        const val COLUMN_ID = "_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_PRICE = "price"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_IMAGE = "image"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_PRODUCTS ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_NAME TEXT, $COLUMN_DESCRIPTION TEXT,$COLUMN_PRICE TEXT, $COLUMN_IMAGE TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }
    fun getAllProducts(): List<Product> {
        val products = mutableListOf<Product>()
        val db = this.readableDatabase
        val projection = arrayOf(COLUMN_ID, COLUMN_NAME,COLUMN_PRICE, COLUMN_DESCRIPTION, COLUMN_IMAGE)
        val cursor = db.query(TABLE_PRODUCTS, projection, null, null, null, null, null)

        while (cursor.moveToNext()) {
            val productId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val productName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val productPrice = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRICE))
            val productDescription = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
            val productImageUri = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE))

            val product = Product(productId, productName,productPrice, productDescription, Uri.parse(productImageUri))
            products.add(product)
        }

        cursor.close()
        db.close()

        return products
    }


    @SuppressLint("Range")
    fun getProduct(productId: Long): Product? {

        val db = readableDatabase

        val columns = arrayOf(
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_PRICE,
            COLUMN_DESCRIPTION,
            COLUMN_IMAGE
        )

        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(productId.toString())

        val cursor = db.query(
            TABLE_PRODUCTS,
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        if (!cursor.moveToFirst()) {
            cursor.close()
            db.close()
            return null
        }

        val product = Product(
            cursor.getLong(cursor.getColumnIndex(COLUMN_ID)),
            cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
            cursor.getString(cursor.getColumnIndex(COLUMN_PRICE)),
            cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)),
            Uri.parse(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE)))
        )

        cursor.close()
        db.close()

        return product
    }

    fun updateProduct(product: Product): Boolean {

        val db = writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_NAME, product.name)
            put(COLUMN_PRICE, product.price)
            put(COLUMN_DESCRIPTION, product.description)
            put(COLUMN_IMAGE, product.imageUri.toString())
        }

        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(product.productId.toString())

        // Update the database
        val numRowsUpdated = db.update(TABLE_PRODUCTS, values, selection, selectionArgs)

        db.close()

        return numRowsUpdated > 0
    }



}