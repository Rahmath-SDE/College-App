package com.example.mcet

import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
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
fun StudentSidebarDrawer(
    navController: NavController,
    drawerState: DrawerState,
    closeDrawer: () -> Unit,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val user = FirebaseAuth.getInstance().currentUser

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color(0xFFD0F0C0).copy(alpha = 0.7f) // translucent pastel green
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    "Menu",
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black
                )
                NavigationDrawerItem(
                    label = { Text("About") },
                    selected = false,
                    onClick = {
                        Toast.makeText(
                            context,
                            "Email: ${user?.email ?: "Not available"}",
                            Toast.LENGTH_LONG
                        ).show()
                        closeDrawer()
                    },
                    icon = { Icon(Icons.Default.Info, contentDescription = null) },
                )
                NavigationDrawerItem(
                    label = { Text("Help") },
                    selected = false,
                    onClick = {
                        Toast.makeText(
                            context,
                            "Contact us at: AMS@gmail.com",
                            Toast.LENGTH_LONG
                        ).show()
                        closeDrawer()
                    },
                    icon = { Icon(Icons.Default.Help, contentDescription = null) },
                )
                NavigationDrawerItem(
                    label = { Text("Logout") },
                    selected = false,
                    onClick = {
                        FirebaseAuth.getInstance().signOut()
                        closeDrawer()
                        navController.navigate("studentLogin") {
                            popUpTo("studentHome") { inclusive = true }
                        }
                    },
                    icon = { Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null) },
                )
            }
        },
        content = content
    )
}
