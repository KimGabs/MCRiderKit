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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mcriderkit.data.DataSource
import com.example.mcriderkit.ui.ExamResultScreen
import com.example.mcriderkit.ui.HazardResultScreen
import com.example.mcriderkit.ui.HazardTestMenuScreen
import com.example.mcriderkit.ui.HazardTestScreen
import com.example.mcriderkit.ui.HazardTestScreenReview
import com.example.mcriderkit.ui.HazardTestViewModel
import com.example.mcriderkit.ui.LTOMenuScreen
import com.example.mcriderkit.ui.MainMenuScreen
import com.example.mcriderkit.ui.NonProExamViewModel
import com.example.mcriderkit.ui.NonProQuizScreen
import com.example.mcriderkit.ui.ProExamViewModel
import com.example.mcriderkit.ui.ProQuizScreen
import com.example.mcriderkit.ui.ProfileScreen
import com.example.mcriderkit.ui.RevScreen
import com.example.mcriderkit.ui.SelectedVideoScreen
import com.example.mcriderkit.ui.SettingsScreen
import com.example.mcriderkit.ui.StudentExamViewModel
import com.example.mcriderkit.ui.StudentQuizScreen
import com.example.mcriderkit.ui.TutorialScreen
import com.example.mcriderkit.ui.components.BaseExamViewModel
import com.example.mcriderkit.ui.components.BaseHazardViewModel
import com.example.mcriderkit.ui.studyMaterials.AttAndBehaveScreen
import com.example.mcriderkit.ui.studyMaterials.BlowbagetsChecklistScreen
import com.example.mcriderkit.ui.studyMaterials.DLClassification
import com.example.mcriderkit.ui.studyMaterials.DrivingAndRoadCourtesyScreen
import com.example.mcriderkit.ui.studyMaterials.EmergencyScreen
import com.example.mcriderkit.ui.studyMaterials.EngineOilInspectionScreen
import com.example.mcriderkit.ui.studyMaterials.ExpresswaySigns
import com.example.mcriderkit.ui.studyMaterials.GeneralProcedures
import com.example.mcriderkit.ui.studyMaterials.GuideSigns
import com.example.mcriderkit.ui.studyMaterials.LicensingInformationMenu
import com.example.mcriderkit.ui.studyMaterials.LongitudinalLines
import com.example.mcriderkit.ui.studyMaterials.OtherLines
import com.example.mcriderkit.ui.studyMaterials.PavementMarkings
import com.example.mcriderkit.ui.studyMaterials.PermitsLicenses
import com.example.mcriderkit.ui.studyMaterials.Qualifications
import com.example.mcriderkit.ui.studyMaterials.RegulatorySign
import com.example.mcriderkit.ui.studyMaterials.RoadSignsScreen
import com.example.mcriderkit.ui.studyMaterials.TireInspectionScreen
import com.example.mcriderkit.ui.studyMaterials.TrafficRulesAndRegulationsScreen
import com.example.mcriderkit.ui.studyMaterials.TrafficViolationsAndPenaltiesScreen
import com.example.mcriderkit.ui.studyMaterials.TraversalLines
import com.example.mcriderkit.ui.studyMaterials.VehicleMaintenanceAndInspectionMenuScreen
import com.example.mcriderkit.ui.studyMaterials.WarningSign

sealed class NavigationScreen(val route: String, @StringRes val title: Int) {
    object Start : NavigationScreen("Start", R.string.Home)
    object Profile : NavigationScreen("Profile", R.string.profile_)
    object Settings : NavigationScreen("Settings", R.string.settings_)
    object LTOMenu : NavigationScreen("LTOMenu", R.string.lto_exam_menu)
    object Reviewer : NavigationScreen("Reviewer", R.string.lto_reviewer)
    object StudentExam : NavigationScreen("StudentExam", R.string.student_exam)
    object NonProExam : NavigationScreen("NonProExam", R.string.non_pro_exam)
    object ProExam : NavigationScreen("ProExam", R.string.pro_exam)
    object ExamResult : NavigationScreen("ExamResult", R.string.exam_result)
    object HazardTestMenu : NavigationScreen("HazardTestMenu", R.string.hazard_test_menu)
    object SelectedVideo : NavigationScreen("SelectedVideo", R.string.selected_video_screen)
    object HazardTest : NavigationScreen("HazardTest", R.string.hazard_test_screen)
    object HazardTestReview : NavigationScreen("HazardTestReview", R.string.hazard_review)
    object HazardResult : NavigationScreen("HazardResult", R.string.hazard_result)

    object LicensingInfo : NavigationScreen("LicensingInfo", R.string.licensing_info)
    object PermitsLicenses : NavigationScreen("PermitsLicenses", R.string.permits_licenses)
    object DriverLicense : NavigationScreen("DriverLicense", R.string.driver_license)
    object Qualifications : NavigationScreen("Qualifications", R.string.qualifications)
    object GeneralProcedures : NavigationScreen("GeneralProcedures", R.string.genera_procedures)

    object RoadSignAndMarkings : NavigationScreen("RoadSignAndMarkings", R.string.road_signs_and_markings)
    object RegulatorySigns : NavigationScreen("RegulatorySigns", R.string.regulatory_signs)
    object WarningSigns : NavigationScreen("WarningSigns", R.string.warning_signs)
    object GuideSigns : NavigationScreen("GuideSigns", R.string.guide_signs)
    object SignsOnExpressway : NavigationScreen("SignsOnExpressway", R.string.signs_expressway)

    object PavementMarkings : NavigationScreen("PavementMarkings", R.string.PAVEMENT_MARK)
    object LongitudinalLines : NavigationScreen("LongitudinalLines", R.string.LONG_MARK_SEC)
    object TraversalLines : NavigationScreen("TraversalLines", R.string.TRAV_SEC)
    object OtherLines : NavigationScreen("OtherLines", R.string.OTHER_LINES_SEC)

    object TrafficRulesAndRegulations : NavigationScreen("TrafficRulesAndRegulations", R.string.traffic_rules_and_regulations)
    object ViolationsAndPenalties: NavigationScreen("TrafficViolationsAndPenalties", R.string.TVP_SEC)
    object VehicleMaintenanceAndInspection: NavigationScreen("VehicleMaintenanceAndInspection", R.string.VMI_SEC)
    object TireInspection: NavigationScreen("TireInspection", R.string.tire_inspection_banner_title)
    object EngineOilInspection: NavigationScreen("EngineOilInspection", R.string.engine_oil_inspection_banner_title)
    object BlowbagetsInspection: NavigationScreen("BLOWBAGETS Inspection", R.string.blowbagets_banner_title)
    object DrivingAndRoadCourtesy: NavigationScreen("Drive Safety and Road Courtesy", R.string.DRC_Banner)
    object AttitudeAndBehavior: NavigationScreen("Attitude and Behavior", R.string.AB_Banner)
    object Emergency: NavigationScreen("Dealing with Emergency Situations", R.string.Emergency_banner)

    companion object {
        fun fromRoute(route: String?): NavigationScreen {
            return when (route) {
                Start.route -> Start
                Profile.route -> Profile
                Settings.route -> Settings
                else -> Start // Default to Start if route is unknown
            }
        }
    }

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
        title = { Text(text = stringResource(currentScreen.title), style = MaterialTheme.typography.titleLarge) },
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
    nonProQuizViewModel: NonProExamViewModel,
    proQuizViewModel: ProExamViewModel,
    studentExamViewModel: StudentExamViewModel,
    hazardViewModel: HazardTestViewModel,
    darkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit
) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Home", "Profile", "Settings")

    val selectedIcons = listOf(Icons.Filled.Home, Icons.Filled.Person, Icons.Filled.Settings)
    val unselectedIcons = listOf(Icons.Outlined.Home, Icons.Outlined.Person, Icons.Outlined.Settings)
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = NavigationScreen.fromRoute(
        backStackEntry?.destination?.route ?: NavigationScreen.Start.route
    )
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val topLevelDestinations = setOf(
        NavigationScreen.Start.route,
        NavigationScreen.Profile.route,
        NavigationScreen.Settings.route
    )

    Scaffold(
        topBar = {
            if (currentRoute?.startsWith("ExamResult") == true || currentRoute?.startsWith("HazardResult") == true) {
                NavAppBar(
                    currentScreen = currentScreen,
                    canNavigateBack = false,
                    navigateUp = { navController.navigateUp() }
                )
            }
            else {
                NavAppBar(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null && currentScreen.route !in topLevelDestinations,
                    navigateUp = { navController.navigateUp() }
                )
            }
        },
        bottomBar = {
            if (currentRoute != NavigationScreen.HazardTest.route) {
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
                                    0 -> navController.navigate(NavigationScreen.Start.route)
                                    1 -> navController.navigate(NavigationScreen.Profile.route)
                                    2 -> navController.navigate(NavigationScreen.Settings.route)
                                    else -> throw IllegalArgumentException("Invalid Screen")
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
                startDestination = NavigationScreen.Start.route,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(route = NavigationScreen.Start.route) {
                    MainMenuScreen(
                        menuList = DataSource.menuList,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_medium)),
                        onNextButtonClicked = { index ->
                            when (index) {
                                0 -> navController.navigate(NavigationScreen.LTOMenu.route)
                                1 -> navController.navigate(NavigationScreen.Reviewer.route)
                                2 -> navController.navigate(NavigationScreen.HazardTestMenu.route)
                                else -> throw IllegalArgumentException("Invalid Screen")

                            }
                        }
                    )
                }
                composable(route = NavigationScreen.Profile.route) {
                    ProfileScreen(
                        studentViewModel = studentExamViewModel,
                        NonProViewModel = nonProQuizViewModel,
                        proViewModel = proQuizViewModel,
                        viewModel = hazardViewModel,
                        context = LocalContext.current
                    )
                }
                composable(route = NavigationScreen.Settings.route) {
                    SettingsScreen(
                        darkMode = darkMode,
                        onDarkModeToggle = onToggleDarkMode,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_medium))
                    )
                }
                composable(route = NavigationScreen.LTOMenu.route) {
                    LTOMenuScreen(
                        examType = DataSource.examList,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_medium)),
                        onNextButtonClicked = { index ->
                            when(index) {
                                0 -> navController.navigate(NavigationScreen.StudentExam.route)
                                1 -> navController.navigate(NavigationScreen.NonProExam.route)
                                2 -> navController.navigate(NavigationScreen.ProExam.route)
                                else -> throw IllegalArgumentException("Invalid Screen")
                            }
                        }
                    )
                }
                composable(route = NavigationScreen.NonProExam.route) {
                    NonProQuizScreen(
                        viewModel = nonProQuizViewModel,
                        onNextButtonClicked = {
                            navController.navigate("ExamResult/NonPro")
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_medium)))

                }
                composable(route = NavigationScreen.StudentExam.route) {
                    StudentQuizScreen(
                        viewModel = studentExamViewModel,
                        onNextButtonClicked = {
                            navController.navigate("ExamResult/Student")
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_medium)))

                }
                composable(route = NavigationScreen.ProExam.route) {
                    ProQuizScreen(
                        viewModel = proQuizViewModel,
                        onNextButtonClicked = {
                            navController.navigate("ExamResult/Pro")
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_medium)))

                }
                composable(
                    route = "${NavigationScreen.ExamResult.route}/{examType}",
                    arguments = listOf(navArgument("examType") { type = NavType.StringType })
                ) { backStackEntry ->
                    val examType = backStackEntry.arguments?.getString("examType") ?: "NonPro"

                    val selectedViewModel: BaseExamViewModel = when (examType) {
                        "Student" -> studentExamViewModel
                        "Pro" -> proQuizViewModel
                        else -> nonProQuizViewModel
                    }

                    ExamResultScreen(
                        viewModel = selectedViewModel,
                        onMainMenu = {
                            selectedViewModel.resetQuiz()
                            navController.navigate(NavigationScreen.Start.route)
                        },
                        onRetry = {
                            selectedViewModel.resetQuiz()
                            navController.popBackStack(
                                when (examType) {
                                    "Student" -> NavigationScreen.StudentExam.route
                                    "Pro" -> NavigationScreen.ProExam.route
                                    else -> NavigationScreen.NonProExam.route
                                },
                                true
                            )
                        }
                    )
                }

                composable(route = NavigationScreen.Reviewer.route) {
                    RevScreen(
                        revCategory = DataSource.examCategoryList,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_medium)),
                        onNextButtonClicked = { index ->
                            when(index) {
                                0 -> navController.navigate(NavigationScreen.LicensingInfo.route)
                                1 -> navController.navigate(NavigationScreen.RoadSignAndMarkings.route)
                                2 -> navController.navigate(NavigationScreen.TrafficRulesAndRegulations.route)
                                3 -> navController.navigate(NavigationScreen.ViolationsAndPenalties.route)
                                4 -> navController.navigate(NavigationScreen.VehicleMaintenanceAndInspection.route)
                                5 -> navController.navigate(NavigationScreen.DrivingAndRoadCourtesy.route)
                                else -> throw IllegalArgumentException("Invalid Screen")
                            }
                        }
                    )
                }
                composable(route = NavigationScreen.LicensingInfo.route) {
                    LicensingInformationMenu(
                        licensingInfoList = DataSource.licensingInfo,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_medium)),
                        onNextButtonClicked = { index ->
                            when(index) {
                                0 -> navController.navigate(NavigationScreen.PermitsLicenses.route)
                                1 -> navController.navigate(NavigationScreen.DriverLicense.route)
                                2 -> navController.navigate(NavigationScreen.Qualifications.route)
                                3 -> navController.navigate(NavigationScreen.GeneralProcedures.route)
                                else -> throw IllegalArgumentException("Invalid Screen")
                            }
                        }
                    )
                }
                composable(route = NavigationScreen.PermitsLicenses.route) {
                    PermitsLicenses(
                        onNextButtonClicked = {
                            navController.navigate(NavigationScreen.DriverLicense.route)
                        }
                    )
                }
                composable(route = NavigationScreen.DriverLicense.route) {
                    DLClassification(
                        onPrevButtonClicked = {
                            navController.navigate(NavigationScreen.PermitsLicenses.route)
                        },
                        onNextButtonClicked = {
                            navController.navigate(NavigationScreen.Qualifications.route)
                        }
                    )
                }
                composable(route = NavigationScreen.Qualifications.route) {
                    Qualifications(
                        onPrevButtonClicked = {
                            navController.navigate(NavigationScreen.DriverLicense.route)
                        },
                        onNextButtonClicked = {
                            navController.navigate(NavigationScreen.GeneralProcedures.route)
                        }
                    )
                }
                composable(route = NavigationScreen.GeneralProcedures.route) {
                    GeneralProcedures(
                        onPrevButtonClicked = {
                            navController.navigate(NavigationScreen.Qualifications.route)
                        }
                    )
                }
                composable(route = NavigationScreen.RoadSignAndMarkings.route) {
                    RoadSignsScreen(
                        roadSignsList = DataSource.roadSignsAndMarkings,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_medium)),
                        onNextButtonClicked = { index ->
                            when(index) {
                                0 -> navController.navigate(NavigationScreen.RegulatorySigns.route)
                                1 -> navController.navigate(NavigationScreen.WarningSigns.route)
                                2 -> navController.navigate(NavigationScreen.GuideSigns.route)
                                3 -> navController.navigate(NavigationScreen.SignsOnExpressway.route)
                                4 -> navController.navigate(NavigationScreen.PavementMarkings.route)
                                else -> throw IllegalArgumentException("Invalid Screen")
                            }
                        }
                    )
                }
                composable(route = NavigationScreen.RegulatorySigns.route) {
                    RegulatorySign(
                        onNextButtonClicked = {
                            navController.navigate(NavigationScreen.WarningSigns.route)
                        }
                    )
                }
                composable(route = NavigationScreen.WarningSigns.route){
                    WarningSign (
                        onPrevButtonClicked = {
                            navController.navigate(NavigationScreen.RegulatorySigns.route)
                        },
                        onNextButtonClicked = {
                            navController.navigate(NavigationScreen.GuideSigns.route)
                        }
                    )
                }
                composable(route = NavigationScreen.GuideSigns.route){
                    GuideSigns (
                        onPrevButtonClicked = {
                            navController.navigate(NavigationScreen.WarningSigns.route)
                        },
                        onNextButtonClicked = {
                            navController.navigate(NavigationScreen.SignsOnExpressway.route)
                        }
                    )
                }
                composable(route = NavigationScreen.SignsOnExpressway.route){
                    ExpresswaySigns(
                        onPrevButtonClicked = {
                            navController.navigate(NavigationScreen.GuideSigns.route)
                        }
                    )
                }
                composable(route = NavigationScreen.PavementMarkings.route){
                    PavementMarkings(
                        pavementMarkingList = DataSource.PavementMarkings,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_medium)),
                        onNextButtonClicked = { index ->
                            when(index) {
                                0 -> navController.navigate(NavigationScreen.LongitudinalLines.route)
                                1 -> navController.navigate(NavigationScreen.TraversalLines.route)
                                2 -> navController.navigate(NavigationScreen.OtherLines.route)
                            }
                        }
                    )
                }
                composable(route = NavigationScreen.LongitudinalLines.route) {
                    LongitudinalLines(
                        onNextButtonClicked = {
                            navController.navigate(NavigationScreen.TraversalLines.route)
                        }
                    )
                }
                composable(route = NavigationScreen.TraversalLines.route) {
                    TraversalLines(
                        onPrevButtonClicked = {
                            navController.navigate(NavigationScreen.LongitudinalLines.route)
                        },
                        onNextButtonClicked = {
                            navController.navigate(NavigationScreen.OtherLines.route)
                        }
                    )
                }
                composable(route = NavigationScreen.OtherLines.route) {
                    OtherLines(
                        onPrevButtonClicked = {
                            navController.navigate(NavigationScreen.TraversalLines.route)
                        }
                    )
                }
                composable(route = NavigationScreen.TrafficRulesAndRegulations.route) {
                    TrafficRulesAndRegulationsScreen()
                }
                composable(route = NavigationScreen.ViolationsAndPenalties.route) {
                    TrafficViolationsAndPenaltiesScreen()
                }

                composable(route = NavigationScreen.VehicleMaintenanceAndInspection.route) {
                    VehicleMaintenanceAndInspectionMenuScreen(
                        MaintenanceInfoList = DataSource.VehicleMaintenance,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_medium)),
                        onNextButtonClicked = { index ->
                            when(index) {
                                0 -> navController.navigate(NavigationScreen.TireInspection.route)
                                1 -> navController.navigate(NavigationScreen.EngineOilInspection.route)
                                2 -> navController.navigate(NavigationScreen.BlowbagetsInspection.route)
                                else -> throw IllegalArgumentException("Invalid Screen")
                            }
                        }
                    )
                }
                composable(route = NavigationScreen.TireInspection.route) {
                    TireInspectionScreen(
                        onNextButtonClicked = {
                            navController.navigate(NavigationScreen.EngineOilInspection.route)
                        }
                    )
                }
                composable(route = NavigationScreen.EngineOilInspection.route) {
                    EngineOilInspectionScreen(
                        onNextButtonClicked = {
                            navController.navigate(NavigationScreen.BlowbagetsInspection.route)
                        },
                        onPrevButtonClicked = {
                            navController.navigate(NavigationScreen.TireInspection.route)
                        },
                    )
                }
                composable(route = NavigationScreen.BlowbagetsInspection.route) {
                    BlowbagetsChecklistScreen(
                        onPrevButtonClicked = {
                            navController.navigate(NavigationScreen.EngineOilInspection.route)
                        }
                    )
                }
                composable(route = NavigationScreen.DrivingAndRoadCourtesy.route) {
                    DrivingAndRoadCourtesyScreen(
                        drcList = DataSource.DrivingAndRoadCourtesy,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_medium)),
                        onNextButtonClicked = { index ->
                            when(index) {
                                0 -> navController.navigate(NavigationScreen.AttitudeAndBehavior.route)
                                1 -> navController.navigate(NavigationScreen.Emergency.route)
                                else -> throw IllegalArgumentException("Invalid Screen")
                            }
                        }
                    )
                }
                composable(route = NavigationScreen.AttitudeAndBehavior.route) {
                    AttAndBehaveScreen (
                        onNextButtonClicked = {
                            navController.navigate(NavigationScreen.Emergency.route)
                        }
                    )
                }
                composable(route = NavigationScreen.Emergency.route) {
                    EmergencyScreen (
                        onPrevButtonClicked = {
                            navController.navigate(NavigationScreen.AttitudeAndBehavior.route)
                        },
                        onNextButtonClicked = {
                            navController.navigate(NavigationScreen.AttitudeAndBehavior.route)
                        }
                    )
                }

                /* End of Reviewer */
                /* Hazard Testing Menu */
                composable(route = NavigationScreen.HazardTestMenu.route){
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
                                navController.navigate(NavigationScreen.SelectedVideo.route)
                            }
                        )
                    }
                }
                composable(route = NavigationScreen.SelectedVideo.route){
                    val selectedVideo = hazardViewModel.selectedHazardTest.collectAsState().value
                    selectedVideo?.let {
                        SelectedVideoScreen(
                            video = it,
                            onStartTestClicked = { navController.navigate(NavigationScreen.HazardTest.route) },
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(dimensionResource(R.dimen.padding_medium))
                        )
                    }
                }
                composable(route = NavigationScreen.HazardTest.route){
                    val selectedVideo = hazardViewModel.selectedHazardTest.collectAsState().value
                    selectedVideo?.let {
                        HazardTestScreen(
                            viewModel = hazardViewModel,
                            video = it,
                            onTestFinished = {
                                navController.navigate(NavigationScreen.HazardResult.route)
                            },
                            onBackPressed = {navController.navigate(NavigationScreen.HazardTestMenu.route)}
                        )
                    }
                }
                composable(route = NavigationScreen.HazardTestReview.route){
                    val selectedVideo = hazardViewModel.selectedHazardTest.collectAsState().value
                    selectedVideo?.let {
                        HazardTestScreenReview(
                            video = it,
                            onReviewFinished = {
                                navController.navigate(NavigationScreen.HazardResult.route)
                            },
                            onBackPressed = { navController.navigate(NavigationScreen.HazardTestMenu.route) },
                        )
                    }
                }
                composable(route = NavigationScreen.HazardResult.route) {
                    val selectedVideo = hazardViewModel.selectedHazardTest.collectAsState().value
                    selectedVideo?.let{
                        HazardResultScreen(
                            video = it,
                            onMainMenu = {
                                navController.navigate(NavigationScreen.Start.route)
                            },
                            onRetry = {
                                navController.popBackStack(
                                    NavigationScreen.HazardTest.route,
                                    true
                                )
                            },
                            onReview = {
                                navController.navigate(NavigationScreen.HazardTestReview.route)
                            },
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

