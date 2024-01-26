package com.ensias.eldycare.mobile.smartphone

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat
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
import com.ensias.eldycare.mobile.smartphone.composables.main.elderly.ElderHomePage
import com.ensias.eldycare.mobile.smartphone.composables.main.relative.RelativeHomePage
import com.ensias.eldycare.mobile.smartphone.data.AlertType
import com.ensias.eldycare.mobile.smartphone.data.database.AlertDatabase
import com.ensias.eldycare.mobile.smartphone.service.AlertService
import com.ensias.eldycare.mobile.smartphone.theme.ComposeTestTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable

class MainActivity : ComponentActivity() {
    companion object{
        lateinit var fusedLocationClient: FusedLocationProviderClient
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // init variables
        val context = this
        // init database
        AlertDatabase.init(context)

        // location permissions
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        setContent {
            ComposeTestTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "auth"){
                    navigation(
                        startDestination = Screen.Login.route,
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
                        startDestination = Screen.ElderHomePage.route,
                        route = "main-elderly"
                    ){
                        composable(Screen.ElderHomePage.route) {
                            ElderHomePage(navController, context)
                        }
                    }
                    navigation(
                        startDestination = Screen.RelativeHomePage.route,
                        route = "main-relative"
                    ){
                        composable(Screen.RelativeHomePage.route) {
                            RelativeHomePage(navController, context)
                        }
                    }
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
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


