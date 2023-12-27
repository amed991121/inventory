package com.savent.inventory.data.repository

import com.savent.inventory.data.comom.model.Group
import com.savent.inventory.utils.Result
import kotlinx.coroutines.flow.Flow

interface GroupsRepository {

    suspend fun insertGroup(group: Group): Result<Int>

    suspend fun getGroup(id: Int): Result<Group>

    fun getAllGroups(): Flow<Result<List<Group>>>

    suspend fun deleteGroup(id: Int): Result<Int>
}