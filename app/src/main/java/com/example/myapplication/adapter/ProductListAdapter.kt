package com.example.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.myapplication.ProductDatabaseHelper
import com.example.myapplication.R
import com.example.myapplication.com.EXTRA_PRODUCT_ID.EditProductActivity
import com.example.myapplication.model.Product


    class ProductListAdapter(context: Context, products: List<Product>) : ArrayAdapter<Product>(context,
        R.layout.list_item_product, products) {
        private var dbHelper: ProductDatabaseHelper = ProductDatabaseHelper(context)

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var view = convertView
            val holder: ViewHolder

            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.list_item_product, parent, false)
                holder = ViewHolder(
                    view.findViewById(R.id.productImageView),
                    view.findViewById(R.id.productNameTextView),
                    view.findViewById(R.id.productPriceTextView),
                    view.findViewById(R.id.productDescriptionTextView),
                    view.findViewById(R.id.editProductButton),
                    view.findViewById(R.id.deleteProductButton)
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
                holder.editProductButton.setOnClickListener {
                    val intent = Intent(context, EditProductActivity::class.java)
                    intent.putExtra(EditProductActivity.EXTRA_PRODUCT_ID, product.productId)
                    context.startActivity(intent)
                }
                holder.deleteProductButton.setOnClickListener {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Delete Product")
                    builder.setMessage("Are you sure you want to delete this product?")
                    builder.setPositiveButton("Yes") { dialog, which ->
                        val db = ProductDatabaseHelper(context).writableDatabase
                        db.delete(ProductDatabaseHelper.TABLE_PRODUCTS, "${ProductDatabaseHelper.COLUMN_ID} = ?", arrayOf(product.productId.toString()))
                        remove(product)
                        notifyDataSetChanged()
                    }
                    builder.setNegativeButton("No") { dialog, which ->
                        // Do nothing
                    }
                    builder.show()
                }
            }

            return view!!
        }
        private data class ViewHolder(
            val productImageView: ImageView,
            val productNameTextView: TextView,
            val productPriceTextView: TextView,
            val productDescriptionTextView: TextView,
            val editProductButton: ImageButton,
            val deleteProductButton: ImageButton
        )
    }

