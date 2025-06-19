package com.example.mcet

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun FacultyLoginPage(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.fs),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Back Button (top-left)
        IconButton(
            onClick = {
                navController.navigate("selectRole") {
                    popUpTo("facultyLogin") { inclusive = true }
                }
            },
            modifier = Modifier
                .padding(16.dp)
                .size(40.dp)
                .clip(RoundedCornerShape(50))
                .background(Color(0xFF1E8C5A))
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        // Centered Login Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Account Icon",
                modifier = Modifier.size(80.dp),
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color(0xFF000000))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "FACULTY",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF000000),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))

            FacultyLoginForm(navController)
        }
    }
}

@Composable
fun FacultyLoginForm(navController: NavController) {
    var username by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(24.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Log in to your account", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color(0xFF1E8C5A),
                unfocusedIndicatorColor = Color.Gray
            ),
            singleLine = true,
            leadingIcon = {
                Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "User Icon", tint = Color(0xFF000000))
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color(0xFF1E8C5A),
                unfocusedIndicatorColor = Color.Gray
            ),
            singleLine = true,
            trailingIcon = {
                val image = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = "Toggle Password Visibility")
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Forgot Password?",
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF1E8C5A)
        )

        Spacer(modifier = Modifier.height(16.dp))

        val context = LocalContext.current
        val auth = remember { FirebaseAuth.getInstance() } // <-- Add this near top inside FacultyLoginForm

        Button(
            onClick = {
                val email = username.text.trim()
                val pass = password.text.trim()

                if (email.isNotEmpty() && pass.isNotEmpty()) {
                    FirebaseAuth.getInstance()
                        .signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
                                navController.navigate("facultyHome")
                            } else {
                                val errorMsg = when (task.exception?.message) {
                                    null -> "Login failed. Please try again."
                                    else -> {
                                        if (task.exception?.message?.contains("network error", ignoreCase = true) == true) {
                                            "No internet connection. Please check your network."
                                        } else {
                                            "Invalid credentials. Please try again."
                                        }
                                    }
                                }
                                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(context, "Please enter both email and password.", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E8C5A))
        ) {
            Text("Login", color = Color.White)
        }


        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Don't have an account? Sign up",
            color = Color(0xFF1E8C5A),
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navController.navigate("facultySignup")
                },
            textAlign = TextAlign.Center
        )

    }
}
