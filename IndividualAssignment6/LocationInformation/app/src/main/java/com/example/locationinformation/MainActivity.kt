package com.example.locationinformation

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.locationinformation.ui.theme.LocationInformationTheme
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            LocationInformationTheme {
                var userAddress by remember { mutableStateOf<String?>(null) }
                var userLocation by remember { mutableStateOf<LatLng?>(null) }
                var markerList by remember { mutableStateOf(listOf<MarkerState>()) }

                RequestPermissions { location ->
                    userLocation = LatLng(location.latitude, location.longitude)
                    userAddress = getAddress(location.latitude, location.longitude)
                    markerList = listOf(MarkerState(position = userLocation!!))
                }

                userLocation?.let { MainScreen(location = it, address = userAddress, markerList = markerList
                    ) { latLng ->
                        markerList = markerList + MarkerState(position = latLng)
                    }
                }
            }
        }
    }

    @Composable
    fun RequestPermissions(onLocationReceived: (Location) -> Unit) {
        val context = LocalContext.current
        var hasLocationPermission by remember {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            )
        }

        val requestPermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            hasLocationPermission = isGranted
            if (isGranted) fetchLocation(onLocationReceived)
        }

        LaunchedEffect(hasLocationPermission) {
            if (!hasLocationPermission) {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            } else {
                fetchLocation(onLocationReceived)
            }
        }
    }

    private fun fetchLocation(onLocationReceived: (Location) -> Unit) {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let { onLocationReceived(it) }
            }
        } catch (e: SecurityException) {
            Log.e("Location", "Security Exception: ${e.message}")
        }
    }

    private fun getAddress(latitude: Double, longitude: Double): String? {
        val geocoder = Geocoder(this)
        val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
        return if (!addresses.isNullOrEmpty()) {
            val address = addresses[0]
            "${address.getAddressLine(0)}, ${address.locality}, ${address.countryName}"
        } else {
            "Address not found"
        }
    }

    @Composable
    fun MainScreen(
        location: LatLng,
        address: String?,
        markerList: List<MarkerState>,
        onMapClick: (LatLng) -> Unit
    ) {
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.Builder()
                .target(location)
                .zoom(15f)
                .build()
        }

        Box {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapClick = { latLng ->
                    onMapClick(latLng)
                }
            ) {
                markerList.forEach { markerState ->
                    Marker(state = markerState)
                }
            }

            Text(
                text = "Address: $address",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
            )
        }
    }
}
