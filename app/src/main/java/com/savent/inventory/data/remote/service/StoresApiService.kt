package com.savent.inventory.data.remote.service

import com.savent.inventory.AppConstants
import com.savent.inventory.data.comom.model.Store
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface StoresApiService {

    @GET(AppConstants.STORES_API_PATH)
    suspend fun getStores(
        @Query("companyId") companyId: Int
    ): Response<List<Store>>
}