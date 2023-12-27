package com.savent.inventory.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun toDatetime(datetime: String): DateTimeObj =
        Gson().fromJson(datetime, object : TypeToken<DateTimeObj>() {}.type)

    @TypeConverter
    fun fromDatetime(datetime: DateTimeObj): String =
        Gson().toJson(datetime)
}