package com.savent.inventory.data.comom.model

import com.savent.inventory.R
import com.savent.inventory.utils.Message
import com.savent.inventory.utils.Result

data class Credentials(val rfc: String = "", val pin: String = ""){

    fun validate(): Result<Int> {
        if (rfc.isEmpty())
            return Result.Error(Message.StringResource(R.string.empty_rfc))
        if(!rfc.isLettersOrDigits()  || rfc.length < 12)
            return Result.Error(Message.StringResource(R.string.invalid_rfc))
        if(pin.isEmpty())
            return Result.Error(Message.StringResource(R.string.empty_pin))
        return Result.Success(0)
    }

    private fun String.isLettersOrDigits(): Boolean = all {
        it.isLetterOrDigit()
    }
}