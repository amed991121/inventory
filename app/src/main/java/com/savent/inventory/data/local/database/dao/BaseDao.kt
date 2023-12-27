package com.savent.inventory.data.local.database.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
abstract class BaseDao<T> {

    protected abstract fun getTableName(): String

    @Insert(onConflict = REPLACE)
    abstract suspend fun insert(item: T): Long

    @Insert(onConflict = REPLACE)
    abstract suspend fun upsertAll(items: List<T>): List<Long>

    suspend fun getAll(): List<T> =
        getQuery(SimpleSQLiteQuery("SELECT * FROM ${getTableName()}"))

    suspend fun get(id: Int): T? =
        getQuery(
            SimpleSQLiteQuery("SELECT * FROM ${getTableName()} WHERE id = $id")
        ).firstOrNull()

    @RawQuery
    protected abstract suspend fun getQuery(query: SupportSQLiteQuery): List<T>

    @RawQuery
    protected abstract suspend fun modificationQuery(query: SupportSQLiteQuery): Int

    @Update
    abstract suspend fun update(item: T): Int

    @Delete
    abstract suspend fun delete(item: T): Int

    suspend fun delete(id: Int): Int =
        modificationQuery(
            SimpleSQLiteQuery("DELETE FROM ${getTableName()} WHERE id = $id")
        )

}