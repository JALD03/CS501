package com.example.polyapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.polyapp.ui.theme.PolyAppTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PolyAppTheme {
                    MapWithOverlays()
                }
            }
        }
    }


@Composable
fun MapWithOverlays() {
    val context = LocalContext.current
    // Used AI to give me to coordinates of a park and hiking trail in massachussets
    val trailCords = remember {
        listOf(
            LatLng(42.6455, -73.1684),
            LatLng(42.6482, -73.1655),
            LatLng(42.6526, -73.1606),
            LatLng(42.6526, -73.1586)
        )
    }
    val parkCords = remember {
        listOf(
            LatLng(42.6386, -73.1754),
            LatLng(42.6586, -73.1554),
            LatLng(42.6686, -73.1654),
            LatLng(42.6486, -73.1854)
        )
    }
    var polylineColor by remember { mutableStateOf(Color.Blue) }
    var polylineWidth by remember { mutableStateOf(5f) }
    var polygonColor by remember { mutableStateOf(Color.Green.copy(alpha = 0.3f)) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(trailCords.first(), 12f)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.weight(1f),
            cameraPositionState = cameraPositionState
        ) {
            Polyline(
                points = trailCords,
                color = polylineColor,
                width = polylineWidth,
                clickable = true,
                onClick = { polyline ->
                    Toast.makeText(
                        context,
                        //Used AI for descriptions 
                        "Thunderbolt Trail: 3 miles, Difficulty: Challenging",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )

            Polygon(
                points = parkCords,
                fillColor = polygonColor,
                strokeColor = Color.Green,
                strokeWidth = 2f,
                clickable = true,
                onClick = { polygon ->
                    Toast.makeText(
                        context,
                        "Mount Greylock State Reservation",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Hiking Trail Width: ${polylineWidth.toInt()} dp")
            Slider(
                value = polylineWidth,
                onValueChange = { polylineWidth = it },
                valueRange = 1f..20f,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Trail Color")
            ColorPicker(
                currentColor = polylineColor,
                onColorChange = { polylineColor = it }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Park Area Color")
            ColorPicker(
                currentColor = polygonColor,
                onColorChange = { polygonColor = it }
            )
        }
    }
}

@Composable
fun ColorPicker(currentColor: Color, onColorChange: (Color) -> Unit) {
    val colorPickerAlert = remember { mutableStateOf(false) }
    if (colorPickerAlert.value) {
        ColorAlert(
            currentColor = currentColor,
            onColorSelected = { selectedColor ->
                onColorChange(selectedColor)
                colorPickerAlert.value = false
            },
            onDismiss = { colorPickerAlert.value = false }
        )
    }
    Box(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = { colorPickerAlert.value = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Customize Color")
        }
    }
}

@Composable
fun ColorAlert(
    currentColor: Color,
    onColorSelected: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    val colorOptions = listOf(
        Color.Red, Color.Blue, Color.Green, Color.Yellow,
        Color.Gray, Color.Black
    )
    //Used AI and documentation to understand how AlertDialog popups worked and
    //how to keep those changes after the alert closes
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Choose Color") },
        text = {
            Column {
                colorOptions.forEach { color ->
                    Button(
                        onClick = {
                            onColorSelected(color)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = color
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .height(50.dp)
                    ) {
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PolyAppTheme {
        MapWithOverlays()
    }
}