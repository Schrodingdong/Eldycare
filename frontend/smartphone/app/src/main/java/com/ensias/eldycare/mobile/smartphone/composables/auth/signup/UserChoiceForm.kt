package com.ensias.eldycare.mobile.smartphone.composables.auth.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ensias.eldycare.mobile.smartphone.R
import com.ensias.eldycare.mobile.smartphone.data.SignupData
import com.ensias.eldycare.mobile.smartphone.UserType

/**
 * USER CHOICE PAGE
 */
@Composable
fun UserChoiceForm(signupData: SignupData, signupStep: Int, onSignupDataChange: (SignupData) -> Unit, onSignupStepChange: (Int) -> Unit, navController: NavController){
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopDecorWithText("WHICH ARE\nYOU")
            Column(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    CloseRelativeCard(signupData, signupStep, onSignupDataChange, onSignupStepChange)
                    Spacer(modifier = Modifier.size(16.dp))
                    ElderlyCard(signupData, signupStep, onSignupDataChange, onSignupStepChange)
                }
                LoginAlternative(navController)
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CloseRelativeCard(signupData: SignupData, signupStep: Int, onSignupDataChange: (SignupData) -> Unit, onSignupStepChange: (Int) -> Unit){
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 8.dp
        ),
        onClick = {
            onSignupDataChange(signupData.copy(userType = UserType.RELATIVE))
            onSignupStepChange(signupStep + 1)
        },
        modifier= Modifier.width(225.dp)
    ){
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Close Relative", fontSize = 24.sp)
            Image(
                modifier = Modifier.size(190.dp),
                painter = painterResource(id = R.drawable.close_relative),
                contentDescription = "close_relative"
            )
        }
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElderlyCard(signupData: SignupData, signupStep: Int, onSignupDataChange: (SignupData) -> Unit, onSignupStepChange: (Int) -> Unit){
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 8.dp
        ),
        onClick = {
            onSignupDataChange(signupData.copy(userType = UserType.ELDERLY))
            onSignupStepChange(signupStep + 1)
        },
        modifier= Modifier.width(225.dp)
    ){
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Elderly person", fontSize = 24.sp)
            Image(
                modifier = Modifier.size(190.dp),
                painter = painterResource(id = R.drawable.elderly_person),
                contentDescription = "close_relative"
            )
        }
    }

}
