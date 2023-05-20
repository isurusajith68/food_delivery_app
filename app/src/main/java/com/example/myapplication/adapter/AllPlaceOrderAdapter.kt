package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.AllPlaceOrderActivity
import com.example.myapplication.R
import com.example.myapplication.model.PlaceOrder

class AllPlaceOrderAdapter(
    private val orders: MutableList<PlaceOrder>,
    private val activity: AllPlaceOrderActivity
) : RecyclerView.Adapter<AllPlaceOrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productNameTextView: TextView = itemView.findViewById(R.id.textViewProductName)
        private val totalPriceTextView: TextView = itemView.findViewById(R.id.textViewTotalPrice)
        private val addressTextView: TextView = itemView.findViewById(R.id.textViewAddress)
        private val phoneNumberTextView: TextView = itemView.findViewById(R.id.textViewPhoneNumber)
        private val statusTextView: TextView = itemView.findViewById(R.id.textViewStatus)
        private val changeStatusButton: Button = itemView.findViewById(R.id.buttonChangeStatus)

        fun bind(placeOrder: PlaceOrder) {
            productNameTextView.text = placeOrder.productName
            totalPriceTextView.text = placeOrder.totalPrice.toString()
            addressTextView.text = placeOrder.address
            phoneNumberTextView.text = placeOrder.phoneNumber
            statusTextView.text = placeOrder.status

            changeStatusButton.setOnClickListener {
                val dialog = createStatusDialog(placeOrder.orderId, placeOrder.status)
                dialog.show()
            }
        }

        private fun createStatusDialog(orderId: Int, currentStatus: String): AlertDialog {
            val statusOptions = arrayOf("Pending", "Completed", "Cancelled")

            val dialogBuilder = AlertDialog.Builder(activity)
            dialogBuilder.setTitle("Change Status")
            dialogBuilder.setSingleChoiceItems(statusOptions, statusOptions.indexOf(currentStatus)) { dialog, which ->
                val newStatus = statusOptions[which]
                activity.updateOrderStatus(orderId, newStatus)
                dialog.dismiss()
            }
            dialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

            return dialogBuilder.create()
        }
    }

    fun updateOrderList(newOrderList: List<PlaceOrder>) {
        orders.clear()
        orders.addAll(newOrderList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val placeOrder = orders[position]
        holder.bind(placeOrder)
    }

    override fun getItemCount(): Int {
        return orders.size
    }
}
