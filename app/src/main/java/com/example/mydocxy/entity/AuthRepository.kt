package com.example.mydocxy.entity

import android.util.Log
import androidx.lifecycle.LiveData

class AuthRepository(private val userDao: UserDao) {

    // Save or update user by inserting (OnConflictStrategy.REPLACE will handle conflict)
    suspend fun saveUser(user: User) {
        userDao.insertUser(user) // No need for conflict checks; REPLACE handles it
    }

    // Get the first user
    suspend fun getUser(): User? {
        return userDao.getUser()  // Returns the first user or null if not found
    }
}


