package dev.dongeeo.tareasdiarias.ui.components

import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.dongeeo.tareasdiarias.domain.Task
import dev.dongeeo.tareasdiarias.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TaskItem(
    task: Task,
    onClick: (Task) -> Unit,
    onLongPress: (Task) -> Unit = {},
    onQuickComplete: (Task) -> Unit = {},
    onQuickEdit: (Task) -> Unit = {},
    onQuickDelete: (Task) -> Unit = {},
    onQuickReminderToggle: (Task) -> Unit = {}
) {
    // Animaciones
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = tween(150),
        label = "scale"
    )
    
    val checkScale by animateFloatAsState(
        targetValue = if (task.isDone) 1.1f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "checkScale"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .scale(scale)
            .combinedClickable(
                onClick = { onClick(task) }, 
                onLongClick = { onLongPress(task) }
            )
            .alpha(if (task.isDone) 0.8f else 1f),
        colors = CardDefaults.cardColors(
            containerColor = if (task.isDone) 
                MaterialTheme.colorScheme.surfaceVariant 
            else 
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
            pressedElevation = 12.dp
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box {
            // Borde superior con gradiente
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(
                        brush = if (task.isDone) 
                            Brush.horizontalGradient(listOf(CorporateBlueAlpha, CorporatePurpleAlpha))
                        else 
                            CorporateGradient,
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
            )
            Column(Modifier.padding(16.dp)) {
                // Título con strikethrough si está completado
                Text(
                    text = task.title, 
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    textDecoration = if (task.isDone) TextDecoration.LineThrough else TextDecoration.None,
                    color = if (task.isDone) 
                        MaterialTheme.colorScheme.onSurfaceVariant 
                    else 
                        MaterialTheme.colorScheme.onSurface
                )
                
                // Descripción
                if (task.description.isNotBlank()) {
                    Text(
                        text = task.description, 
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                
                // Badge de recordatorio si existe
                if (task.reminderAtMillis != null) {
                    val df = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    AssistChip(
                        onClick = { onQuickReminderToggle(task) },
                        label = { 
                            Text(
                                text = df.format(Date(task.reminderAtMillis)),
                                style = MaterialTheme.typography.labelSmall
                            ) 
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Filled.Notifications, 
                                contentDescription = "Recordatorio",
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
                            labelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .background(
                                brush = CorporateGradientSubtle,
                                shape = RoundedCornerShape(12.dp)
                            )
                    )
                }
                
                // Acciones rápidas
                Row(
                    modifier = Modifier.padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { onQuickComplete(task) },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            Icons.Filled.CheckCircle, 
                            contentDescription = "Completar",
                            tint = if (task.isDone) SuccessGreen else MaterialTheme.colorScheme.outline,
                            modifier = Modifier
                                .size(28.dp)
                                .scale(checkScale)
                        )
                    }
                    IconButton(
                        onClick = { onQuickEdit(task) },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            Icons.Filled.Edit, 
                            contentDescription = "Editar",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    IconButton(
                        onClick = { onQuickDelete(task) },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            Icons.Filled.Delete, 
                            contentDescription = "Borrar",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    IconButton(
                        onClick = { onQuickReminderToggle(task) },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            Icons.Filled.Notifications, 
                            contentDescription = "Recordatorio",
                            tint = if (task.reminderAtMillis != null) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}


