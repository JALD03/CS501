package com.example.staticmusic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.foundation.Image
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.staticmusic.ui.theme.StaticMusicTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StaticMusicTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Player(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Player(modifier: Modifier = Modifier){

    Column(
        //Used AI to figure out the alignment here, since I couldn't get it to stop looking weird
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Image( painter = painterResource(id = R.drawable.album_cover),
            contentDescription = "Album Cover",
            modifier = Modifier.size(300.dp).align(Alignment.CenterHorizontally),
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text( text = "EoO", fontSize = 25.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
        Text( text = "Bad Bunny", fontSize = 25.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.height(10.dp))
        Box( modifier = Modifier.align(Alignment.CenterHorizontally) ){
            Row {
                Button(onClick = { }) {
                    Icon(
                        painter = painterResource(id = R.drawable.play),
                        contentDescription = "Play",
                        modifier = Modifier.size(50.dp)
                    )
                }
                Button(onClick = { }) {
                    Icon(
                        painter = painterResource(id = R.drawable.pause2),
                        contentDescription = "Pause",
                        modifier = Modifier.size(50.dp)
                    )
                }
                Button(onClick = { }) {
                    Icon(
                        painter = painterResource(id = R.drawable.skip),
                        contentDescription = "Skip",
                        modifier = Modifier.size(50.dp)
                    )
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PlayerPreview() {
    StaticMusicTheme {
        Player()
    }
}