package com.savent.inventory.utils

import java.text.SimpleDateFormat


data class DateTimeObj(val date: String, val time: String) {

    companion object {
        fun fromLong(long: Long) =
            DateTimeObj(
                DateFormat.format(long, "yyyy-MM-dd"),
                DateFormat.format(long, "HH:mm:ss.SSS")
            )
    }
}

fun DateTimeObj.toLong() =
    SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse("${this.date} ${this.time}")?.time
        ?: System.currentTimeMillis()
