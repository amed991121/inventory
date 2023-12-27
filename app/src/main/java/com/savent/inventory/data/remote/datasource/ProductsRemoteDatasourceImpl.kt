package com.savent.inventory.data.remote.datasource

import com.google.gson.Gson
import com.savent.inventory.R
import com.savent.inventory.data.comom.model.Product
import com.savent.inventory.data.remote.ErrorBody
import com.savent.inventory.data.remote.service.ProductsApiService
import com.savent.inventory.utils.Message
import com.savent.inventory.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductsRemoteDatasourceImpl(private val productsApiService: ProductsApiService): ProductsRemoteDatasource {
    override suspend fun getProducts(companyId: Int): Result<List<Product>> =
        withContext(Dispatchers.IO) {
            try {
                val response = productsApiService.getProducts(companyId)
                if (response.isSuccessful)
                    return@withContext Result.Success(response.body()!!)
                Result.Error(
                    Message.DynamicString(
                        Gson().fromJson(
                            response.errorBody()?.charStream(),
                            ErrorBody::class.java
                        ).message
                    )
                )
            } catch (e: Exception) {
                Result.Error(Message.StringResource(R.string.fetch_products_error))
            }
        }
}