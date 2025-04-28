package com.example.mydocxy.apiDataManager.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydocxy.apiDataManager.api.ApiService
import com.example.mydocxy.apiDataManager.database.AppDatabase
import com.example.mydocxy.apiDataManager.database.ObjectData
import com.example.mydocxy.apiDataManager.repository.ObjectRepository
import com.example.mydocxy.apiDataManager.ui.NotificationPreferencesManager
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.launch

class ObjectViewModel(application: Application) : AndroidViewModel(application) {

    private val objectRepository: ObjectRepository = ObjectRepository(ApiService.create(), AppDatabase.getDatabase(application).objectDataDao())
    private val _allObjects = MutableLiveData<List<ObjectData>>()
    val allObjects: LiveData<List<ObjectData>> get() = _allObjects

    init {
        refreshObjects() // Initial call to load objects
    }

    // Delete object
    fun deleteObject(obj: ObjectData) {
        viewModelScope.launch {
            try {
                // Delete the object from repository
                objectRepository.deleteObject(obj)

                // Remove from local data after deletion
                val updatedList = _allObjects.value?.toMutableList()
                updatedList?.remove(obj)
                _allObjects.postValue(updatedList)

                Toast.makeText(getApplication(), "${obj.name} deleted", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("ObjectViewModel", "Delete failed: ${e.message}")
                Toast.makeText(getApplication(), "Delete failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Update object with updated fields
    fun updateObject(obj: ObjectData, updatedFields: Map<String, Any?>) {
        viewModelScope.launch {
            try {
                // Update object in the repository
                objectRepository.updateObject(obj, updatedFields)

                // Update the local list directly without fetching again from the API
                val updatedObject = obj.copy(
                    name = updatedFields["name"] as? String ?: obj.name,
                    // Add other fields based on updatedFields map
                )

                // Update the LiveData list with the updated object
                val updatedList = _allObjects.value?.toMutableList() ?: mutableListOf()
                val index = updatedList.indexOfFirst { it.id == updatedObject.id }
                if (index != -1) {
                    updatedList[index] = updatedObject
                    _allObjects.postValue(updatedList)
                }

                Toast.makeText(getApplication(), "${updatedObject.name} updated", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("ObjectViewModel", "Update failed: ${e.message}")
            }
        }
    }

    // Fetch objects from the API
    fun refreshObjects() {
        viewModelScope.launch {
            try {
                // Fetch the objects from the API
                val response = ApiService.create().getObjects()

                if (response.isSuccessful) {
                    val objectsWrapper = response.body()
                    if (objectsWrapper != null) {
                        // Map API response to ObjectData
                        val objects = objectsWrapper.map { objectWrapper ->
                            ObjectData(
                                id = objectWrapper.id,
                                name = objectWrapper.name,
                                color = objectWrapper.color
                            )
                        }
                        // Post to LiveData
                        _allObjects.postValue(objects)
                    }
                }
            } catch (e: Exception) {
                Log.e("ObjectViewModel", "Error fetching objects", e)
            }
        }
    }
}


//
//class ObjectViewModel(private val repository: ObjectRepository) : ViewModel() {
//
//    val allObjects = repository.allObjects
//
//    // Delete object
//    fun deleteObject(obj: ObjectData) {
//        viewModelScope.launch {
//            try {
//                repository.deleteObject(obj)
//                refreshObjects()
//            } catch (e: Exception) {
//                Log.e("ObjectViewModel", "Delete failed: ${e.message}")
//            }
//        }
//    }
//
//    // Update object with updated fields
//    fun updateObject(obj: ObjectData, updatedFields: Map<String, Any?>) {
//        viewModelScope.launch {
//            try {
//                repository.updateObject(obj, updatedFields)
//                refreshObjects()
//            } catch (e: Exception) {
//                Log.e("ObjectViewModel", "Update failed: ${e.message}")
//            }
//        }
//    }
//
//
//    // Refresh data from API
//    fun refreshObjects() {
//        viewModelScope.launch {
//            repository.refreshObjects()
//        }
//    }
//}
