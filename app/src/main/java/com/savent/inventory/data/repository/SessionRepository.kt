package com.savent.inventory.data.repository

import com.savent.inventory.data.comom.model.Credentials
import com.savent.inventory.data.comom.model.Session
import com.savent.inventory.utils.Result

interface SessionRepository {

    suspend fun getSession(): Result<Session>

    suspend fun fetchSession(
        credentials: Credentials,
        companyId: Int,
        storeId: Int
    ): Result<Int>
}