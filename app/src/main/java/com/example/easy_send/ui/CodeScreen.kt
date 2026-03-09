package com.example.easy_send.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easy_send.viewmodel.AuthViewModel
import androidx.compose.ui.input.key.*
import com.example.easy_send.viewmodel.AuthState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CodeScreen(
    authVm: AuthViewModel,
    onLoginOk:()->Unit,
    onEmail:()->Unit,
    onBack:()->Unit
){

    val state = authVm.authState
    val code = remember { mutableStateListOf("", "", "", "", "", "") }
    val focusRequesters = remember { List(6) { FocusRequester() } }

    val fullCode = code.joinToString("")

    LaunchedEffect(state) {
        if (state is AuthState.Authenticated) {
            onLoginOk()
        }
    }

    Scaffold(modifier=Modifier.fillMaxWidth()) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)) {
            Row(Modifier
                .width(274.dp)
                .height(46.dp)){
                Text(
                    text = "Enter the 6-digit code sent you",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 23.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFF000000),
                    )
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (i in 0..5) {
                    TextField(
                        value = code[i],
                        onValueChange = { newValue ->
                            if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                                code[i] = newValue

                                if (newValue.isNotEmpty() && i < 5) {
                                    focusRequesters[i + 1].requestFocus()
                                }
                                if (code.all { it.isNotEmpty() }) {
                                    authVm.verifySmsCode(code.joinToString(""))
                                }

                            } else if (newValue.length > 1) {
                                val lastChar = newValue.last().toString()
                                if (lastChar.all { it.isDigit() }) {
                                    code[i] = lastChar
                                    if (i < 5) focusRequesters[i + 1].requestFocus()
                                }
                            }
                        },
                        modifier = Modifier
                            .width(45.dp)
                            .height(55.dp)
                            .focusRequester(focusRequesters[i])
                            .onKeyEvent { keyEvent ->
                                if (keyEvent.type == KeyEventType.KeyDown &&
                                    keyEvent.key == Key.Backspace &&
                                    code[i].isEmpty() && i > 0) {
                                    focusRequesters[i - 1].requestFocus()
                                    true
                                } else {
                                    false
                                }
                            },
                        textStyle = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF0F0F0),
                            unfocusedContainerColor = Color(0xFFF0F0F0),
                            focusedIndicatorColor = Color.Black,
                            unfocusedIndicatorColor = Color.LightGray
                        )
                    )
                }
            }
            if (state is AuthState.Error) {
                Text(state.msg, color = Color.Red, modifier = Modifier.padding(top = 10.dp))
            }
            Spacer(Modifier.fillMaxWidth().height(34.dp))
            Row(Modifier.fillMaxWidth()) {
                Button(
                    onClick = { onEmail() },
                    modifier = Modifier
                        .width(180.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(1000.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEEEEEE),
                        contentColor = Color.Black
                    )
                ) {
                    Text("Login with password")
                }
            }
            Spacer(Modifier.fillMaxWidth().height(10.dp))
            Row(Modifier.fillMaxWidth().offset(y=450.dp),Arrangement.SpaceBetween) {
                Button(
                    onClick = { onBack() },
                    modifier = Modifier
                        .alpha(0.49f)
                        .width(96.dp)
                        .height(49.dp),
                    shape = RoundedCornerShape(1000.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEEEEEE),
                        contentColor = Color.Black
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = ""
                    )
                }
                Button(
                    onClick = { authVm.verifySmsCode(fullCode) },
                    enabled = fullCode.length == 6 && authVm.authState !is AuthState.Loading,
                    modifier = Modifier
                        .alpha(0.49f)
                        .width(96.dp)
                        .height(49.dp),
                    shape = RoundedCornerShape(1000.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEEEEEE),
                        contentColor = Color.Black
                    )
                ) {
                    Row(Modifier,Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Next",
                            color = Color.Black
                        )
                        Icon(
                            imageVector = Icons.Filled.ArrowForward,
                            contentDescription = "",
                        )
                    }
                }
            }
        }
    }
}
