package com.savent.inventory.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.google.gson.Gson
import com.savent.inventory.R
import com.savent.inventory.utils.Message
import com.savent.inventory.utils.Result
import kotlinx.coroutines.flow.*
import java.lang.reflect.Type

class DataObjectStorage<T> constructor(
    private val gson: Gson,
    private val type: Type,
    private val dataStore: DataStore<Preferences>,
    private val preferenceKey: Preferences.Key<String>
) {

    suspend fun saveData(data: T): Result<Int> {
        try {
            dataStore.edit {
                val jsonString = gson.toJson(data, type)
                it[preferenceKey] = jsonString
            }
        } catch (e: Exception) {
            return Result.Error(Message.StringResource(R.string.save_data_error))
        }
        return Result.Success(0)
    }

    fun getData(): Flow<Result<T>> = flow {
        dataStore.data.map { preferences ->
            val jsonString = preferences[preferenceKey]
            val elements = gson.fromJson<T>(jsonString, type)
            elements
        }.catch {
            emit(Result.Error(Message.StringResource(R.string.retrieve_data_error)))
        }.collect {
            if (it == null)
                emit(Result.Error(Message.StringResource(R.string.retrieve_data_error)))
            else
                emit(Result.Success(it))
        }

    }

}