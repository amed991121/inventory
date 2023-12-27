package com.savent.inventory.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.savent.inventory.data.repository.SessionRepository
import com.savent.inventory.utils.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SplashViewModel(private val sessionRepository: SessionRepository): ViewModel() {
    private val _isLogged = MutableLiveData<Boolean?>(null)
    val isLogged: LiveData<Boolean?> = _isLogged
    var job: Job? = null
    fun checkLogin(){
        job?.cancel()
        job = viewModelScope.launch{
            when(sessionRepository.getSession()){
                is Result.Error -> _isLogged.postValue(false)
                is Result.Success -> _isLogged.postValue(true)
            }
        }
    }

}