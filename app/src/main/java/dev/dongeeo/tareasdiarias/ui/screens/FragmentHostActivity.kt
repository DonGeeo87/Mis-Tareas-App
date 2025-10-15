package dev.dongeeo.tareasdiarias.ui.screens

import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import dev.dongeeo.tareasdiarias.R
import dev.dongeeo.tareasdiarias.ui.fragments.TaskDetailFragment

class FragmentHostActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_fragment_host)
        if (savedInstanceState == null) {
            val taskId = intent.getLongExtra(EXTRA_TASK_ID, 0L)
            supportFragmentManager.commit {
                replace(R.id.fragment_container, TaskDetailFragment.newInstance(taskId))
            }
        }
    }

    companion object {
        const val EXTRA_TASK_ID = "extra_task_id"
    }
}


