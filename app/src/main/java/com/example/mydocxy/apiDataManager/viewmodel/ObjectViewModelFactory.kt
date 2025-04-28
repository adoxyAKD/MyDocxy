package com.example.mydocxy.apiDataManager.viewmodel


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mydocxy.apiDataManager.repository.ObjectRepository

class ObjectViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ObjectViewModel::class.java)) {
            return ObjectViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


