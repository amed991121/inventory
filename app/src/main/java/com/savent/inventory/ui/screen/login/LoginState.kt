package com.savent.inventory.ui.screen.login

import com.savent.inventory.data.comom.model.Company
import com.savent.inventory.data.comom.model.Store

data class LoginState(
    val companies: List<Company> = listOf(),
    val stores: List<Store> = listOf(),
    val selectedCompany: String = "",
    val selectedStore: String = "",
    val isLoading: Boolean = false
)