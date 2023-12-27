package com.savent.inventory.data.repository

import com.savent.inventory.data.comom.model.Credentials
import com.savent.inventory.data.comom.model.Session
import com.savent.inventory.data.local.datasource.SessionLocalDatasource
import com.savent.inventory.data.remote.datasource.SessionRemoteDatasource
import com.savent.inventory.utils.Result

class SessionRepositoryImpl(
    private val localDatasource: SessionLocalDatasource,
    private val remoteDatasource: SessionRemoteDatasource
) : SessionRepository {
    override suspend fun getSession(): Result<Session> =
        localDatasource.getSession()

    override suspend fun fetchSession(
        credentials: Credentials,
        companyId: Int,
        storeId: Int,
    ): Result<Int> {
        return when (val response =
            remoteDatasource.getSession(
                credentials = credentials,
                companyId = companyId,
                storeId = storeId
            )) {
            is Result.Success -> localDatasource.saveSession(response.data)
            is Result.Error -> Result.Error(response.message)
        }
    }
}