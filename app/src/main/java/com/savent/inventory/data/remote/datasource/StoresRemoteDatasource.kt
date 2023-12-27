package com.savent.inventory.data.remote.datasource

import com.savent.inventory.data.comom.model.Store
import com.savent.inventory.utils.Result

interface StoresRemoteDatasource {
    suspend fun getStores(companyId: Int): Result<List<Store>>
}