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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.text.font.FontWeight
import coil3.compose.AsyncImage
import com.example.foodsharingapp.data.FoodWithPlace
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private fun formatDate(timeMillis: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date(timeMillis))
}

@Preview
@Composable
fun AppMenu(
    modifier: Modifier = Modifier,
    foods: List<FoodWithPlace> = emptyList(),
    searchQuery: String = "",
    onSearchChange: (String) -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onAddClick: () -> Unit = {},
    onItemClick: (FoodWithPlace) -> Unit = {}
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppNavigationDrawerContent(
                onCloseClick = { scope.launch { drawerState.close() } },
                onSettingsClick = {
                    scope.launch { drawerState.close() }
                    onSettingsClick()
                },
                onLogoutClick = {
                    scope.launch { drawerState.close() }
                    onLogoutClick()
                }
            )
        }
    ) {
        Box(modifier = modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                HeaderLine(
                    onMenuClick = {
                        scope.launch {
                            if (drawerState.isClosed) drawerState.open() else drawerState.close()
                        }
                    }
                )

                AppMenuContent(
                    foods = foods,
                    searchQuery = searchQuery,
                    onSearchChange = onSearchChange,
                    onItemClick = onItemClick
                )
            }

            FloatingActionButton(
                onClick = onAddClick,
                containerColor = Color.Yellow,
                contentColor = Color.Black,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Food"
                )
            }
        }
    }
}

@Composable
fun AppNavigationDrawerContent(
    onCloseClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    ModalDrawerSheet {
        Column(
            modifier = Modifier.fillMaxHeight()
        ) {
            DrawerLine(onClick = onCloseClick)

            Spacer(modifier = Modifier.height(12.dp))

            NavigationDrawerItem(
                label = { Text("Settings") },
                selected = false,
                onClick = onSettingsClick,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            NavigationDrawerItem(
                label = { Text("Logout") },
                selected = false,
                onClick = onLogoutClick,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
fun AppMenuContent(
    modifier: Modifier = Modifier,
    foods: List<FoodWithPlace>,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onItemClick: (FoodWithPlace) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = { Text("Search @tag...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (foods.isEmpty()) {
                item {
                    EmptyFoodState()
                }
            } else {
                items(foods) { item ->
                    FoodItemCard(
                        item = item,
                        onItemClick = { onItemClick(item) }
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyFoodState() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
    ) {
        Text(
            text = "Nothing yet",
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Try adding something",
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
    }
}

@Composable
fun FoodItemCard(
    modifier: Modifier = Modifier,
    item: FoodWithPlace,
    onItemClick: () -> Unit = {},
    onShareClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onItemClick)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (item.food.photo.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    AsyncImage(
                        model = item.food.photo,
                        contentDescription = "Food Photo",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            InfoTable(item = item, onShareClick = onShareClick)
        }
    }
}

@Preview
@Composable
fun HeaderLine(
    onMenuClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
    ) {
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            thickness = 50.dp,
            color = Color.Yellow
        )

        IconButton(
            onClick = onMenuClick,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                tint = Color.Black
            )
        }
    }
}

@Preview
@Composable
fun DrawerLine(
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            thickness = 50.dp,
            color = Color.Yellow
        )

        IconButton(
            onClick = onClick,
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close Drawer"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InfoTable(
    modifier: Modifier = Modifier,
    item: FoodWithPlace? = null,
    onShareClick: () -> Unit = {}
) {
    val dateText = item?.let { formatDate(it.food.timeDate) } ?: "Date not found"

    val tags = item?.let {
        listOfNotNull(it.food.tag1, it.food.tag2, it.food.tag3)
            .filter { tag -> !tag.isNullOrBlank() }
            .joinToString(" ") { tag -> "#$tag" }
    } ?: ""

    val description = item?.food?.description ?: ""
    val placeName = item?.place?.name ?: ""

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFE8C5A0))
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        // Date is important and taken from database
                        text = dateText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    if (tags.isNotBlank()) {
                        Text(
                            text = "@ $tags",
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    }
                }
                IconButton(
                    onClick = onShareClick,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color.Black
                    )
                }
            }

            if (description.isNotBlank()) {
                Text(
                    text = description,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }

            if (placeName.isNotBlank()) {
                Text(
                    text = "📍 $placeName",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}
