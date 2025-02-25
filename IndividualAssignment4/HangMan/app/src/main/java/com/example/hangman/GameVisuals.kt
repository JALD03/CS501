package com.example.hangman

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UnderscoreDisplay(gameHandler: Gamehandler) {
    val dummy by remember { derivedStateOf { gameHandler.giveGuessed() } }
    Text(text = dummy.toString(), fontSize = 24.sp)
}

@Composable
fun StickmanDrawer(gameHandler: Gamehandler){
    val health by remember { derivedStateOf { gameHandler.giveHealth() } }
    //Used the documentation and AI to find out what canvas was and how it worked to represent the
    //stick man
    Canvas(modifier = Modifier.size(200.dp)) {
        val centerX = size.width / 2
        val startY = 20f
        val bodyHeight = 40f

        if (health <= 5) {
            drawCircle(Color.Black, radius = 15f, center = Offset(centerX, startY))
        }
        if (health <= 4) {
            drawLine(Color.Black, Offset(centerX, startY + 15f), Offset(centerX, startY + 15f + bodyHeight), strokeWidth = 5f)
        }
        if (health <= 3) {
            drawLine(Color.Black, Offset(centerX, startY + 25f), Offset(centerX - 20f, startY + 35f), strokeWidth = 5f)
        }
        if (health <= 2) {
            drawLine(Color.Black, Offset(centerX, startY + 25f), Offset(centerX + 20f, startY + 35f), strokeWidth = 5f)
        }
        if (health <= 1) {
            drawLine(Color.Black, Offset(centerX, startY + 55f), Offset(centerX - 15f, startY + 75f), strokeWidth = 5f)
        }
        if (health <= 0) {
            drawLine(Color.Black, Offset(centerX, startY + 55f), Offset(centerX + 15f, startY + 75f), strokeWidth = 5f)
        }
    }
}
