package com.example.mcet

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentHomePage(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.fs), // Place an image in res/drawable
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Main Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White.copy(alpha = 0.8f)) // White overlay for readability
                .padding(16.dp)
        ) {
            SmallTopAppBar(
                title = { Text("Welcome Student", fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = { /* Open Drawer */ }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            val profileInfo = listOf(
                ProfileCardData("GPA", "7.75", Icons.Default.Star, Brush.horizontalGradient(listOf(Color(0xFF64B5F6), Color(0xFFBBDEFB)))),
                ProfileCardData("Year", "3rd", Icons.Default.School, Brush.horizontalGradient(listOf(Color(0xFF81C784), Color(0xFFA5D6A7)))),
                ProfileCardData("Semester", "2nd", Icons.Default.DateRange, Brush.horizontalGradient(listOf(Color(0xFFFFD54F), Color(0xFFFFF176)))),
                ProfileCardData("Syllabus", "View", Icons.Default.Book, Brush.horizontalGradient(listOf(Color(0xFFE57373), Color(0xFFEF9A9A))))
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(profileInfo) { item ->
                    ProfileCard(item)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE8EAF6).copy(alpha = 0.9f))
                    .padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CalendarGrid()
                    AcademicTable()
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CalendarGrid() {
    val days = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    val dates = listOf(
        "", "", "", "", "1", "2", "3",
        "4", "5", "6", "7", "8", "9", "10",
        "11", "12", "13", "14", "15", "16", "17",
        "18", "19", "20", "21", "22", "23", "24",
        "25", "26", "27", "28", "", "", ""
    )
    val highlightedDates = setOf("3", "8", "12", "23")

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("February 2025", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .padding(8.dp)
        ) {
            items(days.size) { index ->
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(days[index], fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
            items(dates.size) { index ->
                val isHighlighted = dates[index] in highlightedDates
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(if (isHighlighted) Color(0xFF81C784) else Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(dates[index], fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun AcademicTable() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray)
                .padding(8.dp)
        ) {
            TableCell("Academic Year", true)
            TableCell("Semester", true)
            TableCell("Grade", true)
            TableCell("CGPA", true)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            TableCell("2023-24")
            TableCell("2nd")
            TableCell("A")
            TableCell("7.75")
        }
        Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            TableCell("2022-23")
            TableCell("1st")
            TableCell("B+")
            TableCell("7.50")
        }
    }
}

@Composable
fun TableCell(text: String, isHeader: Boolean = false) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .background(if (isHeader) Color.DarkGray else Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            fontSize = 16.sp,
            fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal,
            color = if (isHeader) Color.White else Color.Black
        )
    }
}

data class ProfileCardData(val title: String, val value: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val gradient: Brush)

@Composable
fun ProfileCard(data: ProfileCardData) {
    Box(
        modifier = Modifier
            .size(width = 180.dp, height = 100.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(data.gradient)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = data.icon, contentDescription = data.title, tint = Color.White, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = data.title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = data.value, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}
