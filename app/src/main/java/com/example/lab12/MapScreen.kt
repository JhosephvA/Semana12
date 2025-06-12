package com.example.lab12

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import androidx.compose.runtime.remember

import androidx.compose.runtime.*
import com.google.android.gms.maps.model.BitmapDescriptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun MapScreen() {
    val context = LocalContext.current
    val arequipaLocation = LatLng(-16.4040102, -71.559611)
    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(arequipaLocation, 12f)
    }

    val descriptorState = remember { mutableStateOf<BitmapDescriptor?>(null) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.Default) {
            val originalBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.img)
            val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, 96, 96, false)
            val descriptor = BitmapDescriptorFactory.fromBitmap(scaledBitmap)
            descriptorState.value = descriptor
        }
    }

    val locations = listOf(
        LatLng(-16.433415, -71.5442652), // JLByR
        LatLng(-16.4205151, -71.4945209), // Paucarpata
        LatLng(-16.3524187, -71.5675994)  // Zamacola
    )

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            descriptorState.value?.let { descriptor ->
                // Marcador de Arequipa con imagen personalizada
                Marker(
                    state = rememberMarkerState(position = arequipaLocation),
                    icon = descriptor,
                    title = "Arequipa, Perú"
                )

                // Marcadores adicionales con la misma imagen
                locations.forEach { location ->
                    Marker(
                        state = rememberMarkerState(position = location),
                        icon = descriptor,
                        title = "Ubicación",
                        snippet = "Punto de interés"
                    )
                }
            }
        }
    }
}

