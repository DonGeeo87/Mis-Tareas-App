package dev.dongeeo.tareasdiarias.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val title: String,
    val description: String,
    val reminderAtMillis: Long?,
    val isDone: Boolean = false,
    val createdAtMillis: Long,
    val updatedAtMillis: Long
)


