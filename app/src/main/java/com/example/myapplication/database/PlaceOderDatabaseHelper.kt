package com.example.myapplication.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import com.example.myapplication.model.Cart
import com.example.myapplication.model.PlaceOrder

class PlaceOderDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    // create place order DB
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "placeholder.db"
        const val TABLE_NAME = "orders"
        const val COLUMN_ID = "id"
        private const val USER_ID = "user_id"
        private const val COLUMN_PRODUCT_NAME = "product_name"
        private const val COLUMN_TOTAL_PRICE = "total_price"
        private const val COLUMN_ADDRESS = "address"
        private const val COLUMN_PHONE_NUMBER = "phone_number"
        private const val COLUMN_STATUS = "status"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$USER_ID TEXT," +
                "$COLUMN_PRODUCT_NAME TEXT," +
                "$COLUMN_TOTAL_PRICE DOUBLE," +
                "$COLUMN_ADDRESS TEXT," +
                "$COLUMN_PHONE_NUMBER TEXT," +
                "$COLUMN_STATUS TEXT DEFAULT 'pending'" +
                ")"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
    //insert
    fun addOrder(ussrId: Int ,productName: String, totalPrice: Double, address: String, phoneNumber: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(USER_ID, ussrId)
            put(COLUMN_PRODUCT_NAME, productName)
            put(COLUMN_TOTAL_PRICE, totalPrice)
            put(COLUMN_ADDRESS, address)
            put(COLUMN_PHONE_NUMBER, phoneNumber)
        }

        val result = db.insert(TABLE_NAME, null, values)
        return result != -1L
    }
    @SuppressLint("Range")
    fun getPlaceList(userId: Int): List<PlaceOrder> {
        val placeItems = mutableListOf<PlaceOrder>()
        val query = "SELECT * FROM $TABLE_NAME WHERE $USER_ID = $userId"
        val db = readableDatabase
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
            val userId = cursor.getInt(cursor.getColumnIndex(USER_ID))
            val productName = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_NAME))
            val totalPrice = cursor.getString(cursor.getColumnIndex(COLUMN_TOTAL_PRICE))
            val address = cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS))
            val phoneNumber = cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_NUMBER))
            val status = cursor.getString(cursor.getColumnIndex(COLUMN_STATUS))
            val placeOrder = PlaceOrder(id,userId, productName, totalPrice ,address, phoneNumber,status)
            placeItems.add(placeOrder)
        }
        cursor.close()
        db.close()
        return placeItems
    }
    @SuppressLint("Range")
    fun getAllPlaceOrders(): List<PlaceOrder> {
        val orders = mutableListOf<PlaceOrder>()
        val query = "SELECT * FROM $TABLE_NAME"
        val db = readableDatabase
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
            val userId = cursor.getInt(cursor.getColumnIndex(USER_ID))
            val productName = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_NAME))
            val totalPrice = cursor.getString(cursor.getColumnIndex(COLUMN_TOTAL_PRICE))
            val address = cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS))
            val phoneNumber = cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_NUMBER))
            val status = cursor.getString(cursor.getColumnIndex(COLUMN_STATUS))
            val order = PlaceOrder(id, userId, productName, totalPrice, address, phoneNumber, status)
            orders.add(order)
        }
        cursor.close()
        db.close()
        return orders
    }

    fun updateOrderStatus(orderId: Int, newStatus: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_STATUS, newStatus)
        }

        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(orderId.toString())
        db.update(TABLE_NAME, values, selection, selectionArgs)
        db.close()
    }


}
