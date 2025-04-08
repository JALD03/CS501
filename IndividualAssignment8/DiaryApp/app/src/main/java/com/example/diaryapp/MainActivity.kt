package com.example.diaryapp

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.FileNotFoundException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.launch

val Context.dataStore by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                Surface(modifier = Modifier.fillMaxSize()) {
                    DiaryApp()
                }
            }
        }
    }
@Composable
fun DiaryApp() {
    var showCalendar by remember { mutableStateOf(true) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    if (showCalendar) {
        CalendarScreen(onDateSelected = { date ->
            selectedDate = date
            showCalendar = false
        })
    } else {
        DiaryScreen(date = selectedDate, onBack = { showCalendar = true })
    }
}
@Composable
fun CalendarScreen(onDateSelected: (LocalDate) -> Unit) {
    //Used AI and documentation to get a better understanding of java.time
    val today = LocalDate.now()
    val startDay = today.withDayOfMonth(1).minusDays(today.withDayOfMonth(1).dayOfWeek.ordinal.toLong())
    val days = (0 until today.month.length(today.isLeapYear)).map { startDay.plusDays(it.toLong()) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Daily Journal",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .size(size = 48.dp),
            textAlign = TextAlign.Center
        )
        Text("Select today's Date")
        Spacer(modifier = Modifier.height(16.dp))
        days.chunked(3).forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                row.forEach { date ->
                    Button(
                        onClick = { onDateSelected(date) },
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp),
                    ) {
                        Text(
                            date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun DiaryScreen(date: LocalDate, onBack: () -> Unit) {
    //tried my best to model everything after Internal Storage App with all the
    //extra stuff I needed to add
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var entryText by remember { mutableStateOf("") }
    val fontSizeKey = intPreferencesKey("font_size")
    var fontSize by remember { mutableStateOf(16) }
    LaunchedEffect(context) {
        context.dataStore.data.collect { preferences ->
            fontSize = preferences[fontSizeKey] ?: 16
        }
    }
    LaunchedEffect(date) {
        val fileName = "$date.txt"
        entryText = readFromFile(context, fileName)
    }
    val fileName = "$date.txt"
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        //Used AI and documentation to find a nice way to format date according to
        //the day selected
        Text("Diary Entry - ${date.format(DateTimeFormatter.ISO_DATE)}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        OutlinedTextField(
            value = entryText,
            onValueChange = { entryText = it },
            label = { Text("Write your diary entry!") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            textStyle = LocalTextStyle.current.copy(fontSize = fontSize.sp)
        )
        Button(onClick = {
            saveToFile(context, fileName, entryText)
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
        }) {
            Text("Save")
        }
        Button(onClick = {
            val deleted = deleteFile(context, fileName)
            val msg = if (deleted) "Deleted" else "No entry found"
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            if (deleted) entryText = ""
        }) {
            Text("Delete")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Font size: $fontSize", modifier = Modifier.weight(1f))
            Button(onClick = {
                fontSize += 2
                scope.launch {
                    context.dataStore.edit { it[fontSizeKey] = fontSize }
                }
            }) { Text("A+") }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                //Used AI and documentation to better understand how I could
                //edit the fontsize and have it be consistent throughout the whole app
                fontSize = (fontSize - 2).coerceAtLeast(12)
                scope.launch {
                    context.dataStore.edit { it[fontSizeKey] = fontSize }
                }
            }) { Text("a-") }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onBack) {
            Text("Back to Calendar")
        }
    }
}

// Function to save text to internal storage
fun saveToFile(context: Context, filename: String, content: String) {
    context.openFileOutput(filename, Context.MODE_PRIVATE).use {
        it.write(content.toByteArray())
    }
}
// Function to read text from internal storage
fun readFromFile(context: Context, filename: String): String {
    return try {
        context.openFileInput(filename).bufferedReader().useLines { lines ->
            lines.joinToString("\n")
        }
    } catch (e: FileNotFoundException) {
        //Whenever this had text it would show up as if it was a diary entry so I removed it
        ""
    }
}
// Function to delete file from internal storage
fun deleteFile(context: Context, filename: String): Boolean {
    return context.deleteFile(filename)
}
