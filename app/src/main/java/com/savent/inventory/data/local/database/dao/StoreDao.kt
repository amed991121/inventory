package com.savent.inventory.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.savent.inventory.data.comom.model.Store
import kotlinx.coroutines.flow.Flow

@Dao
abstract class StoreDao : BaseDao<Store>() {
    override fun getTableName(): String = "stores"

    @Query("SELECT * FROM stores WHERE id=:id AND company_id=:companyId ORDER BY name ASC")
    abstract fun get(id: Int, companyId: Int): Store?

    @Query("SELECT * FROM stores WHERE name LIKE '%' || :query || '%'  AND company_id=:companyId ORDER BY name ASC")
    abstract fun getAll(query: String, companyId: Int): Flow<List<Store>>

}