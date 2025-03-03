package com.example.altimeter

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.pow
import com.example.altimeter.ui.theme.AltimeterTheme

class MainActivity : ComponentActivity(), SensorEventListener{

    private lateinit var sensorManager: SensorManager
    private var pressureSensor: Sensor? = null

    private var _pressure by mutableStateOf(0f)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize Sensor Manager
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)

        setContent {
            AltimeterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AltimeterScreen(
                        pressure = _pressure,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

//Register the accelerometer sensor when the app starts (onResume).
override fun onResume() {
    super.onResume()
    pressureSensor?.let {
        sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
    }
}

// Unregister the sensor when the app is paused (onPause) to save battery
override fun onPause() {
    super.onPause()
    sensorManager.unregisterListener(this)
}

override fun onSensorChanged(event: SensorEvent?) {
    event?.let {
        _pressure = it.values[0]
    }
}

override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    // Not needed for this example
}
}

// Display the real-time accelerometer data
@Composable
fun AltimeterScreen(pressure: Float, modifier: Modifier) {
    val altitude = 44330* (1 - (pressure/1013.25).pow(1/5.255))

    //I used AI and documentation to find an efficient way to make a decaying exponential function
    //that worked inversely with the value of altitude, the scaleFactor serves to control how
    //quickly the screen darkens
    val scaleFactor = 10000f
    val brightness = kotlin.math.exp(-altitude / scaleFactor).toFloat()

    val backgroundColor = Color(
        red = brightness,
        green = brightness,
        blue = brightness
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Altimeter Data", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)

        Spacer(modifier = Modifier.height(16.dp))

        SensorValue(label = "Altitude", value = altitude.toFloat())
    }
}

@Composable
fun SensorValue(label: String, value: Float) {
    Text(
        text = "$label: ${"%.2f".format(value)}",
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium,
        color = Color.DarkGray
    )
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
  //  AccelerometerTheme {
    //    AccelerometerScreen(x = 0f, y = 0f, z = 0f)
    //}
//}