package com.ensias.eldycare.mobile.smartphone.data

import com.ensias.eldycare.mobile.smartphone.data.database.Alert

data class Connection(
    var name: String,
    var email: String,
    var phone: String,
    var lastAlert: Alert? = null
)