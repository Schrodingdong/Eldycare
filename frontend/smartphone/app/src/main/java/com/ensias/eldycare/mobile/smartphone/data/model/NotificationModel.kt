package com.ensias.eldycare.mobile.smartphone.data.model

import com.ensias.eldycare.mobile.smartphone.data.AlertType

data class NotificationModel(
    val elderEmail: String,
    val alertMessage: String,
    val alertType: List<AlertType>,
    val alertTime: String,
    val location: String,
    val relativeEmail: List<String>? = null
)
