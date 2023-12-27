package com.savent.inventory.data.remote.datasource

import com.google.gson.Gson
import com.savent.inventory.R
import com.savent.inventory.data.comom.model.Company
import com.savent.inventory.data.remote.ErrorBody
import com.savent.inventory.data.remote.service.CompaniesApiService
import com.savent.inventory.utils.Message
import com.savent.inventory.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class
CompaniesRemoteDatasourceImpl(private val companiesApiService: CompaniesApiService) :
    CompaniesRemoteDatasource {
    override suspend fun getCompanies(): Result<List<Company>> =
        withContext(Dispatchers.IO) {
            try {
                val response = companiesApiService.getCompanies()
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
                Result.Error(Message.StringResource(R.string.fecth_companies_error))
            }
        }
}