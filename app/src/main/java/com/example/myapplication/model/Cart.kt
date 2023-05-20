package com.example.myapplication.model

import android.net.Uri


data class Cart(
    var id: Int,
    val userId: Int,
    val productId: Long,
    val name:String,
    val price: String,
    val description: String,
    var quantity: Int,
    var total: Double,
    var imageUri: Uri,

)