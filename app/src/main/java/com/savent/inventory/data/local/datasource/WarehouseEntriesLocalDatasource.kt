package com.savent.inventory.data.local.datasource

import com.savent.inventory.data.comom.model.WarehouseEntry
import com.savent.inventory.utils.Result
import kotlinx.coroutines.flow.Flow

interface WarehouseEntriesLocalDatasource {

    suspend fun upsertEntry(entry: WarehouseEntry): Result<Int>

    suspend fun getEntry(id: Int): Result<WarehouseEntry>

    fun getEntries(): Flow<Result<List<WarehouseEntry>>>

    suspend fun upsertEntries(entries: List<WarehouseEntry>): Result<Int>

}