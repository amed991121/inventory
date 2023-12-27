package com.savent.inventory.data.local.datasource

import com.savent.inventory.data.comom.model.Product
import com.savent.inventory.utils.Result
import kotlinx.coroutines.flow.Flow

interface ProductsLocalDatasource {

    suspend fun getProduct(id: Int): Result<Product>

    suspend fun getProduct(barcode: String): Result<Product>

    fun getProducts(query: String): Flow<Result<List<Product>>>

    suspend fun upsertProducts(products: List<Product>): Result<Int>

}