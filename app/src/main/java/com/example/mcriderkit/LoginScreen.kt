package com.example.mcriderkit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.auth
import com.google.firebase.database.database

@Composable
fun LoginScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Changed "Signup" to "Login"
            Text("Login", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    errorMessage = null // Clear error when typing
                },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = errorMessage != null
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = null // Clear error when typing
                },
                label = { Text("Password") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                // Hide password characters
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = errorMessage != null
            )

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))


            Button(
                onClick = {

                    // Basic empty check
                    if (email.isBlank() || password.isBlank()) {
                        errorMessage = "Email and password cannot be empty."
                        return@Button
                    }

                    Firebase.auth
                        .signInWithEmailAndPassword(email.trim(), password.trim())
                        .addOnCompleteListener { task ->

                            if (task.isSuccessful) {

                                errorMessage = null

                                val userId = Firebase.auth.currentUser?.uid
                                val dbRef = Firebase.database.getReference("users/$userId")

                                dbRef.get().addOnSuccessListener { snapshot ->

                                    if (snapshot.hasChild("licenseType")) {

                                        navController.navigate("main") {
                                            popUpTo("login") { inclusive = true }
                                        }

                                    } else {

                                        navController.navigate("onboard") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    }
                                }

                            } else {

                                errorMessage = when (task.exception) {

                                    is FirebaseAuthInvalidUserException ->
                                        "No account found with this email."

                                    is FirebaseAuthInvalidCredentialsException ->
                                        "Incorrect email or password."

                                    else ->
                                        "Login failed. Please try again."
                                }
                            }
                        }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }


            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = {
                // Navigate to the signup screen
                navController.navigate("signup") {
                    popUpTo("login") { inclusive = true }
                }
            }) {
                // Changed text to direct users to signup
                Text("Don't have an account? Sign up here.")
            }
        }
    }
}