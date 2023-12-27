package com.savent.inventory.utils

sealed class Result<T> {

    class Success<T>(val data: T): Result<T>()

    class Error<T>(val message: Message): Result<T>()
}
