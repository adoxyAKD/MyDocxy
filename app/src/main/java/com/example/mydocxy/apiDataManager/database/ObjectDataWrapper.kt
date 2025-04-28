package com.example.mydocxy.apiDataManager.database

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import com.google.gson.annotations.SerializedName

//data class ObjectDataWrapper(
//    val id: String,
//    val name: String,
//    val color: String? // Directly map color here, assuming color is always a string in the response.
//)

//fun parseJson(json: String): List<ObjectData> {
//    val gson = Gson()
//    val listType = object : TypeToken<List<ObjectDataWrapper>>() {}.type
//    val objects = gson.fromJson<List<ObjectDataWrapper>>(json, listType)
//
//    return objects.map { objectWrapper ->
//        // Extracting color flexibly from both 'color' and 'Color' keys
//        val color = (objectWrapper.data?.get("color") as? String) ?: (objectWrapper.data?.get("Color") as? String)
//        val capacity = objectWrapper.data?.get("capacity") as? String // Example for extracting capacity
//        val price = objectWrapper.data?.get("price") as? Double // Example for extracting price
//
//        ObjectData(
//            id = objectWrapper.id,
//            name = objectWrapper.name,
//            color = color,
//            capacity = capacity,
//            price = price
//        )
//    }
//}
