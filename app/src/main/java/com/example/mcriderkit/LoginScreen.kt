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
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.navigation.NavHostController
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val credentialManager = remember { CredentialManager.create(context) }

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

            // Password Field
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

            // Login Button with Verification Guard
            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        errorMessage = "Please fill in all fields."
                        return@Button
                    }
                    isLoading = true

                    Firebase.auth.signInWithEmailAndPassword(email.trim(), password.trim())
                        .addOnCompleteListener { task ->
                            isLoading = false
                            if (task.isSuccessful) {
                                val user = Firebase.auth.currentUser
                                if (user != null) {
                                    val userId = user.uid
                                    val dbRef = Firebase.database.getReference("users/$userId")

                                    // 1. Fetch user data from Realtime Database to check Admin Status
                                    dbRef.get().addOnSuccessListener { snapshot ->
                                        val isDbVerified = snapshot.child("emailVerified").getValue(Boolean::class.java) ?: false
                                        val isAuthVerified = user.isEmailVerified

                                        // 2. CRITICAL CHECK: Verify via either Auth Link OR Admin Panel approval
                                        if (isAuthVerified || isDbVerified) {
                                            errorMessage = null

                                            // If verified in Auth email link, sync it back to DB for consistency
                                            if (isAuthVerified && !isDbVerified) {
                                                dbRef.child("emailVerified").setValue(true)
                                            }

                                            // Update last login timestamp
                                            dbRef.child("lastLogin").setValue(System.currentTimeMillis())

                                            // Route based on layout profile completion
                                            if (snapshot.hasChild("licenseType")) {
                                                navController.navigate("main") {
                                                    popUpTo("login") { inclusive = true }
                                                }
                                            } else {
                                                navController.navigate("onboard") {
                                                    popUpTo("login") { inclusive = true }
                                                }
                                            }
                                        } else {
                                            // 3. Reject if neither is verified, and trigger/resend email link
                                            errorMessage = "Please verify your email address. A verification link has been sent."
                                            user.sendEmailVerification()
                                        }
                                    }.addOnFailureListener {
                                        errorMessage = "Database fetch failed: ${it.message}"
                                    }
                                }
                            } else {
                                errorMessage = task.exception?.message ?: "Login failed"
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

            Button(
                onClick = {
                    isLoading = true
                    errorMessage = null

                    coroutineScope.launch {
                        try {
                            // 1. Set up the Google ID option
                            val googleIdOption = GetGoogleIdOption.Builder()
                                .setFilterByAuthorizedAccounts(false)
                                .setServerClientId(context.getString(R.string.default_web_client_id))
                                .setAutoSelectEnabled(false) // Will auto-login if only one account exists
                                .build()

                            // 2. Build the request
                            val request = GetCredentialRequest.Builder()
                                .addCredentialOption(googleIdOption)
                                .build()

                            // 3. Launch the native UI (suspends until user finishes)
                            val result = credentialManager.getCredential(
                                request = request,
                                context = context
                            )

                            // 4. Extract the token and pass it to Firebase
                            val credential = result.credential
                            if (credential is CustomCredential &&
                                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                            ) {
                                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                                val idToken = googleIdTokenCredential.idToken

                                firebaseAuthWithGoogle(idToken, navController, context) { error ->
                                    errorMessage = error
                                    isLoading = false
                                }
                            } else {
                                errorMessage = "Unexpected credential type"
                                isLoading = false
                            }
                        } catch (e: Exception) {
                            // Catch cancellation or API errors
                            errorMessage = "Google sign-in failed: ${e.localizedMessage}"
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(12.dp)
            ) {
                Text("Sign in with Google")
            }

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

private fun firebaseAuthWithGoogle(
    idToken: String,
    navController: NavHostController,
    context: Context,
    onError: (String) -> Unit
) {
    val credential = GoogleAuthProvider.getCredential(idToken, null)

    Firebase.auth.signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = Firebase.auth.currentUser
                // Check if this is the first time this user is signing in
                val isNewUser = task.result?.additionalUserInfo?.isNewUser ?: false

                if (user != null) {
                    val userId = user.uid
                    val dbRef = Firebase.database.getReference("users/$userId")

                    if (isNewUser) {
                        // Google provides a single "displayName" (e.g., "John Doe").
                        val nameParts = user.displayName?.split(" ") ?: listOf("", "")
                        val firstName = nameParts.firstOrNull() ?: ""

                        val newUserProfile = mapOf(
                            "email" to (user.email ?: ""),
                            "username" to firstName,
                            "emailVerified" to true, // Google accounts are pre-verified
                            "accountCreated" to System.currentTimeMillis(),
                            "lastLogin" to System.currentTimeMillis(),
                            "role" to "viewer",
                            "status" to "active"
                        )

                        // Save the full map and immediately send them to onboarding
                        dbRef.setValue(newUserProfile).addOnSuccessListener {
                            navController.navigate("onboard") {
                                popUpTo("login") { inclusive = true }
                            }
                        }.addOnFailureListener {
                            onError("Failed to save user data: ${it.message}")
                        }

                    } else {
                        // 2. Returning User: Update login timestamp and route them
                        dbRef.child("lastLogin").setValue(System.currentTimeMillis())

                        // Just in case it wasn't true before
                        dbRef.child("emailVerified").setValue(true)

                        // Fetch data to determine if they finished onboarding
                        dbRef.get().addOnSuccessListener { snapshot ->
                            if (snapshot.hasChild("licenseType")) { // Change "licenseType" to whatever marks a complete profile
                                navController.navigate("main") {
                                    popUpTo("login") { inclusive = true }
                                }
                            } else {
                                navController.navigate("onboard") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        }.addOnFailureListener {
                            onError("Database fetch failed: ${it.message}")
                        }
                    }
                }
            } else {
                onError(task.exception?.message ?: "Google Authentication failed.")
            }
        }
}