package com.example.lab12

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext



@Composable
fun MapScreen() {
    val context = LocalContext.current
    val arequipaLocation = LatLng(-16.4040102, -71.559611)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(arequipaLocation, 12f)
    }

    val mapType = remember { mutableStateOf(MapType.NORMAL) }

    LaunchedEffect(Unit) {
        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngZoom(LatLng(-16.2520984, -71.6836503), 12f), // Yura
            durationMs = 3000
        )
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

    Column(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.weight(1f),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(mapType = mapType.value)
        ) {
            descriptorState.value?.let { descriptor ->
                // Marcador de Arequipa con imagen personalizada
                Marker(
                    state = rememberMarkerState(position = arequipaLocation),
                    icon = descriptor,
                    title = "Arequipa, Perú"
                )

                // Marcadores adicionales
                locations.forEach { location ->
                    Marker(
                        state = rememberMarkerState(position = location),
                        icon = descriptor,
                        title = "Ubicación",
                        snippet = "Punto de interés"
                    )
                }

                // Polígonos
                Polygon(
                    points = listOf(
                        LatLng(-16.398866, -71.536961),
                        LatLng(-16.398744, -71.536529),
                        LatLng(-16.399178, -71.536289),
                        LatLng(-16.399299, -71.536721)
                    ),
                    strokeColor = Color.Red,
                    fillColor = Color.Blue.copy(alpha = 0.3f),
                    strokeWidth = 5f
                )

                Polygon(
                    points = listOf(
                        LatLng(-16.422704, -71.530830),
                        LatLng(-16.422920, -71.531340),
                        LatLng(-16.423264, -71.531110),
                        LatLng(-16.423050, -71.530600)
                    ),
                    strokeColor = Color.Red,
                    fillColor = Color.Green.copy(alpha = 0.3f),
                    strokeWidth = 5f
                )

                Polygon(
                    points = listOf(
                        LatLng(-16.432292, -71.509145),
                        LatLng(-16.432757, -71.509626),
                        LatLng(-16.433013, -71.509310),
                        LatLng(-16.432566, -71.508853)
                    ),
                    strokeColor = Color.Red,
                    fillColor = Color.Yellow.copy(alpha = 0.3f),
                    strokeWidth = 5f
                )

                // Ruta
                Polyline(
                    points = listOf(
                        LatLng(-16.398866, -71.536961),
                        LatLng(-16.4205151, -71.4945209),
                        LatLng(-16.3524187, -71.5675994),
                        LatLng(-16.392010, -71.548901)
                    ),
                    color = Color.Magenta,
                    width = 6f
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = { mapType.value = MapType.NORMAL }) {
                Text("Normal")
            }
            Button(onClick = { mapType.value = MapType.HYBRID }) {
                Text("Híbrido")
            }
            Button(onClick = { mapType.value = MapType.TERRAIN }) {
                Text("Terreno")
            }
        }
    }
}

