package com.ensias.eldycare.mobile.smartphone.data

data class Connection(
    var name: String,
    var email: String,
    var phone: String,
    var lastAlert: Alert? = null
)