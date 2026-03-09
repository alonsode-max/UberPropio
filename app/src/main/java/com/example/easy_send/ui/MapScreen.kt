package com.example.easy_send.ui

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.rememberCameraPositionState

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    onBack:()->Unit
){
    var startLocation by remember { mutableStateOf("") }
    var endLocation by remember { mutableStateOf("") }
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val initialLocation = LatLng(40.416775, -3.703790)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLocation, 12f)
    }

    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    var mapProperties by remember {
        mutableStateOf(MapProperties(isMyLocationEnabled = false))
    }

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

    Column(Modifier
        .fillMaxWidth()
        .height(132.dp)
        .background(color = Color(0xFFFFFFFF))) {
        Row(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        }
        Spacer(Modifier.fillMaxWidth().height(3.dp))

        Row(Modifier.fillMaxWidth().height(30.dp), horizontalArrangement = Arrangement.Center) {
            val interactionSource = remember { MutableInteractionSource() }
            BasicTextField(
                value = startLocation,
                onValueChange = { startLocation = it },
                modifier = Modifier
                    .width(274.dp)
                    .height(30.dp),
                textStyle = TextStyle(fontSize = 14.sp),
                singleLine = true,
                interactionSource = interactionSource,
                decorationBox = { innerTextField ->
                    TextFieldDefaults.DecorationBox(
                        value = startLocation,
                        innerTextField = innerTextField,
                        enabled = true,
                        singleLine = true,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = interactionSource,
                        placeholder = { Text("My location", color = Color(0xFF53585E), fontSize = 14.sp)},
                        container = {
                            TextFieldDefaults.ContainerBox(
                                enabled = true,
                                isError = false,
                                interactionSource = interactionSource,
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFFEDEDED),
                                    unfocusedContainerColor = Color(0xFFEDEDED),
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                        },
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
                    )
                }
            )
        }

        Spacer(Modifier.fillMaxWidth().height(10.dp))

        Row(Modifier.fillMaxWidth().height(30.dp), horizontalArrangement = Arrangement.Center) {
            val interactionSource = remember { MutableInteractionSource() }
            BasicTextField(
                value = endLocation,
                onValueChange = { endLocation = it },
                modifier = Modifier
                    .width(274.dp)
                    .height(30.dp),
                textStyle = TextStyle(fontSize = 14.sp),
                singleLine = true,
                interactionSource = interactionSource,
                decorationBox = { innerTextField ->
                    TextFieldDefaults.DecorationBox(
                        value = endLocation,
                        innerTextField = innerTextField,
                        enabled = true,
                        singleLine = true,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = interactionSource,
                        placeholder = { Text("Where to", color = Color(0xFF53585E), fontSize = 14.sp) },
                        container = {
                            TextFieldDefaults.ContainerBox(
                                enabled = true,
                                isError = false,
                                interactionSource = interactionSource,
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFFEDEDED),
                                    unfocusedContainerColor = Color(0xFFEDEDED),
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                        },
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
                    )
                }
            )
        }
    }


    Box(
        modifier = Modifier
            .offset(y=132.dp)
            .fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties
        )
        Button(onClick= {},//onDone
            modifier = Modifier.width(343.dp).height(49.dp).offset(x=16.dp,y=558.dp), shape = RoundedCornerShape(2.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Done",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0xFFFFFFFF),
                )
            )
        }
    }

}
