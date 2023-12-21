package com.ensias.eldycare.mobile.smartphone.composables

import androidx.annotation.StringRes

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
//    object AutenticationPage : Screen("authentication", com.example.composetest.R.string.authentication)
    object Signup : Screen("signup", com.ensias.eldycare.mobile.smartphone.R.string.signup)
    object Login : Screen("login", com.ensias.eldycare.mobile.smartphone.R.string.login)
    object RemindersPage : Screen("reminders", com.ensias.eldycare.mobile.smartphone.R.string.reminders)
    object AlertsPage : Screen("alerts", com.ensias.eldycare.mobile.smartphone.R.string.alerts)
    object ConnectionsPage : Screen("connections", com.ensias.eldycare.mobile.smartphone.R.string.connections)
}
