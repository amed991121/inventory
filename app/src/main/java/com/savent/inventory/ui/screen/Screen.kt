package com.savent.inventory.ui.screen


sealed class Screen(val route: String) {
    object Products : Screen("products")
    object Inventory : Screen("inventory")
}