package com.ensias.eldycare.mobile.smartphone.data.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ensias.eldycare.mobile.smartphone.data.dao.AlertDao

@Database(
    entities = [Alert::class],
    version = 1,
    exportSchema = false
)
abstract class AlertDatabase : RoomDatabase() {
    abstract val alertDao: AlertDao

    companion object {
        const val DATABASE_NAME = "alert_database"
        var instance: AlertDatabase? = null
        fun init(context: Context) {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context,
                    AlertDatabase::class.java,
                    DATABASE_NAME
                ).build()
            }
        }
        fun getDbInstance(): AlertDatabase{
            if (instance == null) {
                throw Exception("AlertDatabase is not initialized")
            }
            return instance!!
        }
    }
}