package com.savent.inventory.data.remote.service

import com.savent.inventory.AppConstants
import com.savent.inventory.data.comom.model.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductsApiService {
    @GET(AppConstants.PRODUCTS_API_PATH)
    suspend fun getProducts(
        @Query("companyId") companyId: Int
    ): Response<List<Product>>
}