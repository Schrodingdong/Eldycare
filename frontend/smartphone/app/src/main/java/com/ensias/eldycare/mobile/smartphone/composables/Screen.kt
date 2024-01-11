package com.ensias.eldycare.mobile.smartphone.composables

import androidx.annotation.StringRes

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
//    object AutenticationPage : Screen("authentication", com.example.composetest.R.string.authentication)
    object Signup : Screen("signup", com.ensias.eldycare.mobile.R.string.signup)
    object Login : Screen("login", com.ensias.eldycare.mobile.R.string.login)

    object ElderHomePage : Screen("elder_home", com.ensias.eldycare.mobile.R.string.elder_home)
    object RemindersPage : Screen("reminders", com.ensias.eldycare.mobile.R.string.reminders)
    object AlertsPage : Screen("alerts", com.ensias.eldycare.mobile.R.string.alerts)

    object RelativeHomePage : Screen("relative_home", com.ensias.eldycare.mobile.R.string.relative_home)
    object ConnectionsPage : Screen("connections", com.ensias.eldycare.mobile.R.string.connections)
}
