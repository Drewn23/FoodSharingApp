package com.example.foodsharingapp.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun AppMenu (
    modifier: Modifier = Modifier
) {
    HeaderLine ()
    LazyColumn(

    ) {

    }
    Button(onClick = {}) {

    }
}

@Preview
@Composable
fun HeaderLine (){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
    ) {
        // Yellow header line
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            thickness = 40.dp,
            color = Color.Yellow
        )

        IconButton(
            onClick = {

            }
        ) {
            Icon(
                imageVector = Icons.Default.Menu, // or Icons.Filled.Menu
                contentDescription = "Menu",
                tint = Color.Black
            )
        }

    }
}