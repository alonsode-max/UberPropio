package com.example.easy_send.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easy_send.R

@Composable
fun SafetyScreen(
    onBack:()->Unit,
    onNext:()->Unit
    //Falta onGuidelines
){
    Column(Modifier
        .fillMaxWidth()
        .wrapContentWidth()) {
        Row(Modifier
            .fillMaxWidth()
            .background(color = Color(0xFFFFE1DF))
            .padding(10.dp)) {
            IconButton(onClick= onBack ,
                modifier = Modifier
                    .width(20.dp)
                    .height(20.dp)
                    .offset(x = 10.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.cross),
                    contentDescription = null
                )
            }
        }

        Box(
            Modifier
                .height(208.dp)
                .background(color = Color(0xFFFFE1DF))
        ){
            Image(
                painter = painterResource(id = R.drawable.diversidad),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(177.dp)
                    .offset(y = 31.dp)
            )
        }

        Column(Modifier
            .fillMaxSize()
            .offset(16.dp, y = 15.dp)) {
            Row(Modifier.fillMaxWidth()) {
                Text(
                    text = "Uber’s Comunity Guidlines",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFF000000)
                    )
                )
            }

            Spacer(Modifier
                .fillMaxWidth()
                .height(8.dp))
            Row(Modifier.fillMaxWidth()) {
                Text(
                    text = "Safety and respect for all",
                    style = TextStyle(
                        fontSize = 21.sp,
                        fontWeight = FontWeight(500),
                        color = Color(0xFF000000)
                    )
                )
            }

            Spacer(Modifier
                .fillMaxWidth()
                .height(14.dp))
            Row(Modifier
                .width(311.dp)
                .height(52.dp)) {
                Text(
                    text = "We’re commited, along wit millions of riders and drivers, to:",
                    style = TextStyle(
                        fontSize = 18.sp,
                        lineHeight = 25.79.sp,
                        fontWeight = FontWeight(300),
                        color = Color(0xFF000000)
                    )
                )
            }

            Spacer(Modifier
                .fillMaxWidth()
                .height(16.dp))
            Row(Modifier
                .width(253.dp)
                .height(44.dp)) {
                Column(Modifier.offset(y=13.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.tick),
                        contentDescription = null,
                        Modifier
                            .padding(1.dp)
                            .width(12.dp)
                            .height(9.dp)
                    )
                }
                Column(Modifier
                    .width(253.dp)
                    .offset(30.dp)) {
                    Text(
                        text = "Treat everyone with kindness and respect",
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 22.sp,
                            fontWeight = FontWeight(500),
                            color = Color(0xFF000000)
                        )
                    )
                }
            }

            Spacer(Modifier
                .fillMaxWidth()
                .height(16.dp))
            Row(Modifier
                .fillMaxWidth()
                .height(22.dp)) {
                Column(Modifier.offset(y=6.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.tick),
                        contentDescription = null,
                        Modifier
                            .padding(1.dp)
                            .width(12.dp)
                            .height(9.dp)
                    )
                }
                Column(Modifier.offset(30.dp)) {
                    Text(
                        text = "Help keep one another safe",
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 22.sp,
                            fontWeight = FontWeight(500),
                            color = Color(0xFF000000)
                        )
                    )
                }
            }

            Spacer(Modifier
                .fillMaxWidth()
                .height(21.dp))
            Row(Modifier
                .fillMaxWidth()
                .height(22.dp)) {
                Column(Modifier.offset(y=6.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.tick),
                        contentDescription = null,
                        Modifier
                            .padding(1.dp)
                            .width(12.dp)
                            .height(9.dp)
                    )
                }
                Column(Modifier.offset(30.dp)) {
                    Text(
                        text = "Follow the law",
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 22.sp,
                            fontWeight = FontWeight(500),
                            color = Color(0xFF000000)
                        )
                    )
                }
            }

            Spacer(Modifier
                .fillMaxWidth()
                .height(21.dp))
            Row(Modifier
                .width(344.dp)
                .height(46.dp)) {
                Text(
                    text = "Everyone who uses Uber apps is expected to follow these guidlines.",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 22.93.sp,
                        fontWeight = FontWeight(300),
                        color = Color(0xFF000000),
                    )
                )
            }

            Spacer(Modifier
                .fillMaxWidth()
                .height(11.dp))
            Row(Modifier
                .fillMaxWidth()
                .height(50.dp),Arrangement.Absolute.Left, Alignment.CenterVertically) {
                Text(
                    text = "You can read about our",
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 20.06.sp,
                        fontWeight = FontWeight(300),
                        color = Color(0xFF000000),
                    )
                )

                TextButton({}, Modifier.padding(0.dp)) { //onGuidelines
                    Text(
                        text = "Community Guidlines here",
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 20.06.sp,
                            fontWeight = FontWeight(300),
                            color = Color(0xFF276EF1),
                        )
                    )
                }
            }

            Spacer(Modifier.fillMaxWidth().height(71.dp))
            Row(Modifier.width(343.dp).height(49.dp)) {

                Button(onClick= {onNext()},
                    modifier = Modifier.width(343.dp).height(49.dp), shape = RoundedCornerShape(2.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "I understand",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight(500),
                                color = Color(0xFFFFFFFF),
                            )
                        )
                    }
                }
            }
        }
    }
}