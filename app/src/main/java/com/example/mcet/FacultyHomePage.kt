package com.example.mcet

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun FacultyHomePage(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFB2EBF2), Color(0xFFE0F7FA))
                )
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.fs),  // Add image in res/drawable
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            SmallTopAppBar(
                title = { Text("Faculty Dashboard", fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = { /* Open Drawer */ }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu", tint = Color.Black)
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            val facultyInfo = listOf(
                FacultyCardData("Classes", "4 Today", Icons.Default.Schedule),
                FacultyCardData("Meetings", "2 Pending", Icons.Default.People),
                FacultyCardData("Exams", "Upcoming", Icons.Default.Edit),
                FacultyCardData("Reports", "View", Icons.Default.Assessment)
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(facultyInfo) { item ->
                    FacultyCard(item)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            FacultyProfiles()

            Spacer(modifier = Modifier.height(32.dp))

            ExamTimetable()
        }
    }
}

@Composable
fun FacultyProfiles() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1E8C5A))
            .padding(16.dp)
    ) {
        Text("Faculty Members", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)

        val facultyList = listOf(
            "Majeed Sir", "Ali Sir", "Nazneen Mam",
            "Deeba Mam", "Saba Mam", "Rasheeda Mam"
        )

        Column {
            for (row in facultyList.chunked(3)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    row.forEach { name ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.baseline_account_circle_24), // Add profile image in res/drawable
                                contentDescription = "Profile",
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(Color.White)
                            )
                            Text(text = name, fontSize = 14.sp, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExamTimetable() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB))
                )
            )
            .padding(8.dp)
    ) {
        Text("Exam Timetable", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(8.dp))

        val exams = listOf(
            ExamSchedule("March 5", "Data Structures", "10:00 AM - 12:00 PM"),
            ExamSchedule("March 7", "Database Systems", "02:00 PM - 04:00 PM"),
            ExamSchedule("March 10", "Machine Learning", "09:00 AM - 11:00 AM"),
            ExamSchedule("March 12", "Software Engineering", "01:00 PM - 03:00 PM")
        )

        exams.forEach { exam ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TableCell(exam.date)
                TableCell(exam.subject)
                TableCell(exam.time)
            }
        }
    }
}

data class FacultyCardData(val title: String, val value: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

data class ExamSchedule(val date: String, val subject: String, val time: String)

@Composable
fun FacultyCard(data: FacultyCardData) {
    Box(
        modifier = Modifier
            .size(width = 160.dp, height = 100.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primary)
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

@Composable
fun TableCell(text: String) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(6.dp))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, fontSize = 16.sp, fontWeight = FontWeight.Normal, color = Color.Black)
    }
}
