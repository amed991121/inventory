package com.savent.inventory.ui.screen.inventory

import com.savent.inventory.ui.screen.inventory.store_filter.StoreFilter

data class InventoryState (
    val inventory: List<ProductInventory> = listOf(),
    val storeFilterList: List<StoreFilter> = listOf(),
    val isRefreshing: Boolean = false,
)