package com.savent.inventory.ui.screen.inventory

sealed class InventoryEvent {
    class SearchEntriesByQuery(val query: String):InventoryEvent()
    class SearchEntriesByGroup(val group: String):InventoryEvent()
    class ChangeStoreFilter(val storeId: Int, val isSelected: Boolean): InventoryEvent()
    object ClearAllStoreFilter: InventoryEvent()
}