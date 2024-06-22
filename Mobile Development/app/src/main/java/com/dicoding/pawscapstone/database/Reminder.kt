package com.dicoding.pawscapstone.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String,
    // Menyimpan referensi drawable resource
    val hour: Int,
    val minute: Int
)