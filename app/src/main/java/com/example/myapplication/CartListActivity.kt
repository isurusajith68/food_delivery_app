package com.example.myapplication

import CartDatabaseHelper
import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.CartListAdapter
import com.example.myapplication.model.Cart
class CartListActivity : AppCompatActivity() {

    private lateinit var dbHelper: CartDatabaseHelper
    private lateinit var cartAdapter: CartListAdapter
    private lateinit var cartList: MutableList<Cart>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)

        val userId = intent.getIntExtra("userId", -1)

        dbHelper = CartDatabaseHelper(this)
        cartList = dbHelper.getCartList(intent.getIntExtra("userId", -1)) as MutableList<Cart>

        val cartRecyclerView = findViewById<RecyclerView>(R.id.cartRecyclerView)
        cartAdapter = CartListAdapter(userId, cartList) { cart, action ->
            when (action) {
                "edit" -> editCartItem(cart)
                "delete" -> deleteCartItem(cart)
            }
        }

        cartRecyclerView.adapter = cartAdapter
        cartRecyclerView.layoutManager = LinearLayoutManager(this)

    }

    private fun deleteCartItem(cart: Cart) {
        val context = this
        val db = CartDatabaseHelper(context).writableDatabase
        val selection = "${CartDatabaseHelper.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(cart.id.toString())
        db.delete(CartDatabaseHelper.TABLE_NAME, selection, selectionArgs)
        db.close()
        fetchCartItems()
    }


    fun editCartItem(cart: Cart) {
        val context = this
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_edit_cart)

        val productname = dialog.findViewById<TextView>(R.id.etProductName)
        val productprice = dialog.findViewById<TextView>(R.id.etProductPrice)
        val etQuantity = dialog.findViewById<EditText>(R.id.etProductQuantity)
        val etTotal = dialog.findViewById<TextView>(R.id.etTotalPrice)
        val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)
        val btnUpdate = dialog.findViewById<Button>(R.id.btnSave)

       productprice.setText(cart.price)
        productname.setText(cart.name)


        etQuantity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.isNotEmpty()) {
                    val quantity = s.toString().toIntOrNull() ?: 0
                    val totalPrice = cart.price.toDouble() * quantity
                    etTotal.text = totalPrice.toString()
                } else {
                    etTotal.text = getString(R.string.price_format, 0)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })


        btnCancel.setOnClickListener { dialog.dismiss() }

        btnUpdate.setOnClickListener {
            val newQuantity = etQuantity.text.toString().toInt()
            val newTotal = etTotal.text.toString().toDouble()
            if (newQuantity == 0) {
                Toast.makeText(context, "Quantity must be greater than zero", Toast.LENGTH_SHORT).show()
            } else {
                val cartDbHelper = CartDatabaseHelper(context)
                cart.quantity = newQuantity
                cart.total = newTotal
                cartDbHelper.updateCartItem(cart)
                Toast.makeText(context, "Cart item updated successfully", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                fetchCartItems()
            }
        }

        dialog.show()
    }

    private fun fetchCartItems() {
        val cartDbHelper = CartDatabaseHelper(this)
        val cartList = cartDbHelper.getCartList(intent.getIntExtra("userId", -1)) as MutableList<Cart>
        cartAdapter.updateCartList(cartList as ArrayList<Cart>)
    }




}



