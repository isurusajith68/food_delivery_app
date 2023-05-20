package com.example.myapplication.model

import android.net.Uri

data class Product(
    val productId: Long = 0,
    var name: String,
    var price:String,
    var description: String,
    var imageUri: Uri
)