import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mcriderkit.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database

@Composable
fun SignupScreen(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    // Error States
    var usernameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    val context = LocalContext.current

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
                contentDescription = "Logo",
                modifier = Modifier.size(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Create Account",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Username Field
            OutlinedTextField(
                value = username,
                onValueChange = { username = it; usernameError = it.isBlank() },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
                isError = usernameError,
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                supportingText = { if (usernameError) Text("Username cannot be empty") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it; emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches() },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth(),
                isError = emailError,
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                supportingText = { if (emailError) Text("Enter a valid email address") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; passwordError = it.length < 6 },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                isError = passwordError,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = null)
                    }
                },
                supportingText = { if (passwordError) Text("Password must be at least 6 characters") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Signup Button
            Button(
                onClick = {
                    if (username.isBlank() || emailError || passwordError) {
                        Toast.makeText(context, "Please correct the errors above", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    isLoading = true

                    Firebase.auth.createUserWithEmailAndPassword(email.trim(), password.trim())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val userId = Firebase.auth.currentUser?.uid
                                val database = Firebase.database.getReference("users")
                                val userMap = mapOf("username" to username, "email" to email)

                                userId?.let {
                                    database.child(it).setValue(userMap).addOnSuccessListener {
                                        isLoading = false
                                        navController.navigate("login") { popUpTo("signup") { inclusive = true } }
                                    }
                                }
                            } else {
                                isLoading = false
                                Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                } else {
                    Text("Sign Up", style = MaterialTheme.typography.titleMedium)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { navController.navigate("login") }) {
                Text("Already have an account? Login here.", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}