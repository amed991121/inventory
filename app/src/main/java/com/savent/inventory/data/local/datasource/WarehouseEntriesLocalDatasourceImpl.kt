package com.savent.inventory.data.local.datasource

import com.savent.inventory.R
import com.savent.inventory.data.comom.model.Company
import com.savent.inventory.data.comom.model.WarehouseEntry
import com.savent.inventory.data.local.database.dao.WarehouseEntryDao
import com.savent.inventory.utils.Message
import com.savent.inventory.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class WarehouseEntriesLocalDatasourceImpl(private val warehouseEntryDao: WarehouseEntryDao) :
    WarehouseEntriesLocalDatasource {

    override suspend fun upsertEntry(entry: WarehouseEntry): Result<Int> =
        withContext(Dispatchers.IO) {
            val result = warehouseEntryDao.insert(entry)
            if (result > 0L)
                return@withContext Result.Success(result.toInt())
            Result.Error(
                Message.StringResource(R.string.insert_entry_error)
            )
        }

    override suspend fun getEntry(id: Int): Result<WarehouseEntry> =
        withContext(Dispatchers.IO) {
            val result = warehouseEntryDao.get(id)
            if (result != null) return@withContext Result.Success(result)
            Result.Error(
                Message.StringResource(R.string.entry_not_found)
            )
        }

    override fun getEntries(): Flow<Result<List<WarehouseEntry>>> = flow {
        warehouseEntryDao.getAllEntries().onEach {
            emit(Result.Success(it))
        }.catch {
            Result.Error<List<Company>>(
                Message.StringResource(R.string.get_entries_error)
            )
        }.collect()
    }

    override suspend fun upsertEntries(entries: List<WarehouseEntry>): Result<Int> =
        synchronized(this) {
            runBlocking(Dispatchers.IO) {
                warehouseEntryDao.getAll().forEach { current ->
                    if (entries.find { new -> current.id == new.id } == null)
                        warehouseEntryDao.delete(current)
                }
                val result = warehouseEntryDao.upsertAll(entries)
                if (result.isEmpty() && entries.isNotEmpty())
                    return@runBlocking Result.Error<Int>(
                        Message.StringResource(R.string.update_entries_error)
                    )
                Result.Success(result.size)
            }
        }
}