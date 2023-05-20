package com.example.myapplication.adapter

import CartDatabaseHelper
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.PlaceOrderActivity
import com.example.myapplication.R
import com.example.myapplication.model.Cart

class CartListAdapter(
    private val userId: Int,
    private val cartList: MutableList<Cart>,
    private val itemClickListener: (Cart, String) -> Unit
)
    : RecyclerView.Adapter<CartListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productImageView : ImageView = view.findViewById(R.id.productImageView)
        val productNameTextView: TextView = view.findViewById(R.id.productNameTextView)
        val priceTextView: TextView = view.findViewById(R.id.productPriceTextView)
        val totalTextView: TextView = view.findViewById(R.id.totalPriceTextView)
        val quantityTextView: TextView = view.findViewById(R.id.quantityTextView)
        val editButton: ImageButton = view.findViewById(R.id.editButton)
        val deleteButton: ImageButton = view.findViewById(R.id.deleteButton)
        val placebutton : ImageButton = view.findViewById(R.id.placebutton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cart = cartList[position]
        holder.productNameTextView.text = cart.name
        holder.priceTextView.text = cart.price
        holder.quantityTextView.text = cart.quantity.toString()
        holder.totalTextView.text = cart.total.toString()
        holder.productImageView.setImageURI(cart.imageUri)

        holder.editButton.setOnClickListener {
            itemClickListener(cart, "edit")
        }

        holder.deleteButton.setOnClickListener {
            itemClickListener(cart, "delete")
        }

        holder.placebutton.setOnClickListener {
            val intent = Intent(holder.itemView.context, PlaceOrderActivity::class.java)
            val bundle = Bundle()
            bundle.putString("image_uri", cart.imageUri.toString())
            bundle.putInt("user_id", userId)
            bundle.putString("order_name", cart.name)
            bundle.putDouble("order_total", cart.total)
            bundle.putDouble("order_total", cart.total)
            intent.putExtras(bundle)
            holder.itemView.context.startActivity(intent)
        }
    }


    fun updateCartList(newCartList: ArrayList<Cart>) {
        cartList.clear()
        cartList.addAll(newCartList)
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return cartList.size
    }


}
