package com.savent.inventory.data.local.datasource

import com.savent.inventory.R
import com.savent.inventory.data.comom.model.Session
import com.savent.inventory.data.local.DataObjectStorage
import com.savent.inventory.utils.Message
import com.savent.inventory.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class SessionLocalDatasourceImpl(private val sessionStorage: DataObjectStorage<Session>) :
    SessionLocalDatasource {

    override suspend fun saveSession(session: Session): Result<Int> =
        withContext(Dispatchers.IO) {
            sessionStorage.saveData(session)
        }

    override suspend fun getSession(): Result<Session> =
        withContext(Dispatchers.IO) {
            try {
                sessionStorage.getData().first()
            }catch (e: Exception){
                Result.Error(Message.StringResource(R.string.get_session_error))
            }

        }
}