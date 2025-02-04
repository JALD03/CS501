package com.example.staticcart

import android.graphics.Paint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.staticcart.ui.theme.StaticCartTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StaticCartTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Cart(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Cart(modifier: Modifier = Modifier) {
    var clicked by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    Column(modifier = Modifier.padding(30.dp),
        verticalArrangement = Arrangement.Center){
        Row() {
            Text(text = "Plunger", fontSize = 15.sp)
            Text(text = " $20", fontSize = 15.sp)
            Text(text = "QTY: 3", fontSize = 15.sp, modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End)
        }
        Divider(color = Color.Black)
        Row() {
            Text(text = "Toilet Paper", fontSize = 15.sp)
            Text(text = " $5", fontSize = 15.sp)
            Text(text = "QTY: 10", fontSize = 15.sp, modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End)
        }
        Divider(color = Color.Black)
        Row() {
            Text(text = "Toothbrush", fontSize = 15.sp)
            Text(text = " $3", fontSize = 15.sp)
            Text(text = "QTY: 1", fontSize = 15.sp, modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End)
        }
        Divider(color = Color.Black)
        Row() {
            Text(text = "Toothpaste", fontSize = 15.sp)
            Text(text = " $1000", fontSize = 15.sp)
            Text(text = "QTY: 1", fontSize = 15.sp, modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End)
        }
        Divider(color = Color.Black)
        Spacer(modifier = Modifier.height(550.dp))
        Row(){
            Text(text = "Total: $1,113")
        }
        Divider(color = Color.Black)
        Spacer(modifier = Modifier.height(20.dp))
        Row(){
            Button(onClick = {clicked = true
                scope.launch {
                }}){
                Text( text = "Checkout")
            }
            if (clicked){
                Snackbar { Text("Ordered") }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CartPreview() {
    StaticCartTheme {
        Cart()
    }
}