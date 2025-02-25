package com.example.hangman

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hangman.ui.theme.HangManTheme
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HangManTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HangManGame(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun HangManGame(modifier: Modifier = Modifier) {
    val config = LocalConfiguration.current
    val gameHandler = remember { Gamehandler() }
    gameHandler.NewGame()

    if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        Row(modifier = modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(1f)) {
                StickmanDrawer(gameHandler)
            }
            Column(modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center)
            {
                UnderscoreDisplay(gameHandler)
                Spacer(modifier = Modifier.height(10.dp))
                LetterButtonPanel(gameHandler)
            }
        }
    } else {
        Column(modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            StickmanDrawer(gameHandler)
            Spacer(modifier = Modifier.height(16.dp))
            UnderscoreDisplay(gameHandler)
            Spacer(modifier = Modifier.height(16.dp))
            LetterButtonPanel(gameHandler)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HangManGamePreview() {
    HangManTheme {
        HangManGame()
    }
}
