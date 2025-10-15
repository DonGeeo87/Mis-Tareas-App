package dev.dongeeo.tareasdiarias.domain.usecase

import dev.dongeeo.tareasdiarias.data.TaskRepository
import dev.dongeeo.tareasdiarias.domain.Task
import kotlinx.coroutines.flow.Flow

class GetTasksUseCase(private val repository: TaskRepository) {
    operator fun invoke(): Flow<List<Task>> = repository.observeTasks()
}


