package com.savent.inventory.data.local.datasource

import com.savent.inventory.data.comom.model.Session
import com.savent.inventory.utils.Result

interface SessionLocalDatasource {
    suspend fun saveSession(session: Session): Result<Int>
    suspend fun getSession(): Result<Session>
}