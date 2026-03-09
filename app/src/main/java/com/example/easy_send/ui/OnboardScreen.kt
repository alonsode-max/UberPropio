package com.example.easy_send.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardScreen(onNext:()->Unit) {
    Scaffold( containerColor = Color(0xFF276EF1)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 97.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = com.example.easy_send.R.drawable.vector),
                contentDescription = null,
                modifier = Modifier
                    .width(79.dp)
                    .height(26.dp)
            )
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 207.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = com.example.easy_send.R.drawable.screenshot1),
            contentDescription = null,
            modifier = Modifier
                .width(174.dp)
                .height(199.dp)
        )
    }
    Column(
        modifier = Modifier.fillMaxSize().offset(y = 700.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Button(onClick= { onNext() },
            modifier = Modifier
                .width(343.dp)
                .height(49.dp),
            shape = RoundedCornerShape(2.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(90.dp, Alignment.End),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize()){
                Text(text="Get Started", color = Color.White)
                Icon(Icons.Filled.ArrowForward, "Button")
            }
        }
    }
}
