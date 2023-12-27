package com.savent.inventory.ui.screen.products

import com.savent.inventory.data.comom.model.Group
import com.savent.inventory.data.comom.model.Product

data class ProductsState(
    val products: List<Product> = listOf(),
    val selectedProduct: Product =
        Product(id = 0, description = "", barcode = "", unit = ""),
    val groups: List<Group> = listOf(),
    val currentGroup: Group? = null,
    val isRefreshing: Boolean = false,
    val addEntryStatus: AddEntryStatus = AddEntryStatus.Default,
    val addGroupStatus: AddGroupStatus = AddGroupStatus.Default,
    val employeeName: String = "",
    val storeName: String = ""
)