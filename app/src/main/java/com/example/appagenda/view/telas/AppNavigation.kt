package com.example.appagenda.view.telas

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.appagenda.view.AgendaView

object Routes {
    const val LOGIN = "login"
    const val HOME = "home"
    const val TAB_HOME = "tab_home"
    const val AGENDA = "agenda"
    const val PERFIL = "perfil"

    const val NOTIFICACAO = "notificacao"
}

sealed class TabScreen(val route: String, val title: String, val icon: ImageVector){
    object Home: TabScreen(Routes.HOME, "Home", Icons.Outlined.Home)
    object Agenda: TabScreen(Routes.AGENDA, "Agenda", Icons.Outlined.DateRange)
    object Perfil: TabScreen(Routes.PERFIL, "Perfil", Icons.Outlined.Person)
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onForgotPassword = {
                    // Implementar tela de recuperação de senha
                }
            )
        }
        composable(Routes.HOME) {
            MainScreenWithTabs(rootNavController = navController)
        }

        composable(Routes.NOTIFICACAO) {
            NotificacaoView()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenWithTabs(rootNavController: NavController) {
    val tabsNavController = rememberNavController()

    val tabScreens = listOf(
        TabScreen.Home,
        TabScreen.Agenda,
        TabScreen.Perfil
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by tabsNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                tabScreens.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(imageVector = screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            tabsNavController.navigate(screen.route) {
                                popUpTo(tabsNavController.graph.findStartDestination().id) { saveState = true }
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
            navController = tabsNavController,
            startDestination = Routes.TAB_HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.HOME) {
                HomeView(onNotificationsClick = { rootNavController.navigate(Routes.NOTIFICACAO) })
            }
            composable(Routes.TAB_HOME) { HomeView() }
            composable(Routes.AGENDA) { AgendaView() }
            composable(Routes.PERFIL) { PerfilView() }
        }
    }
}
