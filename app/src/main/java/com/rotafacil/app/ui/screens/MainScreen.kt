package com.rotafacil.app.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rotafacil.app.ui.screens.profile.PerfilScreen
import com.rotafacil.app.ui.screens.routes.RotasScreen
import com.rotafacil.app.ui.screens.trips.ViagensScreen
import com.rotafacil.app.ui.screens.vehicles.VeiculosScreen
import com.rotafacil.app.ui.screens.details.RotaDetailScreen
import com.rotafacil.app.ui.screens.details.ViagemDetailScreen
import com.rotafacil.app.ui.screens.stats.StatsScreen
import com.rotafacil.app.ui.screens.routes.RotaFormScreen

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Rotas : Screen("rotas", "Rotas", Icons.Default.Route)
    object Viagens : Screen("viagens", "Viagens", Icons.Default.DirectionsBus)
    object Veiculos : Screen("veiculos", "Veículos", Icons.Default.DirectionsCar)
    object Perfil : Screen("perfil", "Perfil", Icons.Default.Person)
    object Stats : Screen("stats", "Estatísticas", Icons.Default.Analytics)
}

// Rotas de detalhes
sealed class DetailScreen(val route: String) {
    object RotaDetail : DetailScreen("rota_detail/{rotaId}")
    object ViagemDetail : DetailScreen("viagem_detail/{viagemId}")
    object RotaForm : DetailScreen("rota_form/{rotaId?}")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                val screens = listOf(
                    Screen.Rotas,
                    Screen.Viagens,
                    Screen.Veiculos,
                    Screen.Perfil,
                    Screen.Stats
                )
                
                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Rotas.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Rotas.route) {
                RotasScreen(
                    onRotaClick = { rotaId ->
                        navController.navigate("rota_detail/$rotaId")
                    },
                    onAddRota = {
                        navController.navigate("rota_form/")
                    },
                    onEditRota = { rotaId ->
                        navController.navigate("rota_form/$rotaId")
                    },
                    onDeleteRota = { rotaId ->
                        // TODO: Implementar diálogo de confirmação
                        // Por enquanto, apenas navegar de volta
                        navController.navigateUp()
                    }
                )
            }
            composable(Screen.Viagens.route) {
                ViagensScreen(
                    onViagemClick = { viagemId ->
                        navController.navigate("viagem_detail/$viagemId")
                    }
                )
            }
            composable(Screen.Veiculos.route) {
                VeiculosScreen()
            }
            composable(Screen.Perfil.route) {
                PerfilScreen(onLogout = onLogout)
            }
            composable(Screen.Stats.route) {
                StatsScreen()
            }
            
            // Telas de detalhes
            composable(
                route = DetailScreen.RotaDetail.route,
                arguments = listOf(navArgument("rotaId") { type = NavType.StringType })
            ) { backStackEntry ->
                val rotaId = backStackEntry.arguments?.getString("rotaId") ?: ""
                RotaDetailScreen(
                    rotaId = rotaId,
                    navController = navController
                )
            }
            
            composable(
                route = DetailScreen.ViagemDetail.route,
                arguments = listOf(navArgument("viagemId") { type = NavType.StringType })
            ) { backStackEntry ->
                val viagemId = backStackEntry.arguments?.getString("viagemId") ?: ""
                ViagemDetailScreen(
                    viagemId = viagemId,
                    navController = navController
                )
            }
            
            // Formulário de rotas
            composable(
                route = DetailScreen.RotaForm.route,
                arguments = listOf(navArgument("rotaId") { 
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                })
            ) { backStackEntry ->
                val rotaId = backStackEntry.arguments?.getString("rotaId")
                RotaFormScreen(
                    rotaId = rotaId,
                    navController = navController
                )
            }
        }
    }
} 