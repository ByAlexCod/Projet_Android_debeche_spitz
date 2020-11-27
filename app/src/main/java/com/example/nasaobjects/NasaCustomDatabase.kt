package com.example.nasaobjects

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(NasaObjectEntity::class), version = 1)
abstract class NasaCustomDatabase : RoomDatabase() {
    abstract fun nasaObjectDao(): NasaObjectDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: NasaCustomDatabase? = null

        fun getDatabase(context: Context): NasaCustomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NasaCustomDatabase::class.java,
                    "nasa_db"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}