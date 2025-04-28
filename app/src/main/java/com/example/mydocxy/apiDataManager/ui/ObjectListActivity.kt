package com.example.mydocxy.apiDataManager.ui

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Switch
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mydocxy.R
import com.example.mydocxy.apiDataManager.api.ApiService
import com.example.mydocxy.apiDataManager.database.AppDatabase
import com.example.mydocxy.apiDataManager.repository.ObjectRepository
import com.example.mydocxy.apiDataManager.viewmodel.ObjectViewModel
import com.example.mydocxy.apiDataManager.viewmodel.ObjectViewModelFactory

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.example.mydocxy.apiDataManager.database.ObjectData
import kotlinx.coroutines.launch

class ObjectListActivity : AppCompatActivity() {

    private lateinit var viewModel: ObjectViewModel
    private lateinit var adapter: ObjectAdapter
    private lateinit var notificationPreferencesManager: NotificationPreferencesManager
    private lateinit var switchNotifications: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_object_list)

        // Preferences Manager
        notificationPreferencesManager = NotificationPreferencesManager(this)

        // Switch
        switchNotifications = findViewById(R.id.switchNotifications)
        switchNotifications.isChecked = notificationPreferencesManager.isNotificationsEnabled()

        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            notificationPreferencesManager.setNotificationsEnabled(isChecked)
            Toast.makeText(this, "Notifications ${if (isChecked) "Enabled" else "Disabled"}", Toast.LENGTH_SHORT).show()
        }

        // RecyclerView setup
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Adapter setup
        adapter = ObjectAdapter(
            onDelete = { obj ->
                // Call the delete method, also interact with ViewModel if needed
                deleteObject(obj)
                showNotification(obj)
                Toast.makeText(this@ObjectListActivity, "${obj.name} deleted", Toast.LENGTH_SHORT).show()
            },
            onUpdate = { obj ->
                // Show update dialog for the selected object
                showUpdateDialog(obj)
            }
        )
        recyclerView.adapter = adapter

        // ViewModel setup
        val factory = ObjectViewModelFactory(application) // Pass Application instance
        viewModel = ViewModelProvider(this, factory).get(ObjectViewModel::class.java)

        // Observe the changes from the ViewModel and update the adapter
        viewModel.allObjects.observe(this) { objects ->
            adapter.submitList(objects)
        }

        // Initially fetch or refresh objects
        viewModel.refreshObjects()
    }

    private fun deleteObject(obj: ObjectData) {
        // Assuming you want to remove the item from the local list, or you can interact with your repository to delete it.
        val updatedList = adapter.objects.toMutableList()
        updatedList.remove(obj)

        // Update RecyclerView after deletion
        adapter.submitList(updatedList)

        // If you have a repository and want to delete from database or API, call ViewModel
        lifecycleScope.launch {
            try {
                viewModel.deleteObject(obj)
            } catch (e: Exception) {
                Toast.makeText(this@ObjectListActivity, "Delete failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Show update dialog to update the selected object
    private fun showUpdateDialog(obj: ObjectData) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_update, null)
        val nameEditText: EditText = dialogView.findViewById(R.id.editTextName)
        nameEditText.setText(obj.name)

        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Update Object")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val updatedName = nameEditText.text.toString()

                // Create updated object with the new name
                val updatedObj = obj.copy(name = updatedName)

                // Call ViewModel to update the object
                lifecycleScope.launch {
                    try {
                        viewModel.updateObject(obj, mapOf("name" to updatedName))
                    } catch (e: Exception) {
                        Toast.makeText(this@ObjectListActivity, "Update failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                // Update the object in the list locally
                adapter.updateObjectInList(updatedObj)

                // Optionally show a toast confirmation
                Toast.makeText(this, "Object updated to: $updatedName", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .create()

        alertDialog.show()
    }




    private fun showNotification(obj: ObjectData) {
        val channelId = "delete_channel"

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Deleted Items", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Item Deleted")
            .setContentText("Deleted: ${obj.name}")
            .setSmallIcon(R.drawable.ic_launcher_background) // Add any small icon drawable
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}


//class ObjectListActivity : AppCompatActivity() {
//
//    private lateinit var viewModel: ObjectViewModel
//    private lateinit var adapter: ObjectAdapter
//    private lateinit var preferencesManager: NotificationPreferencesManager
//
//    private val CHANNEL_ID = "delete_channel"
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_object_list)
//
//        createNotificationChannel()
//
//        preferencesManager = NotificationPreferencesManager(this)
//
//        // Setup DB, API, Repository
//        val db = AppDatabase.getDatabase(this)
//        val apiService = ApiService.create()  // assuming you have companion object create() in ApiService
//        val repository = ObjectRepository(apiService, db.objectDataDao())
//        val viewModelFactory = ObjectViewModelFactory(repository)
//        viewModel = viewModels<ObjectViewModel> { viewModelFactory }.value
//
//        // Setup RecyclerView
//        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//
//        adapter = ObjectAdapter(onDelete = { obj ->
//            viewModel.deleteObject(obj)
//            if (preferencesManager.isNotificationsEnabled()) {
//                showDeleteNotification(obj.name)
//            }
//        })
//        recyclerView.adapter = adapter
//
//        // Setup Switch
//        val switchNotifications = findViewById<Switch>(R.id.switchNotifications)
//        switchNotifications.isChecked = preferencesManager.isNotificationsEnabled()
//        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
//            preferencesManager.setNotificationsEnabled(isChecked)
//            Toast.makeText(this, "Notifications ${if (isChecked) "Enabled" else "Disabled"}", Toast.LENGTH_SHORT).show()
//        }
//
//        // Observe Data
//        viewModel.allObjects.observe(this) { objects ->
//            adapter.submitList(objects)
//        }
//
//        // Refresh from API
//        viewModel.refreshObjects()
//    }
//
//    private fun showDeleteNotification(objectName: String) {
//        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
//            .setSmallIcon(R.drawable.ic_launcher_background) // you must have an icon
//            .setContentTitle("Item Deleted")
//            .setContentText("Deleted: $objectName")
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//
//        with(NotificationManagerCompat.from(this)) {
//            notify(System.currentTimeMillis().toInt(), builder.build())
//        }
//
//        Log.d("Notification", "Notification sent for deleted item: $objectName")
//    }
//
//    private fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name = "Delete Notifications"
//            val descriptionText = "Notifications when item is deleted"
//            val importance = NotificationManager.IMPORTANCE_HIGH
//            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
//                description = descriptionText
//            }
//            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//    }
//}




//
//class ObjectListActivity : AppCompatActivity() {
//
//    private lateinit var viewModel: ObjectViewModel
//    private lateinit var adapter: ObjectAdapter
//    private lateinit var notificationPreferencesManager: NotificationPreferencesManager
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_object_list)
//
//        // Initialize notification preferences manager
//        notificationPreferencesManager = NotificationPreferencesManager(this)
//
//        // Initialize DB, API, and Repository
//        val db = AppDatabase.getDatabase(this)
//        val apiService = ApiService.create()  // Correct method to create ApiService instance
//        val repository = ObjectRepository(apiService, db.objectDataDao(), notificationPreferencesManager)
//        val viewModelFactory = ObjectViewModelFactory(repository)
//        viewModel = viewModels<ObjectViewModel>(factoryProducer = { viewModelFactory }).value
//
//        // Setup RecyclerView and Adapter
//        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        adapter = ObjectAdapter(onDelete = { obj ->
//            viewModel.deleteObject(obj)  // Deleting object when clicked
//        })
//        recyclerView.adapter = adapter
//
//        // Observe the data
//        viewModel.allObjects.observe(this) { objects ->
//            adapter.submitList(objects)
//        }
//
//        // Fetch data from API and database
//        viewModel.refreshObjects()
//
//        // Setup Switch to enable/disable notifications
//        val switchNotifications = findViewById<Switch>(R.id.switchNotifications)
//
//        // Set the Switch state based on current preference (notifications enabled or disabled)
//        switchNotifications.isChecked = notificationPreferencesManager.isNotificationsEnabled()
//
//        // Toggle notification preference when switch is toggled
//        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
//            lifecycleScope.launch {
//                notificationPreferencesManager.setNotificationsEnabled(isChecked)
//                val message = if (isChecked) "Notifications Enabled" else "Notifications Disabled"
//                Toast.makeText(this@ObjectListActivity, message, Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//}
//
//
