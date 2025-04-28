package com.example.mydocxy.entity

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface UserDao {

    // Insert user or replace if already exists
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    // Fetch the first user (if exists)
    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getUser(): User?

    // Fetch user by UID
    @Query("SELECT * FROM users WHERE uid = :uid LIMIT 1")
    suspend fun getUserByUid(uid: String): User?

    // Delete all users (for debugging or resetting)
    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()

    // Fetch LiveData of the user (to observe changes)
    @Query("SELECT * FROM users LIMIT 1")
    fun getUserLiveData(): LiveData<User?>
}





//@Dao
//interface UserDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertUser(user: User)
//
//    @Query("SELECT * FROM users LIMIT 1")
//    suspend fun getUser(): User?
//}

