package com.example.mcriderkit

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.MaterialTheme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mcriderkit.data.DataSource
import com.example.mcriderkit.ui.ExamResultScreen
import com.example.mcriderkit.ui.ExamViewModel
import com.example.mcriderkit.ui.LTOMenuScreen
import com.example.mcriderkit.ui.MainMenuScreen
import com.example.mcriderkit.ui.NonProQuizScreen
import com.example.mcriderkit.ui.ProfileScreen
import com.example.mcriderkit.ui.RevScreen
import com.example.mcriderkit.ui.SettingsScreen
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.room.Room

enum class NavigationScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Profile(title = R.string.profile_),
    Settings(title = R.string.settings_),
    LTOMenu(title = R.string.lto_exam_menu),
    Reviewer(title = R.string.lto_reviewer),
    NonProExam(title = R.string.non_pro_exam),
    ExamResult(title = R.string.exam_result)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavAppBar(
    currentScreen: NavigationScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
){
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun NavigationApp(
    navController: NavHostController = rememberNavController(),
) {

    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Home", "Profile", "Settings")
    val viewModel: ExamViewModel = viewModel()// Access ViewModel
    val selectedIcons = listOf(Icons.Filled.Home, Icons.Filled.Person, Icons.Filled.Settings)
    val unselectedIcons = listOf(Icons.Outlined.Home, Icons.Outlined.Person, Icons.Outlined.Settings)
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = NavigationScreen.valueOf(
        backStackEntry?.destination?.route ?: NavigationScreen.Start.name
    )
    val topLevelDestinations = setOf(
        NavigationScreen.Start.name,
        NavigationScreen.Profile.name,
        NavigationScreen.Settings.name
    )

    Scaffold(
        topBar = {
            NavAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null && currentScreen.name !in topLevelDestinations,
                navigateUp = { navController.navigateUp() }
            )
        },
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
                                contentDescription = item
                            )
                        },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            when (index) {
                                0 -> navController.navigate(NavigationScreen.Start.name)
                                1 -> navController.navigate(NavigationScreen.Profile.name)
                                2 -> navController.navigate(NavigationScreen.Settings.name)
                            }
                        }
                    )
                }
            }
        },
        content = { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = NavigationScreen.Start.name,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(route = NavigationScreen.Start.name) {
                    MainMenuScreen(
                        menuList = DataSource.menuList,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_medium)),
                        onNextButtonClicked = { index ->
                            when (index) {
                                0 -> navController.navigate(NavigationScreen.LTOMenu.name)
                                1 -> navController.navigate(NavigationScreen.Reviewer.name)
                                2 -> navController.navigate(NavigationScreen.Settings.name)
                            }
                        }
                    )
                }
                composable(route = NavigationScreen.Profile.name) {
                    ProfileScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_medium))
                    )
                }
                composable(route = NavigationScreen.Settings.name) {
                    SettingsScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_medium))
                    )
                }
                composable(route = NavigationScreen.LTOMenu.name) {
                    LTOMenuScreen(
                        examType = DataSource.examTypeList,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_medium)),
                        onNextButtonClicked = { index ->
                            when(index) {
                                0 -> navController.navigate(NavigationScreen.NonProExam.name)
                            }
                        }

                    )
                }
                composable(route = NavigationScreen.NonProExam.name) {
                    NonProQuizScreen(
                        viewModel = viewModel,
                        onNextButtonClicked = {
                            navController.navigate(NavigationScreen.ExamResult.name)
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_medium)))

                }
                composable(route = NavigationScreen.ExamResult.name) {
                    ExamResultScreen(
                        viewModel = viewModel,
                        onMainMenu = {
                            viewModel.resetQuiz()
                            navController.navigate(NavigationScreen.Start.name)
                                     },
                        onRetry = {
                            viewModel.resetQuiz()
                            navController.popBackStack(
                                NavigationScreen.NonProExam.name,
                                false
                            )
                        }
                    )
                }
                composable(route = NavigationScreen.Reviewer.name) {
                    RevScreen(
                        examCategory = DataSource.examCategoryList,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_medium))
                    )
                }
            }
        }
    )
}