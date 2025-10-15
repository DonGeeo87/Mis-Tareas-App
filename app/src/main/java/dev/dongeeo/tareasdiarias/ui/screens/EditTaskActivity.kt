package dev.dongeeo.tareasdiarias.ui.screens

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import dev.dongeeo.tareasdiarias.ui.theme.*
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import java.util.Calendar

class EditTaskActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val initialTitle = intent.getStringExtra(EXTRA_TITLE) ?: ""
        val initialDesc = intent.getStringExtra(EXTRA_DESC) ?: ""
        setContent {
            TareasDiariasTheme {
                EditTaskScreen(
                    initialTitle = initialTitle,
                    initialDescription = initialDesc,
                    onSave = { title, desc, reminderAt ->
                        val data = Intent().apply {
                            putExtra(EXTRA_TITLE, title)
                            putExtra(EXTRA_DESC, desc)
                            putExtra(EXTRA_REMINDER_AT, reminderAt)
                        }
                        setResult(Activity.RESULT_OK, data)
                        finish()
                    },
                    onCancel = { finish() }
                )
            }
        }
    }

    companion object {
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_DESC = "extra_desc"
        const val EXTRA_REMINDER_AT = "extra_reminder_at"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditTaskScreen(
    initialTitle: String,
    initialDescription: String,
    onSave: (String, String, Long?) -> Unit,
    onCancel: () -> Unit
) {
    var title by remember { mutableStateOf(initialTitle) }
    var desc by remember { mutableStateOf(initialDescription) }
    var reminderAtMillis by remember { mutableStateOf<Long?>(null) }
    val calendar = remember { Calendar.getInstance() }
    val ctx = androidx.compose.ui.platform.LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = if (initialTitle.isBlank()) "Nueva tarea" else "Editar tarea",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onCancel) { 
                        Icon(
                            Icons.Filled.ArrowBack, 
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onPrimary
                        ) 
                    }
                },
                actions = {
                    IconButton(
                        onClick = { if (title.isNotBlank()) onSave(title.trim(), desc.trim(), reminderAtMillis) }
                    ) {
                        Icon(
                            Icons.Filled.Save, 
                            contentDescription = "Guardar",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = androidx.compose.ui.graphics.Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.background(
                    brush = CorporateGradient
                )
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyLarge,
                label = { Text("Título") },
                isError = title.isBlank(),
                supportingText = { 
                    if (title.isBlank()) Text("El título es obligatorio") 
                },
                shape = MaterialTheme.shapes.medium
            )
            OutlinedTextField(
                value = desc,
                onValueChange = { desc = it },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                textStyle = MaterialTheme.typography.bodyLarge,
                label = { Text("Descripción") },
                shape = MaterialTheme.shapes.medium
            )
            // Sección de recordatorio
            Text(
                text = "Recordatorio",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
            )
            
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = {
                        val now = Calendar.getInstance()
                        DatePickerDialog(
                            ctx,
                            { _, y, m, d ->
                                TimePickerDialog(
                                    ctx,
                                    { _, hour, min ->
                                        calendar.set(y, m, d, hour, min, 0)
                                        reminderAtMillis = calendar.timeInMillis
                                    },
                                    now.get(Calendar.HOUR_OF_DAY),
                                    now.get(Calendar.MINUTE),
                                    true
                                ).show()
                            },
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    modifier = Modifier.weight(1f)
                ) { 
                    Text(
                        text = if (reminderAtMillis != null) "Cambiar recordatorio" else "Agregar recordatorio",
                        style = MaterialTheme.typography.labelLarge
                    ) 
                }
                
                if (reminderAtMillis != null) {
                    OutlinedButton(
                        onClick = { reminderAtMillis = null },
                        modifier = Modifier.weight(1f)
                    ) { 
                        Text(
                            text = "Quitar",
                            style = MaterialTheme.typography.labelLarge
                        ) 
                    }
                }
            }

            // Botones de acción
            Row(
                Modifier.fillMaxWidth().padding(top = 32.dp), 
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                var isSavePressed by remember { mutableStateOf(false) }
                val saveScale by animateFloatAsState(
                    targetValue = if (isSavePressed) 0.95f else 1f,
                    animationSpec = tween(100),
                    label = "saveScale"
                )
                
                Button(
                    onClick = { 
                        isSavePressed = true
                        onSave(title.trim(), desc.trim(), reminderAtMillis)
                    }, 
                    enabled = title.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = androidx.compose.ui.graphics.Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .scale(saveScale)
                        .background(
                            brush = CorporateGradient,
                            shape = MaterialTheme.shapes.medium
                        )
                ) { 
                    Text(
                        text = "Guardar",
                        style = MaterialTheme.typography.labelLarge
                    ) 
                }
                
                var isCancelPressed by remember { mutableStateOf(false) }
                val cancelScale by animateFloatAsState(
                    targetValue = if (isCancelPressed) 0.95f else 1f,
                    animationSpec = tween(100),
                    label = "cancelScale"
                )
                
                OutlinedButton(
                    onClick = { 
                        isCancelPressed = true
                        onCancel()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .scale(cancelScale)
                        .background(
                            brush = CorporateGradientSubtle,
                            shape = MaterialTheme.shapes.medium
                        )
                ) { 
                    Text(
                        text = "Cancelar",
                        style = MaterialTheme.typography.labelLarge
                    ) 
                }
            }
        }
    }
}


