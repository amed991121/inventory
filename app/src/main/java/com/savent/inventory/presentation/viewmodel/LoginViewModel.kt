package com.savent.inventory.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.savent.inventory.ConnectivityObserver
import com.savent.inventory.NetworkConnectivityObserver
import com.savent.inventory.R
import com.savent.inventory.data.comom.model.Credentials
import com.savent.inventory.data.repository.CompaniesRepository
import com.savent.inventory.data.repository.SessionRepository
import com.savent.inventory.data.repository.StoresRepository
import com.savent.inventory.ui.screen.login.LoginEvent
import com.savent.inventory.ui.screen.login.LoginState
import com.savent.inventory.utils.Message
import com.savent.inventory.utils.NameFormat
import com.savent.inventory.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LoginViewModel(
    private val connectivityObserver: NetworkConnectivityObserver,
    private val companiesRepository: CompaniesRepository,
    private val storesRepository: StoresRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _isLogged = MutableLiveData(false)
    val isLogged: LiveData<Boolean> = _isLogged

    private val _message = MutableSharedFlow<Message>()
    val message = _message.asSharedFlow()

    private var _networkStatus = ConnectivityObserver.Status.Available

    private var companyId = 0
    private var storeId = 0

    private var networkObserverJob: Job? = null
    private var getCompaniesJob: Job? = null
    private var getStoresJob: Job? = null
    private var reloadCompaniesJob: Job? = null
    private var reloadStoresJob: Job? = null
    private var selectCompanyJob: Job? = null
    private var selectStoreJob: Job? = null
    private var loginJob: Job? = null
    private var defaultJob: Job? = null

    init {
        observeNetworkChange()
        reloadCompanies()
        getCompanies()
        getStores()
    }

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.SearchCompany -> getCompanies(event.query)
            is LoginEvent.SearchStore -> getStores(event.query)
            is LoginEvent.SelectCompany -> {
                selectCompany(event.id)
                getStores()
            }
            is LoginEvent.SelectStore -> selectStore(event.id)
            LoginEvent.ReloadCompanies -> reloadCompanies()
            LoginEvent.ReloadStores -> {
                if (companyId == 0) {
                    defaultJob?.cancel()
                    defaultJob = viewModelScope.launch {
                        _message.emit(Message.StringResource(R.string.company_required))
                    }
                    return
                }
                reloadStores()
            }
            is LoginEvent.Login -> login(event.credentials)
        }
    }

    private fun observeNetworkChange() {
        networkObserverJob?.cancel()
        networkObserverJob = viewModelScope.launch(Dispatchers.IO) {
            connectivityObserver.observe().collectLatest { status ->
                _networkStatus = status
            }
        }

    }

    fun getCompanies(query: String = "") {
        getCompaniesJob?.cancel()
        getCompaniesJob = viewModelScope.launch(Dispatchers.IO) {
            companiesRepository.getAllCompanies(query).collectLatest { result ->
                when (result) {
                    is Result.Error -> _message.emit(result.message)
                    is Result.Success -> _state.update {
                        it.copy(
                            companies = result.data.map { it1 ->
                                it1.copy(
                                    name = NameFormat.format(
                                        it1.name
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    fun getStores(query: String = "") {
        getStoresJob?.cancel()
        getStoresJob = viewModelScope.launch(Dispatchers.IO) {
            storesRepository.getStores(query, companyId).collectLatest { result ->
                when (result) {
                    is Result.Error -> {
                        _state.update {
                            it.copy(stores = listOf())
                        }
                        _message.emit(result.message)
                    }
                    is Result.Success -> _state.update {
                        it.copy(
                            stores = result.data.map { it1 ->
                                it1.copy(
                                    name = NameFormat.format(
                                        it1.name
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    fun reloadCompanies() {
        reloadCompaniesJob?.cancel()
        reloadCompaniesJob = viewModelScope.launch(Dispatchers.IO) {
            if (!isNetworkAvailable()) return@launch
            val result = companiesRepository.fetchCompanies()
            if (result is Result.Error) _message.emit(result.message)
        }
    }

    fun reloadStores() {
        reloadStoresJob?.cancel()
        reloadStoresJob = viewModelScope.launch(Dispatchers.IO) {
            if (!isNetworkAvailable()) return@launch
            val result = storesRepository.fetchStores(companyId)
            if (result is Result.Error) _message.emit(result.message)
        }
    }

    fun selectCompany(id: Int) {
        selectCompanyJob?.cancel()
        selectCompanyJob = viewModelScope.launch(Dispatchers.IO) {
            companyId = id
            val result = companiesRepository.getCompany(companyId)
            if (result is Result.Success)
                _state.update {
                    _state.value.copy(
                        selectedCompany = NameFormat.format(result.data.name)
                    )
                }
        }
    }

    fun selectStore(id: Int) {
        selectStoreJob?.cancel()
        selectStoreJob = viewModelScope.launch(Dispatchers.IO) {
            storeId = id
            val result = storesRepository.getStore(storeId, companyId)
            if (result is Result.Success)
                _state.update {
                    _state.value.copy(
                        selectedStore = NameFormat.format(result.data.name)
                    )
                }
        }
    }

    fun login(credentials: Credentials) {
        loginJob?.cancel()
        loginJob = viewModelScope.launch(Dispatchers.IO) {

            credentials.validate().let { result ->
                if (result is Result.Error) {
                    _message.emit(result.message)
                    return@launch
                }
            }

            if (companyId == 0) {
                _message.emit(Message.StringResource(R.string.company_required))
                return@launch
            }
            if (storeId == 0) {
                _message.emit(Message.StringResource(R.string.store_required))
                return@launch
            }

            if (!isNetworkAvailable()) return@launch
            _state.update {
                it.copy(isLoading = true)
            }

            when (val result =
                sessionRepository.fetchSession(
                    credentials,
                    companyId,
                    storeId
                )) {
                is Result.Error -> {
                    _state.update {
                        it.copy(isLoading = false)
                    }
                    _message.emit(result.message)
                }
                is Result.Success -> {
                    _isLogged.postValue(true)
                }
            }

        }
    }

    private suspend fun isNetworkAvailable(): Boolean {
        if (_networkStatus != ConnectivityObserver.Status.Available) {
            _message.emit(Message.StringResource(R.string.internet_error))
            return false
        }
        return true
    }
}