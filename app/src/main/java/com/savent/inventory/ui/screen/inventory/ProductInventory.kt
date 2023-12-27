package com.savent.inventory.ui.screen.inventory

import com.savent.inventory.data.comom.model.Product

data class ProductInventory(
    val product: Product,
    val entries: List<ProductEntryDetails>,
    val totalAmount: String,
)