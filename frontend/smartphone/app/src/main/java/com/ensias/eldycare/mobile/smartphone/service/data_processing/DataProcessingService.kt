package com.ensias.eldycare.mobile.smartphone.service.data_processing

import com.ensias.eldycare.mobile.smartphone.api.ApiClient
import com.ensias.eldycare.mobile.smartphone.data.AlertType
import com.ensias.eldycare.mobile.smartphone.data.model.NotificationModel
import com.ensias.eldycare.mobile.smartphone.service.AlertService
import com.ensias.eldycare.mobile.smartphone.service.NotificationService
import java.time.Instant

class DataProcessingService {
    fun process() {
        // TODO : implement this function
        // ...
        val alertType = mutableListOf<AlertType>();
        val isFall = detectFall();
        val isCardiac = detectCardiac();
        if (isFall) {
            alertType.add(AlertType.FALL);
        }
        if (isCardiac) {
            alertType.add(AlertType.CARDIAC);
        }
        if (alertType.isNotEmpty()) {
//            AlertService().sendAlert(alertType);
        }
    }


    fun detectFall() : Boolean {
        // TODO : implement this function
        return true;
    }

    fun detectCardiac() : Boolean {
        // TODO : implement this function
        return true;
    }
}