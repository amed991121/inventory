package com.savent.inventory.data.repository

import com.savent.inventory.data.comom.model.Product
import com.savent.inventory.utils.Result
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {

    suspend fun getProduct(id: Int): Result<Product>

    suspend fun getProduct(barcode: String): Result<Product>

    fun getProducts(query: String): Flow<Result<List<Product>>>

    suspend fun fetchProducts(companyId: Int): Result<Int>
}