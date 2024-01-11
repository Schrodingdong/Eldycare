package com.ensias.eldycare.mobile.smartphone.composables.main.relative.dialogs

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ensias.eldycare.mobile.smartphone.theme.Typography

@Composable
fun AddConnectionPopup(onDismiss: () -> Unit, onAddConnection: (String) -> Unit) {
    val mContext = LocalContext.current
    var elderEmail by remember { mutableStateOf("") }
    Surface (
        modifier = Modifier.fillMaxSize(),
        color = Color.Black.copy(alpha = 0.5f)
    ){
        Dialog(
            onDismissRequest = onDismiss,
        ){
            Card (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ){
                Column(
                    modifier = Modifier.padding(16.dp)
                ){
                    Text(text = "Add a connection", fontSize = Typography.bodyLarge.fontSize, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(text = "Who do you want to add ?", fontSize = Typography.bodyMedium.fontSize)
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = elderEmail,
                        onValueChange = { elderEmail = it },
                        label = { Text("Elder Email") }
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ){
                        Button(onClick = {
                            onAddConnection(elderEmail)
                            Toast.makeText(mContext, "Connection added", Toast.LENGTH_SHORT).show()
                            onDismiss()
                        }) {
                            Text(text = "Add")
                        }
                    }

                }

            }
        }
    }
}

@Preview
@Composable
fun AddConnectionPopupPreview() {
    AddConnectionPopup(onDismiss = {}, onAddConnection = {})
}
