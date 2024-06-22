package com.dicoding.pawscapstone.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ReminderDao {
    @Insert
    fun insert(reminder: Reminder)

    @Query("SELECT * FROM reminders")
    fun getAllReminders(): List<Reminder>
}
