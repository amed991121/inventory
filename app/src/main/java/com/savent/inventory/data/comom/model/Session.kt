package com.savent.inventory.data.comom.model

import com.google.gson.annotations.SerializedName

data class Session(
    @SerializedName("employee_id")
    val employeeId: Int,
    @SerializedName("employee_name")
    val employeeName: String,
    @SerializedName("branch_id")
    val branchId: Int,
    @SerializedName("company_id")
    val companyId: Int,
    @SerializedName("store_id")
    val storeId: Int
)