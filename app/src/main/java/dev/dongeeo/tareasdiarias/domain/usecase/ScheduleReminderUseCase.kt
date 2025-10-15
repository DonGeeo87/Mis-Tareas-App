package dev.dongeeo.tareasdiarias.domain.usecase

import dev.dongeeo.tareasdiarias.domain.Task

interface ReminderScheduler {
    fun schedule(task: Task)
    fun cancel(taskId: Long)
}

class ScheduleReminderUseCase(private val scheduler: ReminderScheduler) {
    operator fun invoke(task: Task) {
        if (task.reminderAtMillis != null) scheduler.schedule(task)
    }
}


