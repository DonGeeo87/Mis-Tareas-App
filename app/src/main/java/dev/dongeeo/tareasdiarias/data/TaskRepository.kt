package dev.dongeeo.tareasdiarias.data

import dev.dongeeo.tareasdiarias.domain.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepository(private val dao: TaskDao) {
    fun observeTasks(): Flow<List<Task>> = dao.observeTasks().map { list ->
        list.map { it.toDomain() }
    }

    suspend fun getById(id: Long): Task? = dao.getById(id)?.toDomain()

    suspend fun add(task: Task): Long = dao.insert(task.toEntity())

    suspend fun update(task: Task) = dao.update(task.toEntity())

    suspend fun delete(task: Task) = dao.delete(task.toEntity())
}

private fun TaskEntity.toDomain(): Task = Task(
    id = id,
    title = title,
    description = description,
    reminderAtMillis = reminderAtMillis,
    isDone = isDone,
    createdAtMillis = createdAtMillis,
    updatedAtMillis = updatedAtMillis
)

private fun Task.toEntity(): TaskEntity = TaskEntity(
    id = id,
    title = title,
    description = description,
    reminderAtMillis = reminderAtMillis,
    isDone = isDone,
    createdAtMillis = createdAtMillis,
    updatedAtMillis = updatedAtMillis
)


