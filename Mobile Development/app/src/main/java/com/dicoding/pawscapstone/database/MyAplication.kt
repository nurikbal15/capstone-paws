package com.dicoding.pawscapstone.database

import android.app.Application
import androidx.room.Room

class MyApplication : Application() {
    companion object {
        var database: AppDatabase? = null
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "pet-database"
        ).build()
    }
}
