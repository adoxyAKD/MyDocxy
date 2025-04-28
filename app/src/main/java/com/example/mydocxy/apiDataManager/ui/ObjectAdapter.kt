package com.example.mydocxy.apiDataManager.ui


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mydocxy.R
import com.example.mydocxy.apiDataManager.database.ObjectData

class ObjectAdapter(
    private val onDelete: (ObjectData) -> Unit,
    private val onUpdate: (ObjectData) -> Unit
) : RecyclerView.Adapter<ObjectAdapter.ObjectViewHolder>() {

    var objects: MutableList<ObjectData> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_object, parent, false)
        return ObjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: ObjectViewHolder, position: Int) {
        val objectData = objects[position]
        holder.bind(objectData)
    }

    override fun getItemCount(): Int = objects.size

    fun submitList(newObjects: List<ObjectData>) {
        objects.clear()
        objects.addAll(newObjects)
        notifyDataSetChanged()
    }

    // Update local list after an update action
    fun updateObjectInList(updatedObject: ObjectData) {
        val index = objects.indexOfFirst { it.id == updatedObject.id }
        if (index != -1) {
            objects[index] = updatedObject
            notifyItemChanged(index)
        }
    }

    inner class ObjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.textViewTitle)
        private val btnDelete: Button = itemView.findViewById(R.id.buttonDelete)
        private val btnUpdate: Button = itemView.findViewById(R.id.buttonUpdate)

        fun bind(objectData: ObjectData) {
            nameTextView.text = objectData.name

            // Delete button functionality
            btnDelete.setOnClickListener {
                onDelete(objectData)
            }

            // Update button functionality
            btnUpdate.setOnClickListener {
                onUpdate(objectData)
            }
        }
    }
}

//
//class ObjectAdapter(
//    private val onDelete: (ObjectData) -> Unit,
//    private val onUpdate: (ObjectData) -> Unit
//) : ListAdapter<ObjectData, ObjectAdapter.ObjectViewHolder>(DiffCallback()) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_object, parent, false)
//        return ObjectViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ObjectViewHolder, position: Int) {
//        val objectData = getItem(position)
//        holder.bind(objectData)
//    }
//
//    inner class ObjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        private val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
//        private val subtitleTextView: TextView = itemView.findViewById(R.id.textViewSubtitle)
//        private val updateButton: Button = itemView.findViewById(R.id.buttonUpdate)
//        private val deleteButton: Button = itemView.findViewById(R.id.buttonDelete)
//
//        fun bind(obj: ObjectData) {
//            titleTextView.text = obj.name
//            subtitleTextView.text = obj.color ?: "No Color"
//
//            updateButton.setOnClickListener {
//                onUpdate(obj)
//            }
//            deleteButton.setOnClickListener {
//                onDelete(obj)
//            }
//        }
//    }
//
//    class DiffCallback : DiffUtil.ItemCallback<ObjectData>() {
//        override fun areItemsTheSame(oldItem: ObjectData, newItem: ObjectData): Boolean {
//            return oldItem.id == newItem.id
//        }
//
//        override fun areContentsTheSame(oldItem: ObjectData, newItem: ObjectData): Boolean {
//            return oldItem == newItem
//        }
//    }
//}


//
//class ObjectAdapter(
//    private val onDelete: (ObjectData) -> Unit
//) : ListAdapter<ObjectData, ObjectAdapter.ObjectViewHolder>(DiffCallback()) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_object, parent, false)
//        return ObjectViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ObjectViewHolder, position: Int) {
//        val objectData = getItem(position)
//        holder.bind(objectData)
//    }
//
//    inner class ObjectViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        private val title: TextView = view.findViewById(R.id.textViewTitle)
//        private val deleteButton: Button = view.findViewById(R.id.buttonDelete)
//
//        fun bind(obj: ObjectData) {
//            title.text = obj.name
//            deleteButton.setOnClickListener {
//                onDelete(obj)
//            }
//        }
//    }
//
//    class DiffCallback : DiffUtil.ItemCallback<ObjectData>() {
//        override fun areItemsTheSame(oldItem: ObjectData, newItem: ObjectData) = oldItem.id == newItem.id
//        override fun areContentsTheSame(oldItem: ObjectData, newItem: ObjectData) = oldItem == newItem
//    }
//}
//
//

//class ObjectAdapter(private val onDelete: (ObjectData) -> Unit) :
//    RecyclerView.Adapter<ObjectAdapter.ObjectViewHolder>() {
//
//    private var items = listOf<ObjectData>()
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_object, parent, false)
//        return ObjectViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ObjectViewHolder, position: Int) {
//        val objectData = items[position]
//        holder.bind(objectData)
//    }
//
//    override fun getItemCount(): Int = items.size
//
//    fun submitList(newItems: List<ObjectData>) {
//        items = newItems
//        notifyDataSetChanged()
//    }
//
//    inner class ObjectViewHolder(private val view: android.view.View) :
//        RecyclerView.ViewHolder(view) {
//
//        fun bind(objectData: ObjectData) {
//            // Set object name on TextView
//            view.findViewById<android.widget.TextView>(R.id.textViewTitle).text = objectData.name
//
//            // Handle delete button click
//            view.findViewById<android.widget.Button>(R.id.buttonDelete).setOnClickListener {
//                onDelete(objectData)
//            }
//        }
//    }
//}









