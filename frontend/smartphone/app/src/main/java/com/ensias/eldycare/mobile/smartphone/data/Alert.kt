package com.ensias.eldycare.mobile.smartphone.data

import java.util.Date

@Deprecated("Use Alert from data.database")
data class Alert(
    var alertTime: Date,
    var alertMessage: String?
)
