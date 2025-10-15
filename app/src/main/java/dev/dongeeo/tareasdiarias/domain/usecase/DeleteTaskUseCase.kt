package dev.dongeeo.tareasdiarias.domain.usecase

import dev.dongeeo.tareasdiarias.data.TaskRepository
import dev.dongeeo.tareasdiarias.domain.Task

class DeleteTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task) {
        repository.delete(task)
    }
}


