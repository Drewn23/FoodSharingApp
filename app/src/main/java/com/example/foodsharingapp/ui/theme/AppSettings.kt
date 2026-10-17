package com.example.foodsharingapp.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.intellij.lang.annotations.JdkConstants

@Preview
@Composable
fun AppSetting(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    Version: String = "Version 1.0.0"
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        SettingHeader(
            onClick = onClick
        )
        ThemeSetting()
        CameraSetting()
        MapSetting()
        Spacer(modifier = Modifier.weight(1f))
        Text(Version, fontSize = 24.sp)
    }
}

@Preview
@Composable
fun SettingHeader (
    onClick: () -> Unit = {}
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Text("Settings")
        // Yellow header line
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            thickness = 50.dp,
            color = Color.Yellow
        )

        IconButton(
            onClick = onClick
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close"
            )
        }

    }
}

@Preview
@Composable
fun ThemeSetting(
    onClick: () -> Unit = {}
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "Theme",
            autoSize = TextAutoSize
                .StepBased(
                    stepSize = 1.sp,
                    maxFontSize = 24.sp,
                    minFontSize = 12.sp)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onClick,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonColors(
                    containerColor = Color.Red,
                    contentColor = Color.Black,
                    disabledContainerColor = Color.Red,
                    disabledContentColor = Color.Black
                )
            ){}
            Button(
                onClick = onClick,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonColors(
                    containerColor = Color.Yellow,
                    contentColor = Color.Black,
                    disabledContainerColor = Color.Red,
                    disabledContentColor = Color.Black
                )
            ){}
            Button(
                onClick = onClick,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonColors(
                    containerColor = Color(0xFF78d6df),
                    contentColor = Color.Black,
                    disabledContainerColor = Color.Red,
                    disabledContentColor = Color.Black
                )
            ) {}
        }

    }
}

@Preview
@Composable
fun CameraSetting(
    modifier: Modifier = Modifier,
    initialState: Boolean = false,
    onStateChanged: (Boolean) -> Unit = {}
) {
    var isOn by remember { mutableStateOf(initialState) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Text(
            text = "Camera",
            autoSize = TextAutoSize.StepBased(
                stepSize = 1.sp,
                maxFontSize = 24.sp,
                minFontSize = 12.sp
            )
        )

        CustomSlideSwitch(
            checked = isOn,
            onCheckedChange = { newValue ->
                isOn = newValue
                onStateChanged(newValue)
            }
        )
    }
}


@Composable
fun MapSetting(
    modifier: Modifier = Modifier,
    initialState: Boolean = false,
    onStateChanged: (Boolean) -> Unit = {}
) {
    var isOn by remember { mutableStateOf(initialState) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Text(
            text = "Map",
            autoSize = TextAutoSize.StepBased(
                stepSize = 1.sp,
                maxFontSize = 24.sp,
                minFontSize = 12.sp
            )
        )

        CustomSlideSwitch(
            checked = isOn,
            onCheckedChange = { newValue ->
                isOn = newValue
                onStateChanged(newValue)
            }
        )
    }
}


@Composable
fun CustomSlideSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    trackColor: Color = Color(0xFFE2C499),
    thumbColor: Color = Color.Red
) {
    val cornerRadius = 20.dp

    Box(
        modifier = modifier
            .size(width = 90.dp, height = 40.dp)
            .clip(RoundedCornerShape(cornerRadius))
            .background(trackColor)
            .clickable {
                onCheckedChange(!checked)
            }
            .padding(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (checked) {
                Box(
                    modifier = Modifier
                        .size(width = 45.dp, height = 45.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(thumbColor)
                )
                Text(
                    text = "ON",
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(end = 8.dp)
                )
            } else {
                Text(
                    text = "OFF",
                    fontSize = 12.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Box(
                    modifier = Modifier
                        .size(width = 45.dp, height = 45.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(thumbColor)
                )
            }
        }
    }
}