package com.example.soundmeter

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.soundmeter.ui.theme.SoundMeterTheme
import kotlin.math.absoluteValue
import kotlin.math.log10

class MainActivity : ComponentActivity() {
    private var audioRecord: AudioRecord? = null
    private var isRecording = false
    private var bufferSize = 0
    private var _decibelLevel by mutableStateOf(0f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setupMicrophone()
        setContent {
            SoundMeterTheme {
                SoundMeterApp(
                    decibelLevel = _decibelLevel,
                    startRecording = { startRecording() })
            }
        }
    }
// Here I had to look up a lot on the documentation and used some AI when I was
    //Confused about my errors since I was very inexperienced with the xml file and
    // didn't know what classes I needed to work with that while also working with
    //the audio
    private fun setupMicrophone() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.RECORD_AUDIO), 1
            )
            return
        }
        bufferSize = AudioRecord.getMinBufferSize(
            44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT
        )
        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            44100, AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize
        )
    }
    private fun startRecording() {
        if (isRecording) return
        isRecording = true
        audioRecord?.startRecording()
        Thread {
            val buffer = ShortArray(bufferSize)
            while (isRecording) {
                val read = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                if (read > 0) {
                    //Here we get the input of the microphone in amplitude and
                    //turn it into decibel
                    val amplitude = buffer.maxOf { it.toInt().absoluteValue }
                    _decibelLevel = 20 * log10(amplitude.toDouble()).toFloat()
                }
            }
        }.start()
    }
    private fun stopRecording() {
        isRecording = false
        audioRecord?.stop()
    }
    override fun onResume() {
        super.onResume()
        startRecording()
    }
    override fun onPause() {
        super.onPause()
        stopRecording()
    }
}
//I used AI and the documentation to understand how to ask the user for permission
//since I was constantly getting errors and I never really had fiddled with the
//AndroidManifest.xml before
@Composable
fun RequestMicrophonePermission(onPermissionGranted: () -> Unit) {
    val context = LocalContext.current
    var hasPermission by remember { mutableStateOf(false) }

    LaunchedEffect(context) {
        hasPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
        if (isGranted) onPermissionGranted()
    }
    LaunchedEffect(Unit) {
        if (!hasPermission) {
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        } else {
            onPermissionGranted()
        }
    }
    if (!hasPermission) {
        Text("Microphone is needed", color = Color.Red,
            textAlign = TextAlign.Center)
    }
}
@Composable
fun SoundMeter(decibelLevel: Float) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Decibels: %.2f dB".format(decibelLevel), fontSize = 24.sp)
        //Looked up a way to do a progress bar thing and found this
        //found out you could add a conditional to the color which is cool
        LinearProgressIndicator(
            progress = { (decibelLevel / 100).coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(8.dp),
            color = if (decibelLevel > 70) Color.Red else Color.Green,
        )
        if (decibelLevel > 70) {
            Text("Too loud silly", color = Color.Red, fontSize = 20.sp)
        }
    }
}
//Putting it all together
@Composable
fun SoundMeterApp(
    decibelLevel: Float,
    startRecording: () -> Unit
) {
    var permissionGranted by remember { mutableStateOf(false) }

    RequestMicrophonePermission {
        permissionGranted = true
        startRecording()
    }
    if (permissionGranted) {
        SoundMeter(decibelLevel)
    }
}
