package com.example.buttonworld

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.buttonworld.ui.theme.ButtonWorldTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ButtonWorldTheme {
                ButtonWork()
                }
            }
        }
    }


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ButtonWorldTheme {
        Greeting("Android")
    }
}

@Composable
fun ButtonWork(){
    var message by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .padding(48.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        ){
        FilledButtonExample(onClick = { message = "World" })
        if (message == "World") {
            Greeting(name = message)
        }
    }
    }

@Composable
fun FilledButtonExample(onClick: () -> Unit){
    Button(onClick = { onClick() }) {

    }
}