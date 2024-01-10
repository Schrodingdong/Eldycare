package com.ensias.eldycare.mobile.smartphone.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alert_table")
data class Alert(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val elderEmail: String,
    val alertMessage: String,
    val alertType: String, // semi-colon separated list of alert types
    val alertTime: String,
    val alertDate: String,
    val location: String
)
