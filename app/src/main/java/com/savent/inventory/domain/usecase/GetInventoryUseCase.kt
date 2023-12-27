package com.savent.inventory.domain.usecase

import com.savent.inventory.data.repository.ProductsRepository
import com.savent.inventory.data.repository.SessionRepository
import com.savent.inventory.data.repository.StoresRepository
import com.savent.inventory.data.repository.WarehouseEntriesRepository
import com.savent.inventory.isToday
import com.savent.inventory.ui.screen.inventory.ProductEntryDetails
import com.savent.inventory.ui.screen.inventory.ProductInventory
import com.savent.inventory.utils.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach

class GetInventoryUseCase(
    private val warehouseEntriesRepository: WarehouseEntriesRepository,
    private val productsRepository: ProductsRepository,
    private val storesRepository: StoresRepository,
    private val sessionRepository: SessionRepository
) {

    operator fun invoke(
        query: String,
        group: String,
        storeFilterList: List<Int>
    ): Flow<Result<List<ProductInventory>>> = flow {

        warehouseEntriesRepository.getEntries().onEach { result ->
            if (result is Result.Error) emit(Result.Error(result.message))
            val entries = (result as Result.Success).data

            val session = sessionRepository.getSession().let {
                if (it is Result.Error) {
                    emit(Result.Error(it.message))
                    return@onEach
                }
                (it as Result.Success).data
            }

            val productInventoryList = mutableListOf<ProductInventory>()
            val productEntriesMap = hashMapOf<Int, MutableList<ProductEntryDetails>>()
            val productAmountMap = hashMapOf<Int, Float>()

            entries.forEach { entry ->
                val entryDetailsList =
                    productEntriesMap.getOrDefault(entry.productId, mutableListOf())

                val store =
                    storesRepository.getStore(id = entry.storeId, companyId = session.companyId)
                        .let {
                            if (it is Result.Error) {
                                emit(Result.Error(it.message))
                                return@onEach
                            }
                            (it as Result.Success).data
                        }

                //Log.d("log_","$entry")
                if(!entry.datetime.toLong().isToday()) return@forEach
                if (storeFilterList.isNotEmpty() && storeFilterList.find { store.id == it } == null)
                    return@forEach
                if (!entry.group.contains(group, ignoreCase = true)) return@forEach

                entryDetailsList.add(
                    ProductEntryDetails(
                        amount = DecimalFormat.format(entry.amount),
                        employeeName = NameFormat.format(entry.employeeName),
                        datetime = DateFormat.format(entry.datetime.toLong(), "hh:mm a"),
                        storeName = NameFormat.format(store.name),
                        group = NameFormat.format(entry.group)
                    )
                )

                productEntriesMap[entry.productId] = entryDetailsList
                productAmountMap[entry.productId] =
                    productAmountMap.getOrDefault(entry.productId, 0F) + entry.amount
            }

            productEntriesMap.forEach { (key, value) ->
                val product = productsRepository.getProduct(id = key).let {
                    if (it is Result.Error) {
                        emit(Result.Error(it.message))
                        return@onEach
                    }
                    (it as Result.Success).data
                }
                if (product.description.contains(query, ignoreCase = true)
                    || product.barcode.contains(query, ignoreCase = true)
                )
                    productInventoryList.add(
                        ProductInventory(
                            product = product.copy(
                                description = NameFormat.format(product.description),
                                unit = NameFormat.format(product.unit)
                            ),
                            entries = value,
                            totalAmount = DecimalFormat.format(productAmountMap[key] ?: 0F),
                        )
                    )
            }

            emit(Result.Success(productInventoryList))

        }.collect()
    }

}