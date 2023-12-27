package com.savent.inventory.data.di

import com.savent.inventory.data.local.database.AppDatabase
import com.savent.inventory.data.local.datasource.ProductsLocalDatasource
import com.savent.inventory.data.local.datasource.ProductsLocalDatasourceImpl
import com.savent.inventory.data.remote.datasource.ProductsRemoteDatasource
import com.savent.inventory.data.remote.datasource.ProductsRemoteDatasourceImpl
import com.savent.inventory.data.remote.service.ProductsApiService
import com.savent.inventory.data.repository.ProductsRepository
import com.savent.inventory.data.repository.ProductsRepositoryImpl
import org.koin.dsl.module
import retrofit2.Retrofit

val productsDataModule = module {
    includes(baseModule)

    single {
        get<AppDatabase>().productDao()
    }

    single<ProductsLocalDatasource> {
        ProductsLocalDatasourceImpl(get())
    }

    single<ProductsApiService> {
        get<Retrofit>().create(ProductsApiService::class.java)
    }

    single<ProductsRemoteDatasource> {
        ProductsRemoteDatasourceImpl(get())
    }

    single<ProductsRepository> {
        ProductsRepositoryImpl(get(), get())
    }
}