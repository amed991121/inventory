package com.savent.inventory.data.comom.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("products")
data class Product(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val description: String,
    val barcode: String,
    val unit: String
)