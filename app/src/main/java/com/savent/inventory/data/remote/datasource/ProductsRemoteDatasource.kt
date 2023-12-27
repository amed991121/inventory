package com.savent.inventory.data.remote.datasource

import com.savent.inventory.data.comom.model.Product
import com.savent.inventory.utils.Result

interface ProductsRemoteDatasource {
    suspend fun getProducts(companyId: Int): Result<List<Product>>
}