package com.savent.inventory.data.repository

import com.savent.inventory.data.comom.model.Store
import com.savent.inventory.data.local.datasource.StoresLocalDatasource
import com.savent.inventory.data.remote.datasource.StoresRemoteDatasource
import com.savent.inventory.utils.Result
import kotlinx.coroutines.flow.Flow

class StoresRepositoryImpl(
    private val localDatasource: StoresLocalDatasource,
    private val remoteDatasource: StoresRemoteDatasource
) : StoresRepository {

    override suspend fun upsertStores(stores: List<Store>): Result<Int> =
        localDatasource.upsertStores(stores)

    override suspend fun getStore(id: Int, companyId: Int): Result<Store> =
        localDatasource.getStore(id, companyId)

    override suspend fun getAllStores(): Result<List<Store>> =
        localDatasource.getStores()

    override fun getStores(
        query: String,
        companyId: Int
    ): Flow<Result<List<Store>>> =
        localDatasource.getStores(query,companyId)

    override suspend fun fetchStores(companyId: Int): Result<Int> {
        return when (val response = remoteDatasource.getStores(companyId)) {
            is Result.Success -> localDatasource.upsertStores(response.data)
            is Result.Error -> Result.Error(response.message)
        }
    }
}