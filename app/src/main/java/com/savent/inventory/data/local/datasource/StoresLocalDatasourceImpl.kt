package com.savent.inventory.data.local.datasource

import com.savent.inventory.R
import com.savent.inventory.data.comom.model.Company
import com.savent.inventory.data.comom.model.Store
import com.savent.inventory.data.local.database.dao.StoreDao
import com.savent.inventory.utils.Message
import com.savent.inventory.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class StoresLocalDatasourceImpl(private val storeDao: StoreDao) :
    StoresLocalDatasource {
    override suspend fun getStore(id: Int, companyId: Int): Result<Store> =
        withContext(Dispatchers.IO) {
            val result = storeDao.get(id, companyId)
            if (result != null) return@withContext Result.Success(result)
            Result.Error(
                Message.StringResource(R.string.store_not_found)
            )
        }

    override suspend fun getStores(): Result<List<Store>> =
        Result.Success(storeDao.getAll())

    override fun getStores(query: String, companyId: Int): Flow<Result<List<Store>>> =
        flow {
            storeDao.getAll(query, companyId).onEach {
                emit(Result.Success(it))
            }.catch {
                Result.Error<List<Company>>(
                    Message.StringResource(R.string.get_stores_error)
                )
            }.collect()
        }

    override suspend fun upsertStores(restaurants: List<Store>): Result<Int> =
        synchronized(this) {
            runBlocking(Dispatchers.IO) {
                storeDao.getAll().forEach { current ->
                    if (restaurants.find { new ->
                            "${current.id}${current.companyId}" == "${new.id}${new.companyId}"
                        } == null
                    )
                        storeDao.delete(current)
                }
                val result = storeDao.upsertAll(restaurants)
                if (result.isEmpty() && restaurants.isNotEmpty())
                    return@runBlocking Result.Error<Int>(
                        Message.StringResource(R.string.update_stores_error)
                    )
                Result.Success(result.size)
            }
        }
}