package com.example.foodsharingapp.ViewModel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TestScreen(vm: HomeViewModel) {
    val foods by vm.foods.collectAsState()

    Column(Modifier.padding(16.dp)) {
        Button(onClick = { vm.addTestEntry() }) {
            Text("Add test entry")
        }
        foods.forEach {item ->
            Text("${item.food.description} | tag: ${item.food.tag1}")
        }
    }
}