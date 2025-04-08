// A complete Jetpack Compose + Room + Flow demo app with full CRUD functionality
// Includes visual confirmation when a user is updated and shows users in a scrollable list.

package com.example.to_do

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// 1. Entity: Represents the table in SQLite using Room
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Auto-generated primary key
    val name: String, // User name field
    val isDone: Boolean = false      // Has the task been completed
)

// 2. DAO: Data Access Object defines DB operations
@Dao
interface TaskDao {
    // Return all users as a Flow to observe real-time changes
    @Query("SELECT * FROM tasks ORDER BY id DESC")
    fun getAll(): Flow<List<Task>>

    // Insert user (replace if conflict)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: Task)

    // Update user record
    @Update
    suspend fun update(user: Task)

    // Delete user record
    @Delete
    suspend fun delete(user: Task)
}

// 3. Room Database definition
@Database(entities = [Task::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        // Create or return singleton instance of DB
        fun getDatabase(context: android.content.Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "task_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}

// 4. ViewModel: Handles DB operations + state tracking
class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).taskDao()
    val tasks: Flow<List<Task>> = dao.getAll() // Observable list of users

    // Add new task
    fun addTask(name: String) {
        viewModelScope.launch {
            dao.insert(Task(name = name))
        }
    }

    // Update existing user
    fun updateTask(task: Task) {
        viewModelScope.launch {
            dao.update(task)
        }
    }

    // Delete user
    fun deleteUser(task: Task) {
        viewModelScope.launch {
            dao.delete(task)
        }
    }
}

// 5. MainActivity: Entry point
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val context = LocalContext.current
                // Get ViewModel using factory since it needs Application context
                val viewModel: UserViewModel = viewModel(
                    factory = ViewModelProvider.AndroidViewModelFactory.getInstance(
                        context.applicationContext as Application
                    )
                )
                TaskScreen(viewModel) // Call main screen
            }
        }
    }
}

// 6. Composable UI for managing users
@Composable
fun TaskScreen(viewModel: UserViewModel) {
    val tasks by viewModel.tasks.collectAsState(initial = emptyList())
    var filter by remember { mutableStateOf("All") }
    var name by remember { mutableStateOf("") }

    val filteredTasks by remember(tasks, filter) {
        derivedStateOf {
            when (filter) {
                "Completed" -> tasks.filter { it.isDone }
                "Pending" -> tasks.filter { !it.isDone }
                else -> tasks
            }
        }
    }

    Column(Modifier.padding(16.dp)) {
        // Input for user name
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Task Name") },
            modifier = Modifier.fillMaxWidth()
                .padding(top = 20.dp)
        )

        // Button to add task to DB
        Button(
            onClick = {
                if (name.isNotBlank()) {
                    viewModel.addTask(name)
                    name = "" // Clear input
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Add Task")
        }

        //Filtering UI
        Row(Modifier.padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            listOf("All", "Completed", "Pending").forEach { status ->
                Button(onClick = { filter = status }, modifier = Modifier.weight(1f).padding(horizontal = 4.dp)) {
                    Text(status)
                }
            }
        }
        // Scrollable list of users using LazyColumn
        LazyColumn {
            items(filteredTasks) { task ->
                var updatedName by remember(task.id) { mutableStateOf(task.name) }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(Modifier.weight(1f)) {
                        // Editable name field
                        OutlinedTextField(
                            value = updatedName,
                            onValueChange = { updatedName = it },
                            label = { Text("Edit Name") },
                            singleLine = true
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = task.isDone,
                                onCheckedChange = {
                                    viewModel.updateTask(task.copy(isDone = it))
                                }
                            )
                            Text("Completed")
                        }
                    }

                    Column {
                        // Update user button
                        Button(
                            onClick = {
                                if (updatedName.isNotBlank()) {
                                    val updatedTask = task.copy(
                                        name = updatedName,
                                    )
                                    viewModel.updateTask(updatedTask)
                                }
                            }
                        ) {
                            Text("Update")
                        }

                        Spacer(Modifier.height(4.dp))

                        // Delete user button
                        Button(onClick = { viewModel.deleteUser(task) }) {
                            Text("Delete")
                        }
                    }
                }
            }
        }
    }
}
