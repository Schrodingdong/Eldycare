package com.ensias.eldycare.mobile.smartphone.composables.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.ensias.eldycare.mobile.smartphone.R

@Composable
fun TopAppBarEldycare(){
    Row (
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ){
        Box(
            contentAlignment = Alignment.Center,
        ){
            Box {
                Image(painter = painterResource(id = R.drawable.top_decor_rectangle), contentDescription = "top_decor_rectangle")
            }
            Text(
                text = "ELDYCARE",
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                color = Color.White,
            )
        }
    }
}
