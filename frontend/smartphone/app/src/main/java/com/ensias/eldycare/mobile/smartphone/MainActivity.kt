package com.ensias.eldycare.mobile.smartphone

import android.app.Fragment
import android.app.FragmentManager
import android.content.Context
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
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
import com.ensias.eldycare.mobile.smartphone.composables.auth.login.MyMessageListener
import com.ensias.eldycare.mobile.smartphone.composables.auth.signup.SignupPage
import com.ensias.eldycare.mobile.smartphone.composables.main.elderly.ElderHomePage
import com.ensias.eldycare.mobile.smartphone.composables.main.relative.RelativeHomePage
import com.ensias.eldycare.mobile.smartphone.data.database.AlertDatabase
import com.ensias.eldycare.mobile.smartphone.theme.ComposeTestTheme
import com.google.android.gms.wearable.Wearable
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel

class MainActivity : ComponentActivity() {
    val messageListener = MyMessageListener()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // init variables
        val context = this
        // init database
        AlertDatabase.init(context)
        // init wearable listener
        Wearable.getMessageClient(this).addListener(messageListener)

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
        // remove wearable listener
        Wearable.getMessageClient(this).removeListener(MyMessageListener())
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

class MyView : SkiaRenderer() {
    private val knnModel: KNNModel

    init {
        val modelFile = File(context.filesDir, "knn_model.joblib")
        val modelBytes = FileInputStream(modelFile).readBytes()
        val modelText = String(modelBytes)
        knnModel = KNNModel(modelText)
    }

    override fun onRender(canvas: Canvas, width: Int, height: Int, nanoTime: Long) {
        // Use knnModel for inference
    }
}
