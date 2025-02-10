package com.example.mcriderkit

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mcriderkit.data.DataSource
import com.example.mcriderkit.ui.ExamResultScreen
import com.example.mcriderkit.ui.ExamViewModel
import com.example.mcriderkit.ui.HazardTestMenuScreen
import com.example.mcriderkit.ui.HazardTestScreen
import com.example.mcriderkit.ui.HazardResultScreen
import com.example.mcriderkit.ui.HazardTestScreenReview
import com.example.mcriderkit.ui.HazardTestViewModel
import com.example.mcriderkit.ui.LTOMenuScreen
import com.example.mcriderkit.ui.studyMaterials.LicensingInformationMenu
import com.example.mcriderkit.ui.MainMenuScreen
import com.example.mcriderkit.ui.NonProQuizScreen
import com.example.mcriderkit.ui.ProfileScreen
import com.example.mcriderkit.ui.RevScreen
import com.example.mcriderkit.ui.studyMaterials.RoadSignsScreen
import com.example.mcriderkit.ui.SelectedVideoScreen
import com.example.mcriderkit.ui.SettingsScreen
import com.example.mcriderkit.ui.studyMaterials.TrafficRulesAndRegulationsScreen
import com.example.mcriderkit.ui.TutorialScreen
import com.example.mcriderkit.ui.studyMaterials.DLClassification
import com.example.mcriderkit.ui.studyMaterials.PermitsLicenses
import com.example.mcriderkit.ui.studyMaterials.Qualifications

enum class NavigationScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Profile(title = R.string.profile_),
    Settings(title = R.string.settings_),
    LTOMenu(title = R.string.lto_exam_menu),
    Reviewer(title = R.string.lto_reviewer),
    NonProExam(title = R.string.non_pro_exam),
    ExamResult(title = R.string.exam_result),
    HazardTestMenu(title = R.string.hazard_test_menu),
    SelectedVideo(title = R.string.selected_video_screen),
    HazardTest(title = R.string.hazard_test_screen),
    HazardTestReview(title = R.string.hazard_review),
    HazardResult(title = R.string.hazard_result),

    LicensingInfo(title = R.string.licensing_info),
    PermitsLicenses(title = R.string.permits_licenses),
    DriverLicense(title = R.string.driver_license),
    Qualifications(title = R.string.qualifications),
    GeneralProcedures(title = R.string.genera_procedures),

    RoadSignAndMarkings(title = R.string.road_signs_and_markings),
    RegulatorySigns(title = R.string.regulatory_signs),
    WarningSigns(title = R.string.warning_signs),
    GuideSigns(title = R.string.guide_signs),
    SignsOnExpressway(title = R.string.signs_expressway),

    TrafficRulesAndRegulations(title = R.string.traffic_rules_and_regulations)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavAppBar(
    currentScreen: NavigationScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
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
    quizViewModel: ExamViewModel,
    hazardViewModel: HazardTestViewModel,
) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Home", "Profile", "Settings")

//    val viewModel: ExamViewModel = viewModel()// Access ViewModel
    val selectedIcons = listOf(Icons.Filled.Home, Icons.Filled.Person, Icons.Filled.Settings)
    val unselectedIcons = listOf(Icons.Outlined.Home, Icons.Outlined.Person, Icons.Outlined.Settings)
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = NavigationScreen.valueOf(
        backStackEntry?.destination?.route ?: NavigationScreen.Start.name
    )
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val topLevelDestinations = setOf(
        NavigationScreen.Start.name,
        NavigationScreen.Profile.name,
        NavigationScreen.Settings.name
    )

    Scaffold(
        topBar = {
            if (currentRoute == NavigationScreen.ExamResult.name || currentRoute == NavigationScreen.HazardResult.name) {
                NavAppBar(
                    currentScreen = currentScreen,
                    canNavigateBack = false,
                    navigateUp = { navController.navigateUp() }
                )
            }
            else {
                NavAppBar(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null && currentScreen.name !in topLevelDestinations,
                    navigateUp = { navController.navigateUp() }
                )
            }
        },
        bottomBar = {
            if (currentRoute != NavigationScreen.HazardTest.name) {
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
                                2 -> navController.navigate(NavigationScreen.HazardTestMenu.name)
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
                        examType = DataSource.examList,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_medium)),
                        onNextButtonClicked = { index ->
                            when(index) {
                                1 -> navController.navigate(NavigationScreen.NonProExam.name)
                            }
                        }

                    )
                }
                composable(route = NavigationScreen.NonProExam.name) {
                    NonProQuizScreen(
                        viewModel = quizViewModel,
                        onNextButtonClicked = {
                            navController.navigate(NavigationScreen.ExamResult.name)
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_medium)))

                }
                composable(route = NavigationScreen.ExamResult.name) {
                    ExamResultScreen(
                        viewModel = quizViewModel,
                        onMainMenu = {
                            quizViewModel.resetQuiz()
                            navController.navigate(NavigationScreen.Start.name)
                                     },
                        onRetry = {
                            quizViewModel.resetQuiz()
                            navController.popBackStack(
                                NavigationScreen.NonProExam.name,
                                true
                            )
                        }
                    )
                }
                composable(route = NavigationScreen.Reviewer.name) {
                    RevScreen(
                        revCategory = DataSource.examCategoryList,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_medium)),
                        onNextButtonClicked = { index ->
                            when(index) {
                                0 -> navController.navigate(NavigationScreen.LicensingInfo.name)
                                1 -> navController.navigate(NavigationScreen.RoadSignAndMarkings.name)
                                2 -> navController.navigate(NavigationScreen.TrafficRulesAndRegulations.name)
                            }
                        }
                    )
                }
                composable(route = NavigationScreen.LicensingInfo.name) {
                    LicensingInformationMenu(
                        licensingInfoList = DataSource.licensingInfo,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_medium)),
                        onNextButtonClicked = { index ->
                            when(index) {
                                0 -> navController.navigate(NavigationScreen.PermitsLicenses.name)
                                1 -> navController.navigate(NavigationScreen.DriverLicense.name)
                                2 -> navController.navigate(NavigationScreen.Qualifications.name)
                                3 -> navController.navigate(NavigationScreen.GeneralProcedures.name)
                            }
                        }
                    )
                }
                composable(route = NavigationScreen.PermitsLicenses.name) {
                    PermitsLicenses(
                        onNextButtonClicked = {
                            navController.navigate(NavigationScreen.DriverLicense.name)
                        }
                    )
                }
                composable(route = NavigationScreen.DriverLicense.name) {
                    DLClassification(
                        onPrevButtonClicked = {
                            navController.navigate(NavigationScreen.PermitsLicenses.name)
                        },
                        onNextButtonClicked = {
                            navController.navigate(NavigationScreen.Qualifications.name)
                        }
                    )
                }
                composable(route = NavigationScreen.Qualifications.name) {
                    Qualifications(
                        onPrevButtonClicked = {
                            navController.navigate(NavigationScreen.DriverLicense.name)
                        },
                        onNextButtonClicked = {
                            navController.navigate(NavigationScreen.GeneralProcedures.name)
                        }
                    )
                }
                composable(route = NavigationScreen.RoadSignAndMarkings.name) {
                    RoadSignsScreen(
                        roadSignsList = DataSource.roadSignsAndMarkings,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_medium)),
                        onNextButtonClicked = { index ->
                            when(index) {
                                0 -> navController.navigate(NavigationScreen.RegulatorySigns.name)
                                1 -> navController.navigate(NavigationScreen.WarningSigns.name)
                                2 -> navController.navigate(NavigationScreen.GuideSigns.name)
                                3 -> navController.navigate(NavigationScreen.SignsOnExpressway.name)
                            }
                        }
                    )
                }
                composable(route = NavigationScreen.TrafficRulesAndRegulations.name) {
                    TrafficRulesAndRegulationsScreen()
                }
                composable(route = NavigationScreen.HazardTestMenu.name){
                    backStackEntry ->
                    val context = LocalContext.current
                    val hasSeenTutorial = remember { mutableStateOf(isTutorialShown(context)) }

                    if (!hasSeenTutorial.value) {
                        TutorialScreen(
                            tutorialType = "HazardTest",
                            onFinishTutorial = {
                            setTutorialShown(context)
                            hasSeenTutorial.value = true
                        })
                    } else{
                        HazardTestMenuScreen(
                            viewModel = hazardViewModel,
                            onClipSelected = { id ->
                                // Fetch the hazard test data by id
                                hazardViewModel.selectHazardTest(id)
                                navController.navigate(NavigationScreen.SelectedVideo.name)
                            }
                        )
                    }
                }
                composable(route = NavigationScreen.SelectedVideo.name){
                    val selectedVideo = hazardViewModel.selectedHazardTest.collectAsState().value
                    selectedVideo?.let {
                        SelectedVideoScreen(
                            video = it,
                            onStartTestClicked = { navController.navigate(NavigationScreen.HazardTest.name) },
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(dimensionResource(R.dimen.padding_medium))
                        )
                    }
                }
                composable(route = NavigationScreen.HazardTest.name){
                    val selectedVideo = hazardViewModel.selectedHazardTest.collectAsState().value
                    selectedVideo?.let {
                        HazardTestScreen(
                            viewModel = hazardViewModel,
                            video = it,
                            onTestFinished = {
                                navController.navigate(NavigationScreen.HazardResult.name)
                            },
                            onBackPressed = {navController.navigate(NavigationScreen.HazardTestMenu.name)}
                        )
                    }
                }
                composable(route = NavigationScreen.HazardTestReview.name){
                    val selectedVideo = hazardViewModel.selectedHazardTest.collectAsState().value
                    selectedVideo?.let {
                        HazardTestScreenReview(
                            video = it,
                            onReviewFinished = {
                                navController.navigate(NavigationScreen.HazardResult.name)
                            },
                            onBackPressed = { navController.navigate(NavigationScreen.HazardTestMenu.name) },
                        )
                    }
                }
                composable(route = NavigationScreen.HazardResult.name) {
                    val selectedVideo = hazardViewModel.selectedHazardTest.collectAsState().value
                    selectedVideo?.let{
                        HazardResultScreen(
                            video = it,
                            onMainMenu = {
                                navController.navigate(NavigationScreen.Start.name)
                            },
                            onRetry = {
                                navController.popBackStack(
                                    NavigationScreen.HazardTest.name,
                                    true
                                )
                            },
                            onReview = {
                                navController.navigate(NavigationScreen.HazardTestReview.name)
                            }
                        )
                    }
                }
            }
        }
    )
}


fun setTutorialShown(context: Context) {
    val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    sharedPreferences.edit().putBoolean("tutorial_shown", true).apply()
}

fun isTutorialShown(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("tutorial_shown", false)
}

