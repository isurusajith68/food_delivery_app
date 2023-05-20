package com.example.myapplication.model


data class PlaceOrder(
        val orderId: Int,
        val userId: Int,
        val productName: String,
        val totalPrice: String,
        val address: String,
        val phoneNumber: String,
        var status: String
    )
