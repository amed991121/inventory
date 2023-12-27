package com.savent.inventory.data.remote.datasource

import com.savent.inventory.data.comom.model.WarehouseEntry
import com.savent.inventory.utils.Result

interface WarehouseEntriesRemoteDatasource {

    suspend fun insertEntry(entry: WarehouseEntry, companyId: Int): Result<WarehouseEntry>

    suspend fun getEntries(companyId: Int): Result<List<WarehouseEntry>>

}