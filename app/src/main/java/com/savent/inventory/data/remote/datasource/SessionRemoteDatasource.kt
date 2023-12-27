package com.savent.inventory.data.remote.datasource

import com.savent.inventory.data.comom.model.Credentials
import com.savent.inventory.data.comom.model.Session
import com.savent.inventory.utils.Result

interface SessionRemoteDatasource {
    suspend fun getSession(
        credentials: Credentials,
        companyId: Int,
        storeId: Int,
    ): Result<Session>
}