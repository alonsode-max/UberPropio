package com.example.easy_send.ui

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easy_send.R
import com.example.easy_send.model.Country
import com.example.easy_send.viewmodel.AuthState
import com.example.easy_send.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    authVm:      AuthViewModel,
    onLoginOk:   () -> Unit,
    onNext:     ()->Unit
) {
    val countryList by authVm.countries.collectAsState()
    var selectedCountry by remember { mutableStateOf<Country?>(null) }
    var phoneNumber by remember { mutableStateOf("") }
    var showCountryPicker by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var searchQuery by remember { mutableStateOf("") }
    val filteredCountries = remember(searchQuery, countryList) {
        countryList.filter {
            it.countryName.contains(searchQuery, ignoreCase = true) ||
                    it.countryCode.contains(searchQuery)
        }
    }
    val context = LocalContext.current
    val activity= LocalActivity.current
    val state = authVm.authState
    val fullNumber = "${selectedCountry?.countryCode}${phoneNumber}"


    LaunchedEffect(countryList) {
        if (selectedCountry == null && countryList.isNotEmpty()) {
            selectedCountry = countryList.firstOrNull { it.isoCode == "ES" }
                ?: countryList.first()
        }
    }

    LaunchedEffect(state) {
        if (state is AuthState.Authenticated) {
            onLoginOk()
        }
    }

    LaunchedEffect(state) {
        if (state is AuthState.CodeSent) {
            onNext()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Enter your mobile number",
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = selectedCountry?.let { "${it.flagEmoji} ${it.countryCode}" } ?: "...",
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .width(110.dp)
                    .height(56.dp),
                interactionSource = remember { MutableInteractionSource() }.also { interactionSource ->
                    LaunchedEffect(interactionSource) {
                        interactionSource.interactions.collect {
                            if (it is PressInteraction.Release) showCountryPicker = true
                        }
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF0F0F0),
                    unfocusedContainerColor = Color(0xFFF0F0F0),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            TextField(
                value = phoneNumber,
                onValueChange = { if (it.all { char -> char.isDigit() }) phoneNumber = it },
                placeholder = { Text("Mobile Number") },
                prefix = {
                    Text(
                        text = "${selectedCountry?.countryCode ?: ""} ",
                        color = Color.Black
                    )
                },
                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF0F0F0),
                    unfocusedContainerColor = Color(0xFFF0F0F0),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true
            )
        }

        if (state is AuthState.Error) {
            Text(
                text = state.msg,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = { activity?.let { authVm.login(fullNumber, it)}},
            modifier = Modifier
                .fillMaxWidth()
                .height(49.dp),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                if (state is AuthState.Loading)
                    CircularProgressIndicator(Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp)
                else{
                    Text(
                        text = "Next",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.White
                    )
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }
        }


        if (showCountryPicker) {
            ModalBottomSheet(
                onDismissRequest = { showCountryPicker = false },
                sheetState = sheetState,
                containerColor = Color.White
            ) {
                Column(modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .padding(horizontal = 16.dp)) {
                    Text(
                        "Select Country",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search country...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        singleLine = true
                    )

                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(filteredCountries) { country ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp)
                                    .clickable {
                                        selectedCountry = country
                                        showCountryPicker = false
                                        searchQuery = ""
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(country.flagEmoji, fontSize = 24.sp, modifier = Modifier.padding(end = 12.dp))
                                Text(country.countryName, modifier = Modifier.weight(1f))
                                Text(country.countryCode, color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "By continuing you may receive an SMS for verification. Message and data rates may apply",
            style = TextStyle(
                fontSize = 12.sp,
                lineHeight = 18.sp,
                color = Color(0xFF757575),
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        OutlinedButton(
            onClick = { authVm.loginWithGoogle(context) },
            enabled = state !is AuthState.Loading,
            modifier = Modifier
                .fillMaxWidth()
                .height(49.dp),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(22.dp)
                )
                Text(text = "Login with Google")
            }
        }
    }
}


