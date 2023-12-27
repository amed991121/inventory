package com.savent.inventory.data.repository

import com.savent.inventory.data.comom.model.Store
import com.savent.inventory.utils.Result
import kotlinx.coroutines.flow.Flow

interface StoresRepository {

    suspend fun upsertStores(stores: List<Store>): Result<Int>

    suspend fun getStore(id: Int, companyId: Int): Result<Store>

    suspend fun getAllStores(): Result<List<Store>>

    fun getStores(query: String, companyId: Int): Flow<Result<List<Store>>>

    suspend fun fetchStores(companyId: Int): Result<Int>
}