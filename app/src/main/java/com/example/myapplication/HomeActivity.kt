package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home1)
        val intent = getIntent()
        userId = intent.getIntExtra("userId", -1)
        val authorName = intent.getStringExtra("userName")
        val authorTextView = findViewById<TextView>(R.id.textView3)
        authorTextView.text = authorName
    }



    fun ViewProduct(view: View) {
        val intent = Intent(this, ClientProductListActivity::class.java)
        intent.putExtra("userId", userId)
        startActivity(intent)
    }


    fun ViewCart(view: View) {
        val intent = Intent(this, CartListActivity::class.java)
        intent.putExtra("userId", userId)
        startActivity(intent)
    }
    fun PlaceOrdrr(view: View) {
        val intent = Intent(this, PlaceOrderListActivity::class.java)
        intent.putExtra("userId", userId)
        startActivity(intent)
    }



}
