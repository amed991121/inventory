package com.savent.inventory.data.remote.datasource

import com.google.gson.Gson
import com.savent.inventory.R
import com.savent.inventory.data.comom.model.Credentials
import com.savent.inventory.data.comom.model.Session
import com.savent.inventory.data.remote.ErrorBody
import com.savent.inventory.data.remote.service.SessionApiService
import com.savent.inventory.utils.Message
import com.savent.inventory.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SessionRemoteDatasourceImpl(private val sessionApiService: SessionApiService) :
    SessionRemoteDatasource {

    override suspend fun getSession(
        credentials: Credentials,
        companyId: Int,
        storeId: Int,
    ): Result<Session> =
        withContext(Dispatchers.IO) {
            try {
                val response = sessionApiService.getSession(
                    credentials = credentials,
                    companyId = companyId,
                    storeId = storeId
                )
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
                Result.Error(Message.StringResource(R.string.login_error))
            }
        }
}