package com.savent.inventory.ui.screen.login

import com.savent.inventory.data.comom.model.Credentials


sealed class LoginEvent {
    class SelectCompany(val id: Int): LoginEvent()
    class SearchCompany(val query: String): LoginEvent()
    class SelectStore(val id: Int): LoginEvent()
    class SearchStore(val query: String): LoginEvent()
    object ReloadCompanies: LoginEvent()
    object ReloadStores: LoginEvent()
    class Login(val credentials: Credentials): LoginEvent()
}