package com.savent.inventory.data.remote.service

import com.savent.inventory.AppConstants
import com.savent.inventory.data.comom.model.Credentials
import com.savent.inventory.data.comom.model.Session
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface SessionApiService {
    @Headers("Content-Type: application/json")
    @POST(AppConstants.SESSION_API_PATH)
    suspend fun getSession(
        @Body credentials: Credentials,
        @Query("companyId") companyId: Int,
        @Query("storeId") storeId: Int,
    ): Response<Session>
}