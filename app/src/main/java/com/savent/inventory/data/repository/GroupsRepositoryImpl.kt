package com.savent.inventory.data.repository

import com.savent.inventory.R
import com.savent.inventory.data.comom.model.Company
import com.savent.inventory.data.comom.model.Group
import com.savent.inventory.data.local.database.dao.GroupDao
import com.savent.inventory.utils.Message
import com.savent.inventory.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

class GroupsRepositoryImpl(private val groupDao: GroupDao): GroupsRepository {

    override suspend fun insertGroup(group: Group): Result<Int> =
        withContext(Dispatchers.IO) {
            val result = groupDao.insert(group)
            if (result > 0) return@withContext Result.Success(0)
            Result.Error(
                Message.StringResource(R.string.group_insert_error)
            )
        }

    override suspend fun getGroup(id: Int): Result<Group> =
        withContext(Dispatchers.IO) {
            val result = groupDao.get(id)
            if (result != null) return@withContext Result.Success(result)
            Result.Error(
                Message.StringResource(R.string.group_not_found)
            )
        }

    override fun getAllGroups(): Flow<Result<List<Group>>> = flow {
        groupDao.getAllAsync().onEach {
            emit(Result.Success(it))
        }.catch {
            Result.Error<List<Company>>(
                Message.StringResource(R.string.get_groups_error)
            )
        }.collect()
    }

    override suspend fun deleteGroup(id: Int): Result<Int> =
        withContext(Dispatchers.IO) {
            val result = groupDao.delete(id)
            if (result > 0) return@withContext Result.Success(0)
            Result.Error(
                Message.StringResource(R.string.group_delete_error)
            )
        }

}