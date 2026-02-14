
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database

@Composable
fun SignupScreen(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var usernameError by remember { mutableStateOf(false) }

    fun validateUsername(): Boolean {
        usernameError = username.isBlank()

        return !usernameError
    }

    // The error was here. It should be .fillMaxSize() instead of .FillMaxSize()
    // and you need to import androidx.compose.foundation.layout.padding
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        contentAlignment = Alignment.Center){
        Column (horizontalAlignment = Alignment.CenterHorizontally){
            Text("Signup", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    if (it.isNotBlank()) usernameError = false
                },
                label = { Text("Username") },
                singleLine = true,
                isError = usernameError,
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    if (usernameError) {
                        Text("Username is required")
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = email,
                onValueChange = {email = it},
                label = {Text("Email")},
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = {password = it},
                label = {Text("Password")},
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            val context = LocalContext.current

            Button(onClick = {
                if (!validateUsername()) return@Button
                Firebase.auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = Firebase.auth.currentUser?.uid

                            // Modern Kotlin way to get database reference
                            val database = Firebase.database.getReference("users")

                            val userMap = mapOf(
                                "username" to username,
                                "email" to email
                            )

                            if (userId != null) {
                                database.child(userId).setValue(userMap)
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "Signup Successful!", Toast.LENGTH_SHORT).show()
                                        navController.navigate("login") {
                                            popUpTo("signup") { inclusive = true }
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(context, "Database Error: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        } else {
                            Toast.makeText(context, "Auth Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            ){ Text("Signup")}

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = {
                navController.navigate("login"){
                    popUpTo("signup"){ inclusive = true }
                }
            }) {
                Text("Already have an account? Login here.")
            }
        }
    }
}
