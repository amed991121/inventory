package com.savent.inventory.data.remote.service

import com.savent.inventory.AppConstants
import com.savent.inventory.data.comom.model.WarehouseEntry
import retrofit2.Response
import retrofit2.http.*

interface WarehouseEntriesApiService {

    @Headers("Content-Type: application/json")
    @POST(AppConstants.ENTRIES_API_PATH)
    suspend fun insertEntry(
        @Body entry: WarehouseEntry,
        @Query("companyId") companyId: Int,
    ): Response<WarehouseEntry>

    @GET(AppConstants.ENTRIES_API_PATH)
    suspend fun getEntries(
        @Query("companyId") companyId: Int
    ): Response<List<WarehouseEntry>>
}