package com.savent.inventory.data.remote.service

import com.savent.inventory.data.comom.model.Company
import com.savent.inventory.AppConstants
import retrofit2.Response
import retrofit2.http.GET

interface CompaniesApiService {
    @GET(AppConstants.COMPANIES_API_PATH)
    suspend fun getCompanies(): Response<List<Company>>
}