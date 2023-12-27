package com.savent.inventory.ui.screen.products

import com.savent.inventory.data.comom.model.Group
import com.savent.inventory.data.comom.model.Product

sealed class ProductsEvent {
    class SearchProducts(val query: String): ProductsEvent()
    class SelectProduct(val product: Product): ProductsEvent()
    class AddGroup(val group: Group): ProductsEvent()
    class DeleteGroup(val id : Int): ProductsEvent()
    class SelectDefaultGroup(val group: Group): ProductsEvent()
    class AddProductEntry(val amount: Float, val groupName: String): ProductsEvent()
}