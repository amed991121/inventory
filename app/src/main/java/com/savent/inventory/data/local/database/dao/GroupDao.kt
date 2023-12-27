package com.savent.inventory.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.savent.inventory.data.comom.model.Group
import kotlinx.coroutines.flow.Flow

@Dao
abstract class GroupDao: BaseDao<Group>() {
    override fun getTableName(): String = "groups"

    @Query("SELECT * FROM groups ORDER BY name ASC")
    abstract fun getAllAsync(): Flow<List<Group>>
}