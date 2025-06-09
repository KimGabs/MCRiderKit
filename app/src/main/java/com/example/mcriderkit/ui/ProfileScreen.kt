package com.example.mcriderkit.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.mcriderkit.R
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.core.app.ActivityCompat
import com.example.mcriderkit.ui.components.ExamGraph
import com.example.mcriderkit.ui.components.HazardTestGraph
import androidx.core.net.toUri
import androidx.compose.foundation.lazy.items



@Composable
fun ProfileScreen(
    studentViewModel: StudentExamViewModel,
    NonProViewModel: NonProExamViewModel,
    proViewModel: ProExamViewModel,
    viewModel: HazardTestViewModel,
    context: Context
) {
    val context = LocalContext.current

    var userName by rememberSaveable { mutableStateOf("User") }
    var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }
    var newUserName by remember { mutableStateOf(userName) }
    val allHazards by viewModel.hazardTests.collectAsState()
    val unlockedTrophies by viewModel.trophyTests.collectAsState()
    val quizScores by studentViewModel.quizScores.collectAsState()


    // Image picker launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it

            // Persist permission
            context.contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            // Save to SharedPreferences
            val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("profile_photo_uri", it.toString())
                apply()
            }
        }
    }


    // Load saved user data once
    LaunchedEffect(Unit) {
        studentViewModel.loadQuizScores()
        val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        userName = sharedPref.getString("user_name", "User") ?: "User"
        newUserName = userName
        val savedUri = sharedPref.getString("profile_photo_uri", null)
        imageUri = savedUri?.toUri()

        // Request permission (if needed)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                100
            )
        } else {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                100
            )
        }
    }

    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Edit Username") },
            text = {
                TextField(
                    value = newUserName,
                    onValueChange = { newUserName = it },
                    label = { Text("Username") }
                )
            },
            confirmButton = {
                Button(onClick = {
                    userName = newUserName
                    val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putString("user_name", newUserName)
                        apply()
                    }
                    showEditDialog = false
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(onClick = { showEditDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Picture
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .clickable {launcher.launch(arrayOf("image/*"))},
            contentAlignment = Alignment.Center
        ) {
            imageUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } ?: Image(
                painter = painterResource(id = R.drawable.profile_picture),
                contentDescription = "Profile Picture",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Username with edit icon
        Text(
            text = userName,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Button(
            onClick = { showEditDialog = true },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Edit Profile")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Hazard Trophies",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(items = allHazards, key = { it.id }) { hazard ->
                TrophyItem(
                    name = hazard.title,
                    unlocked = hazard.trophy
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        HazardTestGraph(viewModel = viewModel)

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Quiz Trophies",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(quizScores) { score ->
                TrophyItem(
                    name = score.quizType,
                    unlocked = score.trophy,
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        ExamGraph(
            studentViewModel = studentViewModel,
            nonProViewModel = NonProViewModel,
            proViewModel = proViewModel
        )

        Spacer(modifier = Modifier.height(24.dp)) // Ensure enough spacing at the bottom
    }
}

@Composable
fun TrophyItem(name: String, unlocked: Boolean) {
    val icon = if (unlocked) R.drawable.trophy_icon else R.drawable.trophy_icon_locked

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = "$name Trophy",
            modifier = Modifier
                .size(75.dp)
                .clip(CircleShape)
                .background(if (unlocked) Color(0xFF9966CC) else Color.LightGray)
                .border(2.dp, if (unlocked) Color(0xFFE6C200) else Color.Gray, CircleShape)
                .padding(13.dp)
        )
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}