package com.example.staticgallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Divider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Card
import androidx.compose.ui.tooling.preview.Preview
import com.example.staticgallery.ui.theme.StaticGalleryTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StaticGalleryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Gallery(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Gallery(modifier: Modifier = Modifier){
    Column (){
        Spacer( modifier = Modifier.height(20.dp))
        Row( modifier = Modifier.padding(start = 20.dp) ){
            Column() {
                Image(
                    painter = painterResource(id = R.drawable.beach036),
                    contentDescription = "Beach image",
                    modifier = Modifier.size(150.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "An image of a beach with crystal clear water and trees in the background.",
                    fontSize = 15.sp,
                    modifier = Modifier.width(150.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(){
            Image( painter = painterResource(id = R.drawable.pexels_photo_1054655),
                contentDescription = "Elephant Image",
                modifier = Modifier.size(150.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text( text = "An image of an elephant near a body of water as the day comes to and end.",
                fontSize = 15.sp,
                modifier = Modifier.width(150.dp))
            }
        }
        Spacer( modifier = Modifier.height(20.dp))
        Row( modifier = Modifier.padding(start = 20.dp) ){
            Column(){
            Image(painter = painterResource(id = R.drawable.pexels_photo_1563356),
                contentDescription = "Road image",
                modifier = Modifier.size(150.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text( text = "An image of an empty road with the fall foliage starting litter it with orange leaves.",
                fontSize = 15.sp,
                modifier = Modifier.width(150.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(){
            Image(painter = painterResource(id = R.drawable.pexels_photo_268533),
                contentDescription = "Tree image",
                modifier = Modifier.size(150.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text( text = "A tree stands alone under the nightsky with its reflection clear in a nearby body of water.",
                fontSize = 15.sp,
                modifier = Modifier.width(150.dp)
            ) }
        }
        Spacer( modifier = Modifier.height(20.dp))
        Row( modifier = Modifier.padding(start = 20.dp) ){
            Column() {
                Image(
                    painter = painterResource(id = R.drawable.pexels_photo_414612),
                    contentDescription = "Bridge image",
                    modifier = Modifier.size(150.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "A bridge extends towards a dock that rests in the middle of a lake surrounded by a forest.",
                    fontSize = 15.sp,
                    modifier = Modifier.width(150.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column()
            {

                Image(
                    painter = painterResource(id = R.drawable.pexels_photo_459225),
                    contentDescription = "Mountain image",
                    modifier = Modifier.size(150.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "A snow mountain is reflected off of a nearby lake while surrounded by trees",
                    fontSize = 15.sp,
                    modifier = Modifier.width(150.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GalleryPreview() {
    StaticGalleryTheme {
        Gallery()
    }
}