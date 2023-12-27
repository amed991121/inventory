package com.savent.inventory.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.savent.inventory.ConnectivityObserver
import com.savent.inventory.NetworkConnectivityObserver
import com.savent.inventory.R
import com.savent.inventory.data.comom.model.Group
import com.savent.inventory.data.comom.model.Product
import com.savent.inventory.data.comom.model.Session
import com.savent.inventory.data.comom.model.WarehouseEntry
import com.savent.inventory.data.repository.*
import com.savent.inventory.ui.screen.products.AddEntryStatus
import com.savent.inventory.ui.screen.products.AddGroupStatus
import com.savent.inventory.ui.screen.products.ProductsEvent
import com.savent.inventory.ui.screen.products.ProductsState
import com.savent.inventory.utils.Message
import com.savent.inventory.utils.NameFormat
import com.savent.inventory.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductsViewModel(
    private val connectivityObserver: NetworkConnectivityObserver,
    private val sessionRepository: SessionRepository,
    private val productsRepository: ProductsRepository,
    private val warehouseEntriesRepository: WarehouseEntriesRepository,
    private val storesRepository: StoresRepository,
    private val groupsRepository: GroupsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(ProductsState())
    val state = _state.asStateFlow()

    private val _message = MutableSharedFlow<Message>()
    val message = _message.asSharedFlow()

    private var _networkStatus = ConnectivityObserver.Status.Available
    private var _session: Session? = null
    private var selectedProductId: Int = 0

    private var networkObserverJob: Job? = null
    private var refreshDataJob: Job? = null
    private var getSessionJob: Job? = null
    private var getProductsJob: Job? = null
    private var reloadProductsJob: Job? = null
    private var addEntryJob: Job? = null
    private var addGroupJob: Job? = null
    private var getGroupsJob: Job? = null

    init {
        loadSession()
        observeNetworkChange()
        getProducts()
        getGroups()
        refreshData()
        savedStateHandle.get<Int>("groupId")?.let { groupId ->
            viewModelScope.launch(Dispatchers.IO) {
                groupsRepository.getGroup(groupId).let { result ->
                    if (result is Result.Success) {
                        val group = result.data
                        _state.update {
                            it.copy(
                                currentGroup = group.copy(
                                    name = NameFormat.format(
                                        group.name
                                    )
                                )
                            )
                        }
                    }

                }
            }
        }
    }

    fun onEvent(event: ProductsEvent) {
        when (event) {
            is ProductsEvent.SearchProducts -> getProducts(event.query)
            is ProductsEvent.SelectProduct -> {
                _state.update {
                    it.copy(
                        selectedProduct = event.product,
                        addEntryStatus = AddEntryStatus.Default
                    )
                }
                selectedProductId = event.product.id
            }
            is ProductsEvent.AddProductEntry ->
                addProductEntryToInventory(
                    amount = event.amount,
                    group = event.groupName
                )
            is ProductsEvent.AddGroup -> insertNewGroup(event.group)
            is ProductsEvent.DeleteGroup -> deleteGroup(event.id)
            is ProductsEvent.SelectDefaultGroup -> {
                _state.update { it.copy(currentGroup = event.group) }
                savedStateHandle["groupId"] = event.group.id
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
                    val session = result.data
                    val storeName = storesRepository.getStore(
                        id = session.storeId,
                        companyId = session.companyId
                    ).let {
                        when (it) {
                            is Result.Error -> ""
                            is Result.Success -> it.data.name
                        }
                    }
                    _state.update {
                        it.copy(
                            employeeName = NameFormat.format(result.data.employeeName),
                            storeName = NameFormat.format(storeName)
                        )
                    }
                    _session = session
                }
            }
        }
    }

    private fun getProducts(query: String = "") {
        getProductsJob?.cancel()
        getProductsJob = viewModelScope.launch(Dispatchers.IO) {
            productsRepository.getProducts(query).collectLatest { result ->
                when (result) {
                    is Result.Error -> _message.emit(result.message)
                    is Result.Success -> _state.update {
                        it.copy(
                            products = result.data.map { product ->
                                product.copy(
                                    description = NameFormat.format(product.description),
                                    unit = NameFormat.format(product.unit)
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    private fun reloadProducts() {
        reloadProductsJob?.cancel()
        reloadProductsJob = viewModelScope.launch(Dispatchers.IO) {
            if (!isNetworkAvailable()) return@launch
            val result = _session?.let { productsRepository.fetchProducts(it.companyId) }
            if (result is Result.Error) _message.emit(result.message)
            _state.update { it.copy(isRefreshing = false) }
        }
    }

    private fun addProductEntryToInventory(amount: Float, group: String) {
        _state.update { it.copy(addEntryStatus = AddEntryStatus.Loading) }
        addEntryJob?.cancel()
        addEntryJob = viewModelScope.launch(Dispatchers.IO) {
            if (!isNetworkAvailable()) return@launch
            val result = _session?.let { session ->
                if (amount == 0f) {
                    _message.emit(Message.StringResource(R.string.add_amount))
                    _state.update { it.copy(addEntryStatus = AddEntryStatus.Error) }
                    return@launch
                }
                if (group.isEmpty()) {
                    _message.emit(Message.StringResource(R.string.add_group_required))
                    _state.update { it.copy(addEntryStatus = AddEntryStatus.Error) }
                    return@launch
                }
                warehouseEntriesRepository.addEntry(
                    entry = WarehouseEntry(
                        productId = selectedProductId,
                        amount = amount,
                        group = group,
                        storeId = session.storeId,
                        employeeId = session.employeeId,
                        employeeName = session.employeeName,
                    ),
                    companyId = session.companyId
                )
            }
            if (result is Result.Error) {
                _message.emit(result.message)
                _state.update { it.copy(addEntryStatus = AddEntryStatus.Error) }
                return@launch
            }
            _state.update { it.copy(addEntryStatus = AddEntryStatus.Success) }

        }
    }

    private fun getGroups() {
        getGroupsJob?.cancel()
        getGroupsJob = viewModelScope.launch(Dispatchers.IO) {
            groupsRepository.getAllGroups().collectLatest { result ->
                when (result) {
                    is Result.Error -> _message.emit(result.message)
                    is Result.Success -> _state.update {
                        it.copy(groups = result.data.map { group ->
                            group.copy(
                                name = NameFormat.format(group.name)
                            )
                        })
                    }
                }
            }
        }
    }

    private fun insertNewGroup(group: Group) {
        _state.update { it.copy(addGroupStatus = AddGroupStatus.Error) }
        addGroupJob?.cancel()
        addGroupJob = viewModelScope.launch(Dispatchers.IO) {
            if (group.name.isEmpty()) {
                _message.emit(Message.StringResource(R.string.add_group_required))
                _state.update { it.copy(addGroupStatus = AddGroupStatus.Error) }
                return@launch
            }
            groupsRepository.insertGroup(group).let { result ->
                if (result is Result.Error) {
                    _message.emit(result.message)
                    _state.update { it.copy(addGroupStatus = AddGroupStatus.Error) }
                    return@launch
                }
                _state.update { it.copy(addGroupStatus = AddGroupStatus.Success) }
            }
        }
    }

    private fun deleteGroup(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            groupsRepository.deleteGroup(id).let { result ->
                if (result is Result.Error) _message.emit(result.message)
            }
        }
    }

    fun getProductByBarcode(barcode: String, onSearchCompleted: (Product?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            productsRepository.getProduct(barcode).let {
                if (it is Result.Error) {
                    onSearchCompleted(null)
                    _message.emit(it.message)
                    return@launch
                }
                val product = (it as Result.Success).data
                onSearchCompleted(
                    product.copy(
                        description = NameFormat.format(product.description),
                        unit = NameFormat.format(product.unit)
                    )
                )
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