package com.example.mydocxy.apiDataManager.repository

import com.example.mydocxy.apiDataManager.api.ApiService
import com.example.mydocxy.apiDataManager.database.ObjectData
import com.example.mydocxy.apiDataManager.database.ObjectDataDao


class ObjectRepository(
    private val apiService: ApiService,
    private val objectDao: ObjectDataDao
) {

    // Refresh objects (fetch from API)
    suspend fun refreshObjects(): List<ObjectData> {
        // Make the API call to get objects
        val response = apiService.getObjects()

        if (response.isSuccessful) {
            // Parse the response into ObjectData objects without using the 'color' field
            val objectsWrapper = response.body() // Assuming the body is a List of ObjectDataWrapper

            // Map the response into ObjectData without color
            return objectsWrapper?.map { objectWrapper ->
                ObjectData(
                    id = objectWrapper.id,
                    name = objectWrapper.name,
                    capacity = objectWrapper.capacity,
                    price = objectWrapper.price,
                    description = objectWrapper.description
                )
            } ?: emptyList()
        } else {
            // Handle error or fallback
            throw Exception("Failed to fetch objects from API")
        }
    }

    // Delete object from database
    suspend fun deleteObject(obj: ObjectData) {
        objectDao.deleteObject(obj)
    }

    // Update object in the database
    suspend fun updateObject(obj: ObjectData, updatedFields: Map<String, Any?>) {
        objectDao.updateObject(obj)
    }
}









/*
class ObjectRepository(
    private val apiService: ApiService,
    private val dao: ObjectDataDao
) {

    val allObjects: LiveData<List<ObjectData>> = dao.getAllObjects()

    suspend fun refreshObjects() {
        val response = apiService.fetchObjects()
        if (response.isSuccessful) {
            response.body()?.let { objectApiResponses ->
                val objectDataList = objectApiResponses.map {
                    ObjectData(
                        id = it.id,
                        name = it.name,
                        color = it.data?.color,
                        capacity = it.data?.capacity,
                        price = it.data?.price,
                        description = it.data?.description
                    )
                }
                dao.insertObjects(objectDataList)
            }
        } else {
            throw Exception("Server Error: ${response.message()}")
        }
    }

    suspend fun updateObject(obj: ObjectData) {
        dao.updateObject(obj)
    }

    suspend fun deleteObject(obj: ObjectData) {
        dao.deleteObject(obj)
    }
}


 */



