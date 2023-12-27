package com.savent.inventory.data.di

import com.savent.inventory.data.local.database.AppDatabase
import com.savent.inventory.data.local.datasource.StoresLocalDatasource
import com.savent.inventory.data.local.datasource.StoresLocalDatasourceImpl
import com.savent.inventory.data.remote.datasource.StoresRemoteDatasource
import com.savent.inventory.data.remote.datasource.StoresRemoteDatasourceImpl
import com.savent.inventory.data.remote.service.StoresApiService
import com.savent.inventory.data.repository.StoresRepository
import com.savent.inventory.data.repository.StoresRepositoryImpl
import org.koin.dsl.module
import retrofit2.Retrofit

val storesDataModule = module {
    includes(baseModule)

    single {
        get<AppDatabase>().storeDao()
    }

    single<StoresLocalDatasource> {
        StoresLocalDatasourceImpl(get())
    }

    single<StoresApiService> {
        get<Retrofit>().create(StoresApiService::class.java)
    }

    single<StoresRemoteDatasource> {
        StoresRemoteDatasourceImpl(get())
    }

    single<StoresRepository> {
        StoresRepositoryImpl(get(), get())
    }
}