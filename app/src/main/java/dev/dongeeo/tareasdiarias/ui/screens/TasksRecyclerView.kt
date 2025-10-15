package dev.dongeeo.tareasdiarias.ui.screens

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.dongeeo.tareasdiarias.domain.Task

class TaskAdapter(private val onClick: (Task) -> Unit) : ListAdapter<Task, TaskVH>(DIFF) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskVH {
        val tv = TextView(parent.context)
        tv.setPadding(32, 32, 32, 32)
        return TaskVH(tv, onClick)
    }
    override fun onBindViewHolder(holder: TaskVH, position: Int) = holder.bind(getItem(position))
    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem
        }
    }
}

class TaskVH(private val tv: TextView, private val onClick: (Task) -> Unit) : RecyclerView.ViewHolder(tv) {
    private var current: Task? = null
    init { tv.setOnClickListener { current?.let(onClick) } }
    fun bind(task: Task) { current = task; tv.text = task.title }
}

@Composable
fun TasksRecyclerView(tasks: List<Task>, onClick: (Task) -> Unit) {
    AndroidView(factory = { context ->
        RecyclerView(context).apply {
            adapter = TaskAdapter(onClick)
        }
    }, update = { recycler ->
        (recycler.adapter as? TaskAdapter)?.submitList(tasks)
    })
}


