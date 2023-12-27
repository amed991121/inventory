package com.savent.inventory.ui.screen

sealed class ListScreenEvent {
    class Search(val query: String) : ListScreenEvent()
    class Select(val id: Int) : ListScreenEvent()
    object Close : ListScreenEvent()
}