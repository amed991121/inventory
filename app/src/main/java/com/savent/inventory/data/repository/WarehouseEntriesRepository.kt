package com.savent.inventory.data.repository

import com.savent.inventory.data.comom.model.WarehouseEntry
import com.savent.inventory.utils.Result
import kotlinx.coroutines.flow.Flow

interface WarehouseEntriesRepository {

    suspend fun addEntry(entry: WarehouseEntry, companyId: Int): Result<Int>

    suspend fun getEntry(id: Int): Result<WarehouseEntry>

    fun getEntries(): Flow<Result<List<WarehouseEntry>>>

    suspend fun fetchEntries(companyId: Int): Result<Int>

}