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

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            descriptorState.value?.let { descriptor ->
                Marker(
                    state = rememberMarkerState(position = arequipaLocation),
                    icon = descriptor,
                    title = "Arequipa, Perú"
                )
            }
        }
    }
}