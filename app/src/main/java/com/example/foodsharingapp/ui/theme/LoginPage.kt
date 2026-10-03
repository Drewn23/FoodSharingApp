package com.example.foodsharingapp.ui.theme

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalProvider
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
        Text("Hi")
    }
}

@Preview
@Composable
fun LoginCard(){
    Card{
        Text(
            "Food Sharing\nPlanner",
            modifier = Modifier
                .background(color = Color(0xFF545454))
                .padding(
                    horizontal = 32.dp,
                    vertical = 24.dp
                    ),
            autoSize = TextAutoSize.StepBased(
                minFontSize = 12.sp,
                maxFontSize = 48.sp,
                stepSize = 1.sp
            ),
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center

        )
    }
}
