package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.ArrowPrayerLogEntity
import com.example.data.model.CachedMeditationEntity
import com.example.data.model.CachedPrayerSectionEntity
import com.example.data.model.PrayerLogEntity
import com.example.data.model.SpiritualNoteEntity

@Database(
    entities = [
        PrayerLogEntity::class,
        ArrowPrayerLogEntity::class,
        CachedPrayerSectionEntity::class,
        CachedMeditationEntity::class,
        SpiritualNoteEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AgpeyaDatabase : RoomDatabase() {
    abstract fun prayerLogDao(): PrayerLogDao
    abstract fun arrowPrayerDao(): ArrowPrayerDao
    abstract fun prayerTextDao(): PrayerTextDao
    abstract fun meditationDao(): MeditationDao
    abstract fun spiritualNoteDao(): SpiritualNoteDao

    companion object {
        @Volatile
        private var INSTANCE: AgpeyaDatabase? = null

        fun getDatabase(context: Context): AgpeyaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AgpeyaDatabase::class.java,
                    "agpeya_database.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
