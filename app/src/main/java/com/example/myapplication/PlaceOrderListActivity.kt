package com.example.myapplication

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.CartListAdapter
import com.example.myapplication.adapter.PlaceOrderListAdapter
import com.example.myapplication.database.PlaceOderDatabaseHelper
import com.example.myapplication.model.Cart
import com.example.myapplication.model.PlaceOrder

class PlaceOrderListActivity : AppCompatActivity() {


    private lateinit var dbHelper: PlaceOderDatabaseHelper
    private lateinit var placeAdapter: PlaceOrderListAdapter
    private lateinit var placeList: MutableList<PlaceOrder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_order_list)


        val userId = intent.getIntExtra("userId", -1)


        dbHelper = PlaceOderDatabaseHelper(this)
        placeList = dbHelper.getPlaceList(intent.getIntExtra("userId", -1)) as MutableList<PlaceOrder>

        val cartRecyclerView = findViewById<RecyclerView>(R.id.placeRecyclerView)
        placeAdapter = PlaceOrderListAdapter(userId, placeList) { place, action ->
            when (action) {
                //"edit" -> editPlaceItem(place)
                "delete" -> deletePlaceItem(place)
            }
        }

        cartRecyclerView.adapter = placeAdapter
        cartRecyclerView.layoutManager = LinearLayoutManager(this)

    }

    private fun deletePlaceItem(place: PlaceOrder) {
        val context = this
        val db = PlaceOderDatabaseHelper(context).writableDatabase
        val selection = "${PlaceOderDatabaseHelper.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(place.orderId.toString())
        db.delete(PlaceOderDatabaseHelper.TABLE_NAME, selection, selectionArgs)
        db.close()
        fetchCartItems()
    }

    private fun fetchCartItems() {
        val placeDbHelper = PlaceOderDatabaseHelper(this)
        val placeList = placeDbHelper.getPlaceList(intent.getIntExtra("userId", -1)) as MutableList<Cart>
        placeAdapter.updateCartList(placeList as ArrayList<PlaceOrder>)
    }

}
