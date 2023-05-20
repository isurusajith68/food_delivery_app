package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.AllPlaceOrderAdapter
import com.example.myapplication.database.PlaceOderDatabaseHelper
import com.example.myapplication.model.PlaceOrder

class AllPlaceOrderActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AllPlaceOrderAdapter
    private lateinit var databaseHelper: PlaceOderDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_place_order)


        recyclerView = findViewById(R.id.recyclerViewOrders)
        recyclerView.layoutManager = LinearLayoutManager(this)

        databaseHelper = PlaceOderDatabaseHelper(this)

        val orders = databaseHelper.getAllPlaceOrders()

        adapter = AllPlaceOrderAdapter(orders as MutableList<PlaceOrder>, this)
        recyclerView.adapter = adapter
    }

    fun updateOrderStatus(orderId: Int, status: String) {
       databaseHelper.updateOrderStatus(orderId, status)
        adapter.notifyDataSetChanged()
        Toast.makeText(this, "Order status updated successfully", Toast.LENGTH_SHORT).show()
        fetchOrderList();
    }

    private fun fetchOrderList() {
        val orderDbHelper = PlaceOderDatabaseHelper(this)
        val orderList = orderDbHelper.getAllPlaceOrders()
        adapter.updateOrderList(orderList)
    }

}
