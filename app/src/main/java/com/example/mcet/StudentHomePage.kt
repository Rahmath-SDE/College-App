package com.example.mcet

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentHomePage(navController: NavController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // 👇 Back press exit dialog state
    val showExitDialog = remember { mutableStateOf(false) }

    // 👇 Intercept system back button
    androidx.activity.compose.BackHandler {
        showExitDialog.value = true
    }

    // 👇 Show exit confirmation dialog
    if (showExitDialog.value) {
        AlertDialog(
            onDismissRequest = { showExitDialog.value = false },
            title = { Text("Exit App") },
            text = { Text("Do you want to exit the app?") },
            confirmButton = {
                TextButton(onClick = {
                    showExitDialog.value = false
                    android.os.Process.killProcess(android.os.Process.myPid()) // 👈 Exit app
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog.value = false }) {
                    Text("No")
                }
            }
        )
    }

    StudentSidebarDrawer(
        navController = navController,
        drawerState = drawerState,
        closeDrawer = { scope.launch { drawerState.close() } }
    ) {
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = { Text("Welcome Student", fontSize = 20.sp) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu", tint = Color.Black)
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.Black
                    )
                )
            },
            content = { padding ->
                StudentHomePageContent(padding)
            }
        )
    }
}


@Composable
fun StudentHomePageContent(padding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
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
                .background(Color.White.copy(alpha = 0.8f))
                .padding(16.dp)
        ) {
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
    val context = LocalContext.current
    val firebaseAuth = FirebaseAuth.getInstance()
    val userEmail = firebaseAuth.currentUser?.email?.replace(".", "_") ?: return

    val attendanceDates = remember { mutableStateListOf<String>() }
    val eventMap = remember { mutableStateMapOf<String, List<String>>() }

    val db = FirebaseDatabase.getInstance().reference
    var selectedEvent by remember { mutableStateOf<Pair<String, List<String>>?>(null) }

    // ✅ Load attendance and event data
    LaunchedEffect(userEmail) {
        db.child("attendance").child(userEmail).get().addOnSuccessListener {
            it.children.forEach { snap ->
                val dateKey = snap.key ?: return@forEach
                attendanceDates.add(dateKey)
            }
        }

        db.child("events").get().addOnSuccessListener {
            it.children.forEach { dateSnap ->
                val date = dateSnap.key ?: return@forEach
                val titles = dateSnap.children.mapNotNull {
                    it.child("title").getValue(String::class.java)
                }
                eventMap[date] = titles
            }
        }
    }

    val today = LocalDate.now()
    var selectedMonth by remember { mutableStateOf(today.monthValue) }
    var selectedYear by remember { mutableStateOf(today.year) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Month header
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            IconButton(onClick = {
                if (selectedMonth == 1) {
                    selectedMonth = 12
                    selectedYear -= 1
                } else {
                    selectedMonth--
                }
            }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Previous")
            }

            val monthName = java.text.DateFormatSymbols().months[selectedMonth - 1]
                .replaceFirstChar { it.uppercaseChar() }
            Text("$monthName $selectedYear", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            IconButton(onClick = {
                if (selectedMonth == 12) {
                    selectedMonth = 1
                    selectedYear += 1
                } else {
                    selectedMonth++
                }
            }) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Next")
            }
        }

        val dates = CalendarUtils.generateDatesForMonth(selectedYear, selectedMonth)

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .padding(8.dp)
        ) {
            items(listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")) { day ->
                Box(modifier = Modifier.size(48.dp), contentAlignment = Alignment.Center) {
                    Text(day, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }

            items(dates) { date ->
                val dateString = date?.format(DateTimeFormatter.ISO_DATE)
                val isAttended = dateString in attendanceDates
                val events = eventMap[dateString] ?: emptyList()

                val bgColor = when {
                    isAttended && events.isNotEmpty() -> Color(0xFFB2DFDB) // Teal: both
                    isAttended -> Color(0xFF81C784) // Green: attendance
                    events.isNotEmpty() -> Color(0xFFFFF59D) // Yellow: event
                    else -> Color.Transparent
                }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(2.dp)
                        .background(bgColor, shape = RoundedCornerShape(8.dp))
                        .clickable {
                            if (!dateString.isNullOrEmpty() && events.isNotEmpty()) {
                                selectedEvent = dateString to events
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = date?.dayOfMonth?.toString() ?: "",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // 🔹 Event Pop-up Dialog
        selectedEvent?.let { (dateStr, titles) ->
            AlertDialog(
                onDismissRequest = { selectedEvent = null },
                title = { Text("Events on $dateStr") },
                text = {
                    Column {
                        titles.forEach { Text("• $it") }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { selectedEvent = null }) {
                        Text("OK")
                    }
                }
            )
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
