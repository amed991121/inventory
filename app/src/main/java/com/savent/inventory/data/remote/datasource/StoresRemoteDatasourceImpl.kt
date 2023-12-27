package com.savent.inventory.data.remote.datasource

import com.google.gson.Gson
import com.savent.inventory.R
import com.savent.inventory.data.comom.model.Store
import com.savent.inventory.data.remote.ErrorBody
import com.savent.inventory.data.remote.service.StoresApiService
import com.savent.inventory.utils.Message
import com.savent.inventory.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StoresRemoteDatasourceImpl(private val storesApiService: StoresApiService) :
    StoresRemoteDatasource {
    override suspend fun getStores(companyId: Int): Result<List<Store>> =
        withContext(Dispatchers.IO) {
            try {
                val response = storesApiService.getStores(companyId)
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
                Result.Error(Message.StringResource(R.string.fetch_restaurants_error))
            }
        }
}