package com.savent.inventory.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.savent.inventory.data.comom.model.*
import com.savent.inventory.data.local.database.dao.*
import com.savent.inventory.utils.Converters

@Database(
    entities = [
        Company::class,
        Store::class,
        Product::class,
        WarehouseEntry::class,
        Group::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun companyDao(): CompanyDao
    abstract fun productDao(): ProductDao
    abstract fun storeDao(): StoreDao
    abstract fun entryDao(): WarehouseEntryDao
    abstract fun groupDao(): GroupDao
}