package dev.dongeeo.tareasdiarias.domain

data class Task(
    val id: Long = 0L,
    val title: String,
    val description: String,
    val reminderAtMillis: Long?,
    val isDone: Boolean = false,
    val createdAtMillis: Long,
    val updatedAtMillis: Long
)


