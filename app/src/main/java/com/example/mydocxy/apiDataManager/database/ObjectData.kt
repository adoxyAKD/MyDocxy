package com.example.mydocxy.apiDataManager.database


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


@Entity(tableName = "objects")
data class ObjectData(
    @PrimaryKey
    val id: String,
    val name: String,
    val color: String? = null,
    val capacity: String? = null,
    val price: Double? = null,
    val description: String? = null
)




