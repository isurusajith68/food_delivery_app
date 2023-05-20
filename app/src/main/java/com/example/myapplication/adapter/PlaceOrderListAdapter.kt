package com.example.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.PlaceOrderActivity
import com.example.myapplication.R
import com.example.myapplication.model.Cart
import com.example.myapplication.model.PlaceOrder

class PlaceOrderListAdapter(
    private val userId: Int,
    private val placeList: MutableList<PlaceOrder>,
    private val itemClickListener: (PlaceOrder, String) -> Unit
)
    : RecyclerView.Adapter<PlaceOrderListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productNameTextView: TextView = view.findViewById(R.id.productNameTextView)
        val priceTextView: TextView = view.findViewById(R.id.productPriceTextView)
        val totalTextView: TextView = view.findViewById(R.id.totalPriceTextView)
        val quantityTextView: TextView = view.findViewById(R.id.quantityTextView)
        val pendingNameTextView: TextView = view.findViewById(R.id.pending)
       // val editButton: ImageButton = view.findViewById(R.id.editButton)
        val deleteButton: Button = view.findViewById(R.id.deleteButton)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]

        holder.productNameTextView.text = place.productName
        holder.priceTextView.text = place.totalPrice
        holder.quantityTextView.text = place.address
        holder.totalTextView.text = place.phoneNumber
        holder.pendingNameTextView.text = place.status


        holder.deleteButton.setOnClickListener {
            itemClickListener(place, "delete")
        }

//        holder.placebutton.setOnClickListener {
//            val intent = Intent(holder.itemView.context, PlaceOrderActivity::class.java)
//            val bundle = Bundle()
//            bundle.putString("image_uri", cart.imageUri.toString())
//            bundle.putInt("user_id", userId)
//            bundle.putString("order_name", cart.name)
//            bundle.putDouble("order_total", cart.total)
//            bundle.putDouble("order_total", cart.total)
//            intent.putExtras(bundle)
//            holder.itemView.context.startActivity(intent)
//        }
    }


    fun updateCartList(newPlaceList: ArrayList<PlaceOrder>) {
        placeList.clear()
        placeList.addAll(newPlaceList)
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return placeList.size
    }
}

