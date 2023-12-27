package com.savent.inventory.data.repository

import com.savent.inventory.data.comom.model.Product
import com.savent.inventory.data.local.datasource.ProductsLocalDatasource
import com.savent.inventory.data.remote.datasource.ProductsRemoteDatasource
import com.savent.inventory.utils.Result
import kotlinx.coroutines.flow.Flow

class ProductsRepositoryImpl(
    private val localDatasource: ProductsLocalDatasource,
    private val remoteDatasource: ProductsRemoteDatasource
): ProductsRepository {

    override suspend fun getProduct(id: Int): Result<Product> =
        localDatasource.getProduct(id)

    override suspend fun getProduct(barcode: String): Result<Product> =
        localDatasource.getProduct(barcode)

    override fun getProducts(query: String): Flow<Result<List<Product>>> =
        localDatasource.getProducts(query)

    override suspend fun fetchProducts(companyId: Int): Result<Int> {
        return when (val response = remoteDatasource.getProducts(companyId)) {
            is Result.Success -> localDatasource.upsertProducts(response.data)
            is Result.Error -> Result.Error(response.message)
        }
    }

}