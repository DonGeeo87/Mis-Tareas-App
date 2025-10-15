package dev.dongeeo.tareasdiarias.domain.usecase

import dev.dongeeo.tareasdiarias.data.TaskRepository
import dev.dongeeo.tareasdiarias.domain.Task

class AddTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task): Long {
        return repository.add(task.copy(
            createdAtMillis = task.createdAtMillis,
            updatedAtMillis = System.currentTimeMillis()
        ))
    }
}


