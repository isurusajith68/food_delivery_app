package com.example.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.myapplication.ProductDatabaseHelper
import com.example.myapplication.R
import com.example.myapplication.SingleProductActivity
import com.example.myapplication.com.EXTRA_PRODUCT_ID.EditProductActivity.Companion.EXTRA_PRODUCT_ID
import com.example.myapplication.model.Product

class ClientProductListAdapter(context: Context, products: List<Product>, private val userId: Int) : ArrayAdapter<Product>(context,
    R.layout.client_list_item_product, products) {

    private var dbHelper: ProductDatabaseHelper = ProductDatabaseHelper(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val holder: ViewHolder

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.client_list_item_product, parent, false)
            holder = ViewHolder(
                view.findViewById(R.id.productImageView),
                view.findViewById(R.id.productNameTextView),
                view.findViewById(R.id.productPriceTextView),
                view.findViewById(R.id.productDescriptionTextView),
            )
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val product = getItem(position)

        if (product != null) {
            holder.productImageView.setImageURI(product.imageUri)
            holder.productNameTextView.text = product.name
            holder.productPriceTextView.text = product.price.toString()
            holder.productDescriptionTextView.text = product.description

            // add an onClickListener to the list item to navigate to the single product view
            if (view != null) {
                view.setOnClickListener {
                    val intent = Intent(context, SingleProductActivity::class.java)
                    intent.putExtra(EXTRA_PRODUCT_ID, product.productId)
                    intent.putExtra("userId", userId )
                    context.startActivity(intent)
                }
            }
        }

        return view!!
    }


    private data class ViewHolder(
        val productImageView: ImageView,
        val productNameTextView: TextView,
        val productPriceTextView: TextView,
        val productDescriptionTextView: TextView,
    )
}
