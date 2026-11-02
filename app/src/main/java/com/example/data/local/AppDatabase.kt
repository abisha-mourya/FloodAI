package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.EmergencyContact
import com.example.data.model.FamilyMember
import com.example.data.model.ShelterItem
import com.example.data.model.SupplyItem
import com.example.data.model.WaterLevelLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        EmergencyContact::class,
        FamilyMember::class,
        SupplyItem::class,
        WaterLevelLog::class,
        ShelterItem::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun emergencyContactDao(): EmergencyContactDao
    abstract fun familyMemberDao(): FamilyMemberDao
    abstract fun supplyItemDao(): SupplyItemDao
    abstract fun waterLevelLogDao(): WaterLevelLogDao
    abstract fun shelterDao(): ShelterDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "floodaid_disaster.db"
                )
                    .addCallback(AppDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class AppDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialDatabase(database)
                    }
                }
            }
        }

        suspend fun populateInitialDatabase(database: AppDatabase) {
            database.emergencyContactDao().insertAll(PreloadedData.defaultContacts)
            database.supplyItemDao().insertAll(PreloadedData.defaultSupplies)
            database.shelterDao().insertAll(PreloadedData.defaultShelters)
            database.familyMemberDao().insertAll(PreloadedData.defaultFamily)
            for (log in PreloadedData.initialWaterLogs) {
                database.waterLevelLogDao().insertLog(log)
            }
        }
    }
}
