package com.example.mcet
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable

@Composable
fun MetApp() {
    // Initialize NavController to handle navigation
    val navController = rememberNavController()

    // Set up the NavHost that handles navigation between composables (screens)
    NavHost(navController = navController, startDestination = "selectRole") {

        // Define routes and the corresponding composable functions (screens)

        composable("selectRole") {
            SelectRolePage(navController)
        }
        composable("studentLogin") {
            StudentLoginPage(navController)
        }
        composable("facultyLogin") {
            FacultyLoginPage(navController)
        }
        composable("studentHome") {
            StudentHomePage(navController)
        }
        composable("facultyHome") {
            FacultyHomePage(navController)
        }
    }
}

