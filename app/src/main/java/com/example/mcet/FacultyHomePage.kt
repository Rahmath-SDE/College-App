package com.example.mcet

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacultyHomePage(navController: NavController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var showExitDialog by remember { mutableStateOf(false) }

    // 🔙 Handle back button
    BackHandler {
        showExitDialog = true
    }

    // 🔔 Exit confirmation dialog
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Exit App") },
            text = { Text("Do you want to exit the app?") },
            confirmButton = {
                TextButton(onClick = {
                    showExitDialog = false
                    // Exit the app
                    kotlin.system.exitProcess(0)
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("No")
                }
            }
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SidebarDrawerContent(
                navController = navController,
                closeDrawer = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = { Text("Faculty Dashboard", fontSize = 20.sp) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.Black
                            )
                        }
                    }
                )
            },
            content = { padding ->
                FacultyDashboardContent(padding, navController)
            }
        )
    }
}


@Composable
fun FacultyDashboardContent(padding: PaddingValues, navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFB2EBF2), Color(0xFFE0F7FA))
                )
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.fs),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
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

            AttendanceCalendar()

            Spacer(modifier = Modifier.height(32.dp))

            FacultyProfiles()

            Spacer(modifier = Modifier.height(32.dp))

            ExamTimetable()
        }
    }
}




@Composable
fun AttendanceCalendar() {
    val calendar = Calendar.getInstance()
    val currentMonth = remember { mutableStateOf(calendar.get(Calendar.MONTH)) }
    val currentYear = remember { mutableStateOf(calendar.get(Calendar.YEAR)) }
    val database = FirebaseDatabase.getInstance().reference
    val context = LocalContext.current

    val showDialog = remember { mutableStateOf(false) }
    val selectedDate = remember { mutableStateOf("") }
    val showEventDialog = remember { mutableStateOf(false) }
    val showAttendanceDialog = remember { mutableStateOf(false) }
    val inputText = remember { mutableStateOf("") }

    val eventDates = remember { mutableStateListOf<String>() }
    val eventTitles = remember { mutableStateMapOf<String, List<String>>() }

    val showEventInfoDialog = remember { mutableStateOf(false) }
    val selectedEventList = remember { mutableStateOf<List<String>>(emptyList()) }

    // ✅ Fetch events to highlight on calendar
    LaunchedEffect(Unit) {
        database.child("events").get().addOnSuccessListener {
            it.children.forEach { snap ->
                val dateKey = snap.key ?: return@forEach
                eventDates.add(dateKey)

                val titles = snap.children.mapNotNull { it.child("title").getValue(String::class.java) }
                eventTitles[dateKey] = titles
            }
        }
    }

    val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.9f))
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = {
                if (currentMonth.value == 0) {
                    currentMonth.value = 11
                    currentYear.value -= 1
                } else {
                    currentMonth.value -= 1
                }
            }) {
                Icon(Icons.Default.ChevronLeft, contentDescription = "Previous Month")
            }

            Text(
                text = SimpleDateFormat("MMMM yyyy").format(
                    GregorianCalendar(currentYear.value, currentMonth.value, 1).time
                ),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            IconButton(onClick = {
                if (currentMonth.value == 11) {
                    currentMonth.value = 0
                    currentYear.value += 1
                } else {
                    currentMonth.value += 1
                }
            }) {
                Icon(Icons.Default.ChevronRight, contentDescription = "Next Month")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(Modifier.fillMaxWidth()) {
            daysOfWeek.forEach {
                Text(
                    text = it,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        val firstDayOfMonth = Calendar.getInstance().apply {
            set(Calendar.YEAR, currentYear.value)
            set(Calendar.MONTH, currentMonth.value)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        val startDayOfWeek = firstDayOfMonth.get(Calendar.DAY_OF_WEEK) - 1
        val totalDaysInMonth = firstDayOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
        val totalCells = startDayOfWeek + totalDaysInMonth

        LazyVerticalGrid(columns = GridCells.Fixed(7), modifier = Modifier.fillMaxWidth()) {
            items(totalCells) { index ->
                if (index < startDayOfWeek) {
                    Box(modifier = Modifier.size(48.dp)) {}
                } else {
                    val day = index - startDayOfWeek + 1
                    val dateKey = String.format("%04d-%02d-%02d", currentYear.value, currentMonth.value + 1, day)
                    val isEventDay = dateKey in eventDates

                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                if (isEventDay) Color(0xFFFFF59D) else Color(0xFFE0F7FA)
                            )
                            .clickable {
                                if (isEventDay) {
                                    selectedEventList.value = eventTitles[dateKey] ?: emptyList()
                                    showEventInfoDialog.value = true
                                } else {
                                    selectedDate.value = dateKey
                                    showDialog.value = true
                                }
                            }
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("$day")
                    }
                }
            }
        }

        // 🔹 Dialog: Event Options (Mark Attendance / Add Event)
        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                title = { Text("Actions for ${selectedDate.value}") },
                text = {
                    Column {
                        Button(onClick = {
                            showDialog.value = false
                            showAttendanceDialog.value = true
                        }, modifier = Modifier.fillMaxWidth()) {
                            Text("Mark Attendance")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {
                            showDialog.value = false
                            showEventDialog.value = true
                        }, modifier = Modifier.fillMaxWidth()) {
                            Text("Add Event")
                        }
                    }
                },
                confirmButton = {},
                dismissButton = {}
            )
        }

        // 🔹 Dialog: Mark Attendance
        if (showAttendanceDialog.value) {
            AlertDialog(
                onDismissRequest = { showAttendanceDialog.value = false },
                title = { Text("Mark Attendance") },
                text = {
                    OutlinedTextField(
                        value = inputText.value,
                        onValueChange = { inputText.value = it },
                        label = { Text("Enter Student Email") }
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (inputText.value.isNotEmpty()) {
                            val studentEmailKey = inputText.value.replace(".", "_")
                            database.child("attendance").child(studentEmailKey).child(selectedDate.value)
                                .setValue(true)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Attendance marked successfully!", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Failed to mark attendance.", Toast.LENGTH_SHORT).show()
                                }
                        }
                        inputText.value = ""
                        showAttendanceDialog.value = false
                    }) {
                        Text("Mark")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAttendanceDialog.value = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // 🔹 Dialog: Add Event
        if (showEventDialog.value) {
            AlertDialog(
                onDismissRequest = { showEventDialog.value = false },
                title = { Text("Add Event") },
                text = {
                    OutlinedTextField(
                        value = inputText.value,
                        onValueChange = { inputText.value = it },
                        label = { Text("Enter Event Title") }
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (inputText.value.isNotEmpty()) {
                            database.child("events").child(selectedDate.value)
                                .push().setValue(mapOf("title" to inputText.value))
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Event added!", Toast.LENGTH_SHORT).show()
                                    eventDates.add(selectedDate.value)
                                    eventTitles[selectedDate.value] =
                                        (eventTitles[selectedDate.value] ?: emptyList()) + inputText.value
                                }
                        }
                        inputText.value = ""
                        showEventDialog.value = false
                    }) {
                        Text("Add")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showEventDialog.value = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // 🔹 Dialog: Show Event Info
        if (showEventInfoDialog.value) {
            AlertDialog(
                onDismissRequest = { showEventInfoDialog.value = false },
                title = { Text("Events") },
                text = {
                    Column {
                        selectedEventList.value.forEach {
                            Text("• $it", fontSize = 16.sp)
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showEventInfoDialog.value = false }) {
                        Text("OK")
                    }
                }
            )
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
                                painter = painterResource(id = R.drawable.baseline_account_circle_24),
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

data class FacultyCardData(val title: String, val value: String, val icon: ImageVector)

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
