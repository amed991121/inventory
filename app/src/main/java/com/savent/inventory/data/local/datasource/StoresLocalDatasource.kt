package com.savent.inventory.data.local.datasource

import com.savent.inventory.data.comom.model.Store
import com.savent.inventory.utils.Result
import kotlinx.coroutines.flow.Flow

interface StoresLocalDatasource {

    suspend fun getStore(id: Int, companyId: Int): Result<Store>

    suspend fun getStores(): Result<List<Store>>

    fun getStores(query: String, companyId: Int): Flow<Result<List<Store>>>

    suspend fun upsertStores(restaurants: List<Store>): Result<Int>

}