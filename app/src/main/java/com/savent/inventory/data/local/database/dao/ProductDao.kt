package com.savent.inventory.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.savent.inventory.data.comom.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ProductDao : BaseDao<Product>() {
    override fun getTableName(): String = "products"

    @Query("SELECT * FROM products WHERE barcode=:barcode")
    abstract suspend fun get(barcode: String): Product?

    @Query("SELECT * FROM products WHERE description LIKE '%' || :query || '%' OR barcode LIKE '%' || :query || '%' ORDER BY description ASC")
    abstract fun getAll(query: String): Flow<List<Product>>


}