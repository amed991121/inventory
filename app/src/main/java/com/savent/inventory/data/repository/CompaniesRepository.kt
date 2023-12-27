package com.savent.inventory.data.repository

import com.savent.inventory.data.comom.model.Company
import com.savent.inventory.utils.Result
import kotlinx.coroutines.flow.Flow

interface CompaniesRepository {

    suspend fun upsertCompanies(companies: List<Company>): Result<Int>

    suspend fun getCompany(id: Int): Result<Company>

    fun getAllCompanies(query: String): Flow<Result<List<Company>>>

    suspend fun fetchCompanies(): Result<Int>
}