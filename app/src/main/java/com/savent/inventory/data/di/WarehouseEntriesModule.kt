package com.savent.inventory.data.di

import com.savent.inventory.data.local.database.AppDatabase
import com.savent.inventory.data.local.datasource.WarehouseEntriesLocalDatasource
import com.savent.inventory.data.local.datasource.WarehouseEntriesLocalDatasourceImpl
import com.savent.inventory.data.remote.datasource.WarehouseEntriesRemoteDatasource
import com.savent.inventory.data.remote.datasource.WarehouseEntriesRemoteDatasourceImpl
import com.savent.inventory.data.remote.service.WarehouseEntriesApiService
import com.savent.inventory.data.repository.WarehouseEntriesRepository
import com.savent.inventory.data.repository.WarehouseEntriesRepositoryImpl
import org.koin.dsl.module
import retrofit2.Retrofit

val entriesDataModule = module {
    includes(baseModule)

    single {
        get<AppDatabase>().entryDao()
    }

    single<WarehouseEntriesLocalDatasource> {
        WarehouseEntriesLocalDatasourceImpl(get())
    }

    single<WarehouseEntriesApiService> {
        get<Retrofit>().create(WarehouseEntriesApiService::class.java)
    }

    single<WarehouseEntriesRemoteDatasource> {
        WarehouseEntriesRemoteDatasourceImpl(get())
    }

    single<WarehouseEntriesRepository> {
        WarehouseEntriesRepositoryImpl(get(), get())
    }
}