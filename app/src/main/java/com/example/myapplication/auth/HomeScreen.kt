package com.example.myapplication.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.DestinationScreen
import com.example.myapplication.IgViewModel

import com.example.myapplication.R

@Composable
fun HomeScreen(navController: NavController, vm: IgViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF7E6E44))
    ) {

        Image(
            painter = painterResource(id = R.drawable.tus_image), // image used from. https://www.facebook.com/photo/?fbid=4665632226793991&set=ecnf.100071852273842
            contentDescription = "TUS Campus Image",
            modifier = Modifier
                .width(440.dp)
                .height(390.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                )
                .background(Color.Transparent)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Spacer(modifier = Modifier.height(400.dp))


            Button(
                onClick = {
                    navController.navigate(DestinationScreen.Event.route)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(37.dp)
                    .shadow(1.dp, shape = RoundedCornerShape(4.dp)),
                contentPadding = PaddingValues(),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color(
                        red = 0.02916666679084301f,
                        green = 0.028315972536802292f,
                        blue = 0.028315972536802292f,
                        alpha = 1f
                    )
                )
            ) {
                Text(
                    text = "Events",
                    textAlign = TextAlign.Center,
                    fontSize = 25.sp,
                    textDecoration = TextDecoration.None,
                    letterSpacing = 0.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(1f),
                    fontWeight = FontWeight.Medium,
                    fontStyle = FontStyle.Normal,
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    navController.navigate(DestinationScreen.TimeTable.route)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(37.dp)
                    .shadow(1.dp, shape = RoundedCornerShape(4.dp)),
                contentPadding = PaddingValues(),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color(
                        red = 0.02916666679084301f,
                        green = 0.028315972536802292f,
                        blue = 0.028315972536802292f,
                        alpha = 1f
                    )
                )
            ) {
                Text(
                    text = "TimeTable",
                    textAlign = TextAlign.Center,
                    fontSize = 25.sp,
                    textDecoration = TextDecoration.None,
                    letterSpacing = 0.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(1f),
                    fontWeight = FontWeight.Medium,
                    fontStyle = FontStyle.Normal,
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    navController.navigate(DestinationScreen.Contact.route)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(37.dp)
                    .shadow(1.dp, shape = RoundedCornerShape(4.dp)),
                contentPadding = PaddingValues(),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color(
                        red = 0.02916666679084301f,
                        green = 0.028315972536802292f,
                        blue = 0.028315972536802292f,
                        alpha = 1f
                    )
                )
            ) {
                Text(
                    text = "Contact Us",
                    textAlign = TextAlign.Center,
                    fontSize = 25.sp,
                    textDecoration = TextDecoration.None,
                    letterSpacing = 0.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(1f),
                    fontWeight = FontWeight.Medium,
                    fontStyle = FontStyle.Normal,
                )
            }

            Spacer(modifier = Modifier.height(100.dp))

            // Back Button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .align(Alignment.CenterHorizontally)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFFFFD700), Color(0xFFFFD700))
                        )
                    )
            ) {
                Button(
                    onClick = {
                        vm.logout()
                        navController.navigate(DestinationScreen.Main.route)
                    },
                    colors = ButtonDefaults.buttonColors(
                        Color.Transparent
                    ),
                    modifier = Modifier
                        .width(300.dp)
                        .align(Alignment.Center)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xff554800), Color(0xff554800))
                            )
                        )
                ) {
                    Text(
                        text = "Logout",
                        color = Color.Black,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}