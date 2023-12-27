package com.savent.inventory.data.local.datasource

import com.savent.inventory.data.comom.model.Company
import com.savent.inventory.utils.Result
import kotlinx.coroutines.flow.Flow

interface CompaniesLocalDatasource {

    suspend fun getCompany(id: Int): Result<Company>

    fun getCompanies(query: String): Flow<Result<List<Company>>>

    suspend fun upsertCompanies(companies: List<Company>): Result<Int>
}