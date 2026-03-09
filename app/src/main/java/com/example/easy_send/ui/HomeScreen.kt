package com.example.easy_send.ui

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easy_send.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    onMap:()->Unit
){
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    
    val initialLocation = LatLng(40.416775, -3.703790) // Madrid por defecto
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLocation, 12f)
    }

    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    var mapProperties by remember {
        mutableStateOf(MapProperties(isMyLocationEnabled = false))
    }

    val uiSettings = remember { MapUiSettings(zoomGesturesEnabled = false, scrollGesturesEnabled = false, tiltGesturesEnabled = false) }

    LaunchedEffect(locationPermissionState.status.isGranted) {
        if (locationPermissionState.status.isGranted) {
            mapProperties = mapProperties.copy(isMyLocationEnabled = true)

            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val userLatLng = LatLng(it.latitude, it.longitude)
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(userLatLng, 15f)
                }
            }
        } else {
            locationPermissionState.launchPermissionRequest()
        }
    }

    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            IconButton(onClick = {/*onProfile*/}, modifier = Modifier.width(46.dp).height(39.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                )
            }
        }
        Spacer(Modifier.height(3.dp).fillMaxWidth())
        Row(Modifier
            .border(width = 1.dp, color = Color(0xFFDEE4E2), shape = RoundedCornerShape(size = 8.dp))
            .width(343.dp)
            .height(135.dp)
            .background(color = Color(0xFF10462E), shape = RoundedCornerShape(size = 8.dp))) {
            Column(modifier = Modifier
                .width(188.dp).offset(17.dp,38.dp)) {
                Text(
                    text = "Satisfy any carving",
                    style = TextStyle(
                        fontSize = 21.sp,
                        fontWeight = FontWeight(500),
                        color = Color(0xFFFFFFFF),
                    )
                )
                Spacer(Modifier.height(15.dp).fillMaxWidth())
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start,Alignment.CenterVertically) {
                    Text(
                        text = "Order on Eats ",
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight(500),
                            color = Color(0xFFFFFFFF),
                        )
                    )
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier
                            .width(10.dp)
                            .height(10.dp)
                            .background(color = Color(0xFF10462E)),
                        tint=Color(0xFFFFFFFF)
                    )
                }
            }
            Column(Modifier
                .width(111.dp)
                .height(124.dp)
                .offset(42.dp, y = 9.dp), verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.End) {
                Image(
                    painter = painterResource(id = R.drawable.dumplins),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                )
            }
        }
        Spacer(Modifier.fillMaxWidth().height(19.dp))
        Row(Modifier
            .width(343.dp)
            .height(54.dp)
            .background(color = Color(0xFFEDEDED))
            .padding(15.dp)) {
            Text(
                text = "Where To?",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0xFF000000),
                )
            )
        }
        Spacer(Modifier.fillMaxWidth().height(23.dp))
        Row(Modifier.fillMaxWidth()) { //Convertir a boton onFav
            Box(Modifier
                .width(34.dp)
                .height(34.dp)){
                Image(
                    painter = painterResource(id = R.drawable.ellipse),
                    contentDescription = "image description",
                    contentScale = ContentScale.None
                )
                Image(
                    painter = painterResource(id = R.drawable.star_black),
                    contentDescription = "image description",
                    contentScale = ContentScale.None,
                    modifier = Modifier.offset(7.dp,7.dp)
                )
            }
            Text(
                text = "Choose a saved place",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0xFF000000),
                ),
                modifier = Modifier.offset(12.dp,7.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.rightarrow),
                contentDescription = "Next",
                contentScale = ContentScale.None,
                modifier = Modifier.offset(121.dp,10.dp)
            )
        }
        Image(
            painter = painterResource(id = R.drawable.separator),
            contentDescription = "",
            contentScale = ContentScale.None,
            modifier = Modifier.offset(57.dp,22.dp)
        )

        Spacer(Modifier.fillMaxWidth().height(47.dp))

        Row(Modifier.fillMaxWidth().clickable { onMap() }) {
            Box(Modifier
                .width(34.dp)
                .height(34.dp)){
                Image(
                    painter = painterResource(id = R.drawable.ellipse),
                    contentDescription = "image description",
                    contentScale = ContentScale.None
                )
                Image(
                    painter = painterResource(id = R.drawable.marker),
                    contentDescription = "image description",
                    contentScale = ContentScale.None,
                    modifier = Modifier.offset(13.dp,10.dp)
                )
            }
            Text(
                text = "Set destination on map",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0xFF000000),
                ),
                modifier = Modifier.offset(12.dp,7.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.rightarrow),
                contentDescription = "Next",
                contentScale = ContentScale.None,
                modifier = Modifier.offset(112.dp,10.dp)
            )
        }
        Image(
            painter = painterResource(id = R.drawable.separator),
            contentDescription = "",
            contentScale = ContentScale.None,
            modifier = Modifier.offset(57.dp,22.dp)
        )
        Spacer(Modifier.fillMaxWidth().height(39.dp))
        Row(Modifier.fillMaxWidth().offset(7.dp)) {
            Text(
                text = "Around you",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0xFF000000),
                )
            )
        }
        Spacer(Modifier.fillMaxWidth().height(21.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
                .border(1.dp, Color(0xFFDEE4E2), shape = RoundedCornerShape(12.dp))
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = mapProperties,
                uiSettings = uiSettings,
                onMapClick = { onMap() }
            )
        }
    }
}
