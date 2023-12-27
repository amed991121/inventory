package com.savent.inventory

import com.savent.inventory.data.di.*
import com.savent.inventory.domain.usecase.GetInventoryUseCase
import com.savent.inventory.presentation.viewmodel.InventoryViewModel
import com.savent.inventory.presentation.viewmodel.LoginViewModel
import com.savent.inventory.presentation.viewmodel.ProductsViewModel
import com.savent.inventory.presentation.viewmodel.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel

import org.koin.dsl.module

val splashModule = module {
    includes(sessionDataModule)
    viewModel { SplashViewModel(get()) }
}

val loginModule = module {
    includes(sessionDataModule, companiesDataModule, storesDataModule)
    viewModel { LoginViewModel(get(), get(), get(), get()) }
}

val productsModule = module {
    includes(
        sessionDataModule,
        productsDataModule,
        entriesDataModule,
        storesDataModule,
        groupsDataModule
    )
    viewModel { ProductsViewModel(get(), get(), get(), get(), get(), get(), get()) }
}

val inventoryModule = module {
    includes(sessionDataModule, storesDataModule, productsDataModule, entriesDataModule)
    single {
        GetInventoryUseCase(get(), get(), get(), get())
    }
    viewModel { InventoryViewModel(get(), get(), get(), get(), get(), get()) }
}

val appModule = module {
    includes(splashModule, loginModule, productsModule, inventoryModule)
}
