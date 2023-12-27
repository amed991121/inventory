package com.savent.inventory.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.savent.inventory.ConnectivityObserver
import com.savent.inventory.NetworkConnectivityObserver
import com.savent.inventory.R
import com.savent.inventory.data.comom.model.Session
import com.savent.inventory.data.repository.ProductsRepository
import com.savent.inventory.data.repository.SessionRepository
import com.savent.inventory.data.repository.StoresRepository
import com.savent.inventory.data.repository.WarehouseEntriesRepository
import com.savent.inventory.domain.usecase.GetInventoryUseCase
import com.savent.inventory.ui.screen.inventory.InventoryEvent
import com.savent.inventory.ui.screen.inventory.InventoryState
import com.savent.inventory.ui.screen.inventory.store_filter.StoreFilter
import com.savent.inventory.utils.Message
import com.savent.inventory.utils.NameFormat
import com.savent.inventory.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class InventoryViewModel(
    private val connectivityObserver: NetworkConnectivityObserver,
    private val sessionRepository: SessionRepository,
    private val productsRepository: ProductsRepository,
    private val warehouseEntriesRepository: WarehouseEntriesRepository,
    private val storesRepository: StoresRepository,
    private val getInventoryUseCase: GetInventoryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(InventoryState())
    val state = _state.asStateFlow()

    private val _message = MutableSharedFlow<Message>()
    val message = _message.asSharedFlow()

    private var _networkStatus = ConnectivityObserver.Status.Available
    private var session: Session? = null
    private var query: String = ""
    private var group: String = ""
    private var storeFilterList: MutableList<Int> = mutableListOf()

    private var networkObserverJob: Job? = null
    private var refreshDataJob: Job? = null
    private var getSessionJob: Job? = null
    private var getInventoryJob: Job? = null
    private var getStoreFilterListJob: Job? = null
    private var reloadProductsJob: Job? = null
    private var reloadEntriesJob: Job? = null

    init {
        loadSession()
        observeNetworkChange()
        refreshData()
        getInventory()
        getStoreFilterList()
    }

    fun onEvent(event: InventoryEvent) {
        when (event) {
            is InventoryEvent.SearchEntriesByQuery -> {
                getInventory(
                    query = event.query,
                )
                query = event.query
            }

            is InventoryEvent.SearchEntriesByGroup -> {
                getInventory(
                    group = event.group,
                )
                group = event.group
            }

            is InventoryEvent.ChangeStoreFilter -> {
                runBlocking(Dispatchers.IO) {
                    if (event.isSelected) storeFilterList.add(event.storeId)
                    else storeFilterList.remove(event.storeId)
                    getStoreFilterList()
                    getInventory()
                }
            }
            InventoryEvent.ClearAllStoreFilter ->
                runBlocking(Dispatchers.IO) {
                    storeFilterList.clear()
                    getStoreFilterList()
                    getInventory()
                }
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

    fun refreshData() {
        refreshDataJob?.cancel()
        refreshDataJob = viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isRefreshing = true) }
            reloadProducts()
            reloadEntries()
        }
    }

    private fun loadSession() {
        getSessionJob?.cancel()
        getSessionJob = viewModelScope.launch(Dispatchers.IO) {
            when (val result = sessionRepository.getSession()) {
                is Result.Error -> {
                    _message.emit(result.message)
                }
                is Result.Success -> {
                    session = result.data
                }
            }
        }
    }

    private fun getInventory(
        query: String = this.query,
        group: String = this.group,
        storeFilterList: List<Int> = this.storeFilterList
    ) {
        getInventoryJob?.cancel()
        getInventoryJob = viewModelScope.launch(Dispatchers.IO) {
            getInventoryUseCase(
                query = query,
                group = group,
                storeFilterList = storeFilterList
            ).collectLatest { result ->
                when (result) {
                    is Result.Error -> _message.emit(result.message)
                    is Result.Success -> _state.update {
                        it.copy(inventory = result.data)
                    }
                }
            }
        }
    }

    private fun getStoreFilterList() {
        getStoreFilterListJob?.cancel()
        getStoreFilterListJob = viewModelScope.launch(Dispatchers.IO) {
            storesRepository.getAllStores().let { result ->
                if (result is Result.Error) {
                    _message.emit(result.message)
                    return@launch
                }

                val stores = (result as Result.Success).data
                _state.update {
                    it.copy(storeFilterList = stores.map { store ->
                        StoreFilter(
                            store = store.copy(name = NameFormat.format(store.name)),
                            isSelected = storeFilterList.find {id-> store.id == id } != null)
                    })
                }
            }
        }
    }

    private fun reloadProducts() {
        reloadProductsJob?.cancel()
        reloadProductsJob = viewModelScope.launch(Dispatchers.IO) {
            if (!isNetworkAvailable()) return@launch
            val result = session?.let { productsRepository.fetchProducts(it.companyId) }
            if (result is Result.Error) _message.emit(result.message)
        }
    }

    private fun reloadEntries() {
        reloadEntriesJob?.cancel()
        reloadEntriesJob = viewModelScope.launch(Dispatchers.IO) {
            if (!isNetworkAvailable()) return@launch
            val result = session?.let { warehouseEntriesRepository.fetchEntries(it.companyId) }
            if (result is Result.Error) _message.emit(result.message)
            _state.update { it.copy(isRefreshing = false) }
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