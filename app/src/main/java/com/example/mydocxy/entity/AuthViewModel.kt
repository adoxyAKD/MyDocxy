package com.example.mydocxy.entity

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.mydocxy.loginManager.UserDatabase
import com.example.mydocxy.loginManager.UserEntity
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao = UserDatabase.getDatabase(application).userDao()

    // Save user details to Room Database
    fun saveUserDetails(user: FirebaseUser?) {
        val userEntity = UserEntity(
            uid = user?.uid ?: "",
            name = user?.displayName ?: "",
            email = user?.email ?: ""
        )

        // Use the ViewModel's lifecycle scope to handle background work
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userDao.insert(userEntity)
                Log.d("AuthViewModel", "User details saved to Room DB")
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error saving user details", e)
            }
        }
    }
}


