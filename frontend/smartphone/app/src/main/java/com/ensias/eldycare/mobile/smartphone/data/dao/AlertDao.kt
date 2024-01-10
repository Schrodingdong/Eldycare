package com.ensias.eldycare.mobile.smartphone.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.ensias.eldycare.mobile.smartphone.data.database.Alert

@Dao
interface AlertDao {
    @Upsert
    suspend fun upsertAlert(alert: Alert)
    @Delete
    suspend fun deleteAlert(alert: Alert)

    // get alerts ordered by time and by a specific elderEmail at the same time with a given limit
    @Query("SELECT * FROM alert_table WHERE elderEmail = :elderEmail ORDER BY alertTime DESC LIMIT :limit")
    fun getAlertsByElderEmail(elderEmail: String, limit: Int = 10) : List<Alert>
}