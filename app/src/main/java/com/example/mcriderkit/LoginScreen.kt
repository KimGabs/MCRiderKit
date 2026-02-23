package com.example.mcriderkit

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    // Using Box to allow centering, with vertical scroll for small screens/keyboards
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.mcrklogo),
                contentDescription = "App Logo",
                modifier = Modifier.size(200.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it; errorMessage = null },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                isError = errorMessage != null,
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field with Visibility Toggle
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; errorMessage = null },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                isError = errorMessage != null,
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = "Toggle password visibility")
                    }
                }
            )

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp).fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Login Button with Loading State
            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        errorMessage = "Please fill in all fields."
                        return@Button
                    }
                    isLoading = true
                    // Firebase logic stays here, but ensure you set isLoading = false on completion
                    Firebase.auth.signInWithEmailAndPassword(email.trim(), password.trim())
                        .addOnCompleteListener { task ->
                            isLoading = false
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
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = !isLoading,
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Login", style = MaterialTheme.typography.titleMedium)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Footer Links
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = { handleForgotPassword(email, context) }) {
                    Text("Forgot Password?")
                }
                TextButton(onClick = { navController.navigate("signup") }) {
                    Text("Create Account")
                }
            }
        }
    }
}


fun handleForgotPassword(email: String, context: Context) {
    if (email.isBlank()) {
        Toast.makeText(context, "Please enter your email first.", Toast.LENGTH_SHORT).show()
        return
    }

    Firebase.auth.sendPasswordResetEmail(email)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Reset link sent to $email! 📧", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
}