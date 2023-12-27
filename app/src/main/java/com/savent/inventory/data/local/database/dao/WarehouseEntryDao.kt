package com.savent.inventory.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.savent.inventory.data.comom.model.WarehouseEntry
import kotlinx.coroutines.flow.Flow

@Dao
abstract class WarehouseEntryDao: BaseDao<WarehouseEntry>() {
    override fun getTableName(): String = "entries"

    @Query("SELECT * FROM entries")
    abstract fun getAllEntries():Flow<List<WarehouseEntry>>
}