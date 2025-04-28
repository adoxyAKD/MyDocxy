package com.example.mydocxy.apiDataManager.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.*

@Dao
interface ObjectDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertObjects(objects: List<ObjectData>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(objects: List<ObjectData>)

    @Delete
    suspend fun deleteObject(obj: ObjectData)

    @Update
    suspend fun updateObject(obj: ObjectData)

    @Query("SELECT * FROM objects")
    fun getAllObjects(): LiveData<List<ObjectData>>
}


