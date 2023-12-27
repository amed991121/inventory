package com.savent.inventory.data.remote.datasource

import android.util.Log
import com.google.gson.Gson
import com.savent.inventory.R
import com.savent.inventory.data.comom.model.WarehouseEntry
import com.savent.inventory.data.remote.ErrorBody
import com.savent.inventory.data.remote.service.WarehouseEntriesApiService
import com.savent.inventory.utils.Message
import com.savent.inventory.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WarehouseEntriesRemoteDatasourceImpl(
    private val warehouseEntriesApiService: WarehouseEntriesApiService
) : WarehouseEntriesRemoteDatasource {

    override suspend fun insertEntry(
        entry: WarehouseEntry,
        companyId: Int
    ): Result<WarehouseEntry> =
        withContext(Dispatchers.IO) {
            try {
                Log.d("log_","companyId$companyId")
                val response =
                    warehouseEntriesApiService.insertEntry(entry = entry, companyId = companyId)
                if (response.isSuccessful)
                    return@withContext Result.Success(response.body()!!)
                Result.Error(
                    Message.DynamicString(
                        Gson().fromJson(
                            response.errorBody()?.charStream(),
                            ErrorBody::class.java
                        ).message
                    )
                )
            } catch (e: Exception) {
                Result.Error(Message.StringResource(R.string.insert_entry_error))
            }
        }

    override suspend fun getEntries(companyId: Int): Result<List<WarehouseEntry>> =
        withContext(Dispatchers.IO) {
            try {
                val response =
                    warehouseEntriesApiService.getEntries(companyId = companyId)
                if (response.isSuccessful)
                    return@withContext Result.Success(response.body()!!)
                Result.Error(
                    Message.DynamicString(
                        Gson().fromJson(
                            response.errorBody()?.charStream(),
                            ErrorBody::class.java
                        ).message
                    )
                )
            } catch (e: Exception) {
                Result.Error(Message.StringResource(R.string.fetch_entries_error))
            }
        }
}