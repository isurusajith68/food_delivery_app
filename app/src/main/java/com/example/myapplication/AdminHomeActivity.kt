package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class AdminHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)

    }
    fun addProduct(view: View) {
        val intent = Intent(this, AddProductActivity::class.java)
        startActivity(intent)
    }


    fun viewALLProduct(view: View) {
        val intent = Intent(this, AllPlaceOrderActivity::class.java)
        startActivity(intent)
    }

}