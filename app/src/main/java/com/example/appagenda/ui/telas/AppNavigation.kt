package com.example.appagenda.ui.telas

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appagenda.ui.telas.componentes.LoginScreen
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
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.appagenda.ui.telas.componentes.PerfilView

object Routes {
    const val LOGIN = "login"
    const val HOME = "home"

    const val AGENDA = "agenda"

    const val PERFIL = "perfil"
}

sealed class TabScreen(val route: String, val title: String, val icon: ImageVector){
    object Home: TabScreen(Routes.HOME, "Home", Icons.Outlined.Home)
    object Agenda: TabScreen(Routes.AGENDA, "Agenda", Icons.Outlined.DateRange)
    object Perfil: TabScreen(Routes.AGENDA, "Perfil", Icons.Outlined.Person)
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LOGIN){

        composable(Routes.LOGIN){
            LoginScreen (
                onLoginSucess = {
                    navController.navigate("main_app"){
                        popUpTo(Routes.LOGIN) {inclusive = true}
                    }
                }
            )
        }
        composable("main_app"){
           MainScreenWithTabs()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenWithTabs(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    val tabScreen = listOf(
        TabScreen.Home,
        TabScreen.Agenda,
        TabScreen.Perfil
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                tabScreen.forEach {screen ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.title
                            )
                        },
                        label = {Text(screen.title)},
                        selected = currentDestination?.hierarchy?.any {
                            it.route == screen.route
                        } == true,
                        onClick = {
                            navController.navigate(screen.route){
                                popUpTo(navController.graph.findStartDestination()){
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
    ) {  innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(Routes.HOME) { HomeView()  }
            composable(Routes.AGENDA) {AgendaView()  }
            composable(Routes.PERFIL) { PerfilView() }
        }
    }
}