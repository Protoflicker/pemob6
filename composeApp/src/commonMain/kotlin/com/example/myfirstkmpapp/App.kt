package com.example.myfirstkmpapp

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myfirstkmpapp.navigation.Screen
import com.example.myfirstkmpapp.network.HttpClientFactory
import com.example.myfirstkmpapp.network.NewsRepository
import com.example.myfirstkmpapp.screens.*
import com.example.myfirstkmpapp.viewmodel.NewsUiState
import com.example.myfirstkmpapp.viewmodel.NewsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    MaterialTheme {
        val client = remember { HttpClientFactory.create() }
        val repository = remember { NewsRepository(client) }
        val viewModel = remember { NewsViewModel(repository) }
        val navController = rememberNavController()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val isMainScreen = currentRoute in listOf(Screen.Notes.route, Screen.Favorites.route, Screen.Profile.route)
        val isDetailScreen = currentRoute?.startsWith("news_detail") == true

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    Text("Menu", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))
                    HorizontalDivider()
                    NavigationDrawerItem(
                        label = { Text("Berita Terkini") },
                        selected = currentRoute == Screen.Notes.route,
                        onClick = {
                            navController.navigate(Screen.Notes.route) { popUpTo(0) }
                            scope.launch { drawerState.close() }
                        },
                        icon = { Icon(Icons.Default.List, null) },
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                    NavigationDrawerItem(
                        label = { Text("Profile") },
                        selected = currentRoute == Screen.Profile.route,
                        onClick = {
                            navController.navigate(Screen.Profile.route) { popUpTo(0) }
                            scope.launch { drawerState.close() }
                        },
                        icon = { Icon(Icons.Default.Person, null) },
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        ) {
            Scaffold(
                topBar = {
                    if (isMainScreen) {
                        TopAppBar(
                            title = { Text("News Reader App") },
                            navigationIcon = {
                                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                    Icon(Icons.Default.Menu, "Menu")
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                        )
                    } else if (isDetailScreen) {
                        TopAppBar(
                            title = { Text("Baca Berita", fontSize = 18.sp) },
                            navigationIcon = {
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                        )
                    }
                },
                bottomBar = {
                    if (isMainScreen) {
                        NavigationBar {
                            listOf(Screen.Notes, Screen.Favorites, Screen.Profile).forEach { screen ->
                                NavigationBarItem(
                                    selected = currentRoute == screen.route,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = { Icon(screen.icon!!, null) },
                                    label = { Text(if (screen == Screen.Notes) "News" else screen.label!!) }
                                )
                            }
                        }
                    }
                }
            ) { paddingValues ->
                NavHost(
                    navController = navController,
                    startDestination = Screen.Notes.route,
                    modifier = Modifier.padding(paddingValues)
                ) {
                    composable(Screen.Notes.route) {
                        NewsScreen(
                            viewModel = viewModel,
                            onArticleClick = { index -> navController.navigate("news_detail/$index") }
                        )
                    }
                    composable(Screen.Favorites.route) { FavoritesScreen() }
                    composable(Screen.Profile.route) { ProfileScreen() }

                    composable(
                        route = "news_detail/{articleIndex}",
                        arguments = listOf(navArgument("articleIndex") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val index = backStackEntry.arguments?.getInt("articleIndex") ?: 0
                        val state by viewModel.uiState.collectAsState()

                        if (state is NewsUiState.Success) {
                            val article = (state as NewsUiState.Success).articles.getOrNull(index)
                            if (article != null) {
                                NewsDetailScreen(article = article)
                            }
                        }
                    }
                }
            }
        }
    }
}