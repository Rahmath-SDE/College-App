package com.example.mcet

import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SidebarDrawerContent(
    navController: NavController,
    closeDrawer: () -> Unit
) {
    val context = LocalContext.current
    val user = FirebaseAuth.getInstance().currentUser

    ModalDrawerSheet(
        drawerContainerColor = Color(0xFFBDE8C0).copy(alpha = 0.9f), // soft pastel green with more visibility
        drawerTonalElevation = 4.dp
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Navigation",
            modifier = Modifier
                .padding(start = 20.dp, bottom = 12.dp),
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
            color = Color(0xFF1B5E20) // deep green for header
        )

        Divider(thickness = 1.dp, color = Color(0xFFB2DFDB).copy(alpha = 0.5f))

        Spacer(modifier = Modifier.height(12.dp))

        NavigationDrawerItem(
            label = {
                Text("About", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            },
            selected = false,
            onClick = {
                Toast.makeText(
                    context,
                    "Email: ${user?.email ?: "Not available"}",
                    Toast.LENGTH_LONG
                ).show()
                closeDrawer()
            },
            icon = {
                Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFF388E3C))
            },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )

        NavigationDrawerItem(
            label = {
                Text("Help", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            },
            selected = false,
            onClick = {
                Toast.makeText(
                    context,
                    "Contact us at: AMS@gmail.com",
                    Toast.LENGTH_LONG
                ).show()
                closeDrawer()
            },
            icon = {
                Icon(Icons.Default.Help, contentDescription = null, tint = Color(0xFF388E3C))
            },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )

        NavigationDrawerItem(
            label = {
                Text("Logout", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            },
            selected = false,
            onClick = {
                FirebaseAuth.getInstance().signOut()
                closeDrawer()
                navController.navigate("facultyLogin") {
                    popUpTo("facultyHome") { inclusive = true }
                }
            },
            icon = {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = Color(0xFFD32F2F))
            },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Divider(thickness = 1.dp, color = Color(0xFFB2DFDB).copy(alpha = 0.5f))
    }
}

