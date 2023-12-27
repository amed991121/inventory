package com.savent.inventory.data.di

import com.savent.inventory.data.local.database.AppDatabase
import com.savent.inventory.data.local.datasource.CompaniesLocalDatasource
import com.savent.inventory.data.local.datasource.CompaniesLocalDatasourceImpl
import com.savent.inventory.data.remote.datasource.CompaniesRemoteDatasource
import com.savent.inventory.data.remote.datasource.CompaniesRemoteDatasourceImpl
import com.savent.inventory.data.remote.service.CompaniesApiService
import com.savent.inventory.data.repository.CompaniesRepository
import com.savent.inventory.data.repository.CompaniesRepositoryImpl
import org.koin.dsl.module
import retrofit2.Retrofit

val companiesDataModule = module {
    includes(baseModule)

    single {
        get<AppDatabase>().companyDao()
    }

    single<CompaniesLocalDatasource> {
        CompaniesLocalDatasourceImpl(get())
    }

    single<CompaniesApiService> {
        get<Retrofit>().create(CompaniesApiService::class.java)
    }

    single<CompaniesRemoteDatasource> {
        CompaniesRemoteDatasourceImpl(get())
    }

    single<CompaniesRepository> {
        CompaniesRepositoryImpl(get(), get())
    }
}