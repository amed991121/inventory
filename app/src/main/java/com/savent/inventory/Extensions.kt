package com.savent.inventory

import android.app.Activity
import android.widget.Toast
import com.savent.inventory.utils.DateFormat
import com.savent.inventory.utils.Message

fun Activity.toast(message: Message) {
    when (message) {
        is Message.DynamicString ->
            Toast.makeText(this, message.value, Toast.LENGTH_LONG).show()
        is Message.StringResource ->
            Toast.makeText(this, message.resId, Toast.LENGTH_LONG).show()
    }

}

fun Long.isToday(): Boolean{
    val today = DateFormat.format(System.currentTimeMillis(), "yyyy-MM-dd")
    val currentDate = DateFormat.format(this, "yyyy-MM-dd")
    return currentDate == today
}

