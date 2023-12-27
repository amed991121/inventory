package com.savent.inventory.data.repository

import com.savent.inventory.data.comom.model.Company
import com.savent.inventory.data.local.datasource.CompaniesLocalDatasource
import com.savent.inventory.data.remote.datasource.CompaniesRemoteDatasource
import com.savent.inventory.utils.Result
import kotlinx.coroutines.flow.Flow

class CompaniesRepositoryImpl(
    private val localDatasource: CompaniesLocalDatasource,
    private val remoteDatasource: CompaniesRemoteDatasource
) : CompaniesRepository {

    override suspend fun upsertCompanies(companies: List<Company>): Result<Int> =
        localDatasource.upsertCompanies(companies)

    override suspend fun getCompany(id: Int): Result<Company> =
        localDatasource.getCompany(id)

    override fun getAllCompanies(query: String): Flow<Result<List<Company>>> =
        localDatasource.getCompanies(query)

    override suspend fun fetchCompanies(): Result<Int> {
        return when (val response = remoteDatasource.getCompanies()) {
            is Result.Success -> localDatasource.upsertCompanies(response.data)
            is Result.Error -> Result.Error(response.message)
        }
    }
}