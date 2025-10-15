package dev.dongeeo.tareasdiarias.domain.usecase

import dev.dongeeo.tareasdiarias.data.TaskRepository
import dev.dongeeo.tareasdiarias.domain.Task

class UpdateTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task) {
        repository.update(task.copy(updatedAtMillis = System.currentTimeMillis()))
    }
}


