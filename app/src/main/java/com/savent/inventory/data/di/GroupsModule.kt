package com.savent.inventory.data.di

import com.savent.inventory.data.local.database.AppDatabase
import com.savent.inventory.data.repository.GroupsRepository
import com.savent.inventory.data.repository.GroupsRepositoryImpl

import org.koin.dsl.module


val groupsDataModule = module{
    includes(baseModule)

    single {
        get<AppDatabase>().groupDao()
    }

    single<GroupsRepository> {
        GroupsRepositoryImpl(get())
    }
}