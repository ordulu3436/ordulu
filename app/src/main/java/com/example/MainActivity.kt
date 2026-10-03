package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AccountTree
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.ArchitectureScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.HourlyTimelineScreen
import com.example.ui.screens.LogFoodSheet
import com.example.ui.screens.ProfileSetupScreen
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.NutriLogTheme
import com.example.ui.viewmodel.NutriLogViewModel

enum class AppScreen(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
) {
    DASHBOARD("Özet", Icons.Filled.Dashboard, Icons.Outlined.Dashboard, "tab_dashboard"),
    HOURLY("Saatlik", Icons.Filled.BarChart, Icons.Outlined.BarChart, "tab_hourly"),
    PROFILE("Profil", Icons.Filled.Person, Icons.Outlined.Person, "tab_profile"),
    ARCHITECTURE("Mimari", Icons.Filled.AccountTree, Icons.Outlined.AccountTree, "tab_architecture")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NutriLogTheme {
                CaloreeApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaloreeApp(
    viewModel: NutriLogViewModel = viewModel()
) {
    val dashboardState by viewModel.dashboardState.collectAsStateWithLifecycle()
    val logSheetState by viewModel.logSheetState.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val weeklySummaries = remember(dashboardState.entries, userProfile) {
        viewModel.getPast7DaysSummaries()
    }

    var currentScreen by remember { mutableStateOf(AppScreen.DASHBOARD) }
    val snackbarHostState = remember { SnackbarHostState() }

    BackHandler(enabled = currentScreen != AppScreen.DASHBOARD) {
        currentScreen = AppScreen.DASHBOARD
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(EmeraldPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Fastfood,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "caloree",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                AppScreen.values().forEach { screen ->
                    val isSelected = currentScreen == screen
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentScreen = screen },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) screen.selectedIcon else screen.unselectedIcon,
                                contentDescription = screen.title
                            )
                        },
                        label = { Text(screen.title) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = EmeraldPrimary,
                            selectedTextColor = EmeraldPrimary,
                            indicatorColor = EmeraldPrimary.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag(screen.testTag)
                    )
                }
            }
        },
        floatingActionButton = {
            if (currentScreen == AppScreen.DASHBOARD || currentScreen == AppScreen.HOURLY) {
                ExtendedFloatingActionButton(
                    onClick = { viewModel.openLogSheet(null) },
                    containerColor = EmeraldPrimary,
                    contentColor = Color.White,
                    elevation = FloatingActionButtonDefaults.elevation(6.dp),
                    icon = { Icon(imageVector = Icons.Default.Add, contentDescription = "Besin Ekle") },
                    text = { Text("Besin Ekle", fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("fab_log_food")
                )
            }
        }
    ) { innerPadding ->
        Crossfade(
            targetState = currentScreen,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { screen ->
            when (screen) {
                AppScreen.DASHBOARD -> {
                    DashboardScreen(
                        state = dashboardState,
                        onNavigateDays = { days -> viewModel.navigateDateBy(days) },
                        onResetToday = { viewModel.resetToToday() },
                        onOpenLogFood = { mealType -> viewModel.openLogSheet(mealType) },
                        onDeleteEntry = { entry -> viewModel.deleteEntry(entry) },
                        onLogWater = { amount -> viewModel.logWater(amount) }
                    )
                }
                AppScreen.HOURLY -> {
                    HourlyTimelineScreen(
                        state = dashboardState,
                        weeklySummaries = weeklySummaries,
                        onDeleteEntry = { entry -> viewModel.deleteEntry(entry) }
                    )
                }
                AppScreen.PROFILE -> {
                    ProfileSetupScreen(
                        currentProfile = userProfile,
                        onSaveProfile = { profile -> viewModel.saveProfile(profile) },
                        snackbarHostState = snackbarHostState
                    )
                }
                AppScreen.ARCHITECTURE -> {
                    ArchitectureScreen()
                }
            }
        }

        // Doğal Metinle Besin Kaydetme Alt Paneli
        if (logSheetState.isOpen) {
            LogFoodSheet(
                state = logSheetState,
                onDismiss = { viewModel.closeLogSheet() },
                onQueryChange = { text -> viewModel.updateLogQuery(text) },
                onMealTypeChange = { meal -> viewModel.updateMealType(meal) },
                onHourChange = { hour -> viewModel.updateLogHour(hour) },
                onParseRequested = { viewModel.parseCurrentText() },
                onFieldChange = { name, cals, p, c, f, fib, portion ->
                    viewModel.updateEditedField(name, cals, p, c, f, fib, portion)
                },
                onCommit = { viewModel.commitFoodEntry() }
            )
        }
    }
}
