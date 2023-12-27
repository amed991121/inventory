package com.savent.inventory.data.remote.datasource

import com.savent.inventory.data.comom.model.Company
import com.savent.inventory.utils.Result

interface CompaniesRemoteDatasource {
    suspend fun getCompanies(): Result<List<Company>>
}