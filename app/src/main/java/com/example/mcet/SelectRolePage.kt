package com.example.mcet

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SelectRolePage(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.sel), // Replace with your actual drawable image
            contentDescription = "Background Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Content on top of the background
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "SELECT YOUR ROLE",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF000000) // Change text color to contrast with background
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { navController.navigate("studentLogin") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E8C5A)), // Green color applied
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Text("STUDENT", color = Color.White) // Ensuring text is visible
            }

            Button(
                onClick = { navController.navigate("facultyLogin") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E8C5A)), // Green color applied
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Text("FACULTY", color = Color.White) // Ensuring text is visible
            }
        }
    }
}
