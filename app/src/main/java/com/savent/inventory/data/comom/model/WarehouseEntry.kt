package com.savent.inventory.data.comom.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.savent.inventory.utils.DateTimeObj


@Entity("entries")
data class WarehouseEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @SerializedName("product_id")
    @ColumnInfo("product_id")
    val productId: Int,
    val amount: Float,
    val group: String,
    @SerializedName("store_id")
    @ColumnInfo("store_id")
    val storeId: Int,
    @SerializedName("employee_id")
    @ColumnInfo("employee_id")
    val employeeId: Int,
    @SerializedName("employee_name")
    @ColumnInfo("employee_name")
    val employeeName: String,
    val datetime: DateTimeObj = DateTimeObj.fromLong(System.currentTimeMillis())
)