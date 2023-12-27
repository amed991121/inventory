package com.savent.inventory.data.local.datasource

import com.savent.inventory.R
import com.savent.inventory.data.comom.model.Company
import com.savent.inventory.data.comom.model.Product
import com.savent.inventory.data.local.database.dao.ProductDao
import com.savent.inventory.utils.Message
import com.savent.inventory.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class ProductsLocalDatasourceImpl(private val productDao: ProductDao) : ProductsLocalDatasource {

    override suspend fun getProduct(id: Int): Result<Product> =
        withContext(Dispatchers.IO) {
            val result = productDao.get(id)
            if (result != null) return@withContext Result.Success(result)
            Result.Error(
                Message.StringResource(R.string.product_not_found)
            )
        }

    override suspend fun getProduct(barcode: String): Result<Product> =
        withContext(Dispatchers.IO) {
            val result = productDao.get(barcode)
            if (result != null) return@withContext Result.Success(result)
            Result.Error(
                Message.StringResource(R.string.barcode_not_found)
            )
        }

    override fun getProducts(query: String): Flow<Result<List<Product>>> = flow {
        productDao.getAll(query).onEach {
            emit(Result.Success(it))
        }.catch {
            Result.Error<List<Company>>(
                Message.StringResource(R.string.get_products_error)
            )
        }.collect()
    }

    override suspend fun upsertProducts(products: List<Product>): Result<Int> =
        synchronized(this) {
            runBlocking(Dispatchers.IO) {
                productDao.getAll().forEach { current ->
                    if (products.find { new -> current.id == new.id } == null)
                        productDao.delete(current)
                }
                val result = productDao.upsertAll(products)
                if (result.isEmpty() && products.isNotEmpty())
                    return@runBlocking Result.Error<Int>(
                        Message.StringResource(R.string.update_products_error)
                    )
                Result.Success(result.size)
            }
        }
}