package com.example.foodsharingapp.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun LoginPage(
    onClickStart : () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp,Alignment.CenterVertically)
    ) {
        LoginCard()
        Spacer(
            modifier = Modifier.height(32.dp)
        )
        Button(
            onClick = onClickStart,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFAEB8A1),
                contentColor = Color.Black
            )
        ){
            Text(
                "Login",
            )
        }
        Spacer(
            modifier = Modifier.height(5.dp)
        )
    }
}

@Preview
@Composable
fun LoginCard(){
    Card{
        Text(
            text = "Food Sharing\nPlanner",
            fontSize = 32.sp,
            lineHeight = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .background(color = Color(0xFF545454))
                .padding(
                    horizontal = 32.dp,
                    vertical = 24.dp
                )
        )
    }
}
