package com.example.staticuser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.staticuser.ui.theme.StaticUserTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StaticUserTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    User(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun User(modifier: Modifier = Modifier) {
    //From the snackbar documentation
    var clicked by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        Row(){
            Image( painter = painterResource(id = R.drawable.profile_pic),
                contentDescription = "Profile Picture",
                modifier = Modifier.size(150.dp).clip(CircleShape))
            Spacer(modifier = Modifier.width(8.dp))
            Text( text = "BagMan" , fontSize = 50.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text( text = "Yo it's BagMan, follow if you'd like to see all bag related news and awesome bag images")
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {clicked = true
            scope.launch {
            }}){
            Text( text = "Follow")
        }
        if (clicked){
            Snackbar { Text("Following") }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserPreview() {
    StaticUserTheme {
        User()
    }
}