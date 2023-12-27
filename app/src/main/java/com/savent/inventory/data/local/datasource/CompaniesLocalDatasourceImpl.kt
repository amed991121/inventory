package com.savent.inventory.data.local.datasource

import com.savent.inventory.R
import com.savent.inventory.data.comom.model.Company
import com.savent.inventory.data.local.database.dao.CompanyDao
import com.savent.inventory.utils.Message
import com.savent.inventory.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class CompaniesLocalDatasourceImpl(private val companyDao: CompanyDao) : CompaniesLocalDatasource {

    override suspend fun getCompany(id: Int): Result<Company> =
        withContext(Dispatchers.IO) {
            val result = companyDao.get(id)
            if (result != null)  return@withContext Result.Success(result)
            Result.Error(
                Message.StringResource(R.string.company_not_found)
            )
        }

    override fun getCompanies(query: String): Flow<Result<List<Company>>> = flow {
        companyDao.getAll(query).onEach {
            emit(Result.Success(it))
        }.catch {
            Result.Error<List<Company>>(
                Message.StringResource(R.string.get_companies_error)
            )
        }.collect()
    }

    override suspend fun upsertCompanies(companies: List<Company>): Result<Int> =
        synchronized(this) {
            runBlocking(Dispatchers.IO) {
                companyDao.getAll().forEach { current ->
                    if (companies.find { new -> current.id == new.id } == null)
                        companyDao.delete(current)
                }
                val result = companyDao.upsertAll(companies)
                if (result.isEmpty() && companies.isNotEmpty())
                    return@runBlocking Result.Error<Int>(
                        Message.StringResource(R.string.update_products_error)
                    )
                Result.Success(result.size)
            }
        }
}