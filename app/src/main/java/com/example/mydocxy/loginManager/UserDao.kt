package com.example.mydocxy.loginManager

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: UserEntity)

    @Query("SELECT * FROM user_table WHERE uid = :uid")
    suspend fun getUser(uid: String): UserEntity?
}
