package com.ensias.eldycare.mobile.smartphone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.ensias.eldycare.mobile.smartphone.composables.Screen
import com.ensias.eldycare.mobile.smartphone.composables.auth.login.LoginPage
import com.ensias.eldycare.mobile.smartphone.composables.auth.signup.SignupPage
import com.ensias.eldycare.mobile.smartphone.composables.main.elderly.AlertsPage
import com.ensias.eldycare.mobile.smartphone.composables.main.elderly.RemindersPage
import com.ensias.eldycare.mobile.smartphone.composables.main.relative.ConnectionsPage
import com.ensias.eldycare.mobile.smartphone.theme.ComposeTestTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTestTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "auth"){
                    navigation(
                        startDestination = Screen.Signup.route,
                        route = "auth"
                    ){
                        composable(Screen.Signup.route) {
                            SignupPage(navController)
                        }
                        composable(Screen.Login.route) {
                            LoginPage(navController)
                        }
                    }
                    navigation(
                        startDestination = Screen.RemindersPage.route,
                        route = "main-elderly"
                    ){
                        composable(Screen.RemindersPage.route) {
                            RemindersPage(navController)
                        }
                        composable(Screen.AlertsPage.route) {
                            AlertsPage(navController)
                        }
                    }
                    navigation(
                        startDestination = Screen.ConnectionsPage.route,
                        route = "main-relative"
                    ){
                        composable(Screen.ConnectionsPage.route) {
                            ConnectionsPage(navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
inline fun <reified T: ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T{
    val navGraphRoute = destination.parent?.route?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}
