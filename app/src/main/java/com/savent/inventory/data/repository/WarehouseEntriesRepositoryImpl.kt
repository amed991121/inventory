package com.savent.inventory.data.repository

import com.savent.inventory.data.comom.model.WarehouseEntry
import com.savent.inventory.data.local.datasource.WarehouseEntriesLocalDatasource
import com.savent.inventory.data.remote.datasource.WarehouseEntriesRemoteDatasource
import com.savent.inventory.utils.Result
import kotlinx.coroutines.flow.Flow

class WarehouseEntriesRepositoryImpl(
    private val localDatasource: WarehouseEntriesLocalDatasource,
    private val remoteDatasource: WarehouseEntriesRemoteDatasource
) : WarehouseEntriesRepository {

    override suspend fun addEntry(entry: WarehouseEntry, companyId: Int): Result<Int> {
        return when (val response =
            remoteDatasource.insertEntry(entry = entry, companyId = companyId)) {
            is Result.Success -> localDatasource.upsertEntry(entry)
            is Result.Error -> Result.Error(response.message)
        }
    }

    override suspend fun getEntry(id: Int): Result<WarehouseEntry> =
        localDatasource.getEntry(id)

    override fun getEntries(): Flow<Result<List<WarehouseEntry>>> =
        localDatasource.getEntries()

    override suspend fun fetchEntries(companyId: Int): Result<Int> {
        return when (val response = remoteDatasource.getEntries(companyId)) {
            is Result.Success -> localDatasource.upsertEntries(response.data)
            is Result.Error -> Result.Error(response.message)
        }
    }
}