package com.dicoding.pawscapstone.activity

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.dicoding.pawscapstone.R
import com.dicoding.pawscapstone.database.Reminder
import com.dicoding.pawscapstone.database.ReminderBroadcastReceiver
import com.dicoding.pawscapstone.database.ReminderDatabase
import com.dicoding.pawscapstone.databinding.ActivityReminderBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Calendar

class ReminderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReminderBinding
    private lateinit var reminderDatabase: ReminderDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        reminderDatabase =
            Room.databaseBuilder(applicationContext, ReminderDatabase::class.java, "reminders")
                .build()

        createNotificationChannel()
        setupGridItemClickListeners()
        setupBottomNavigation()
    }

    private fun setupGridItemClickListeners() {
        binding.food.setOnClickListener { showSetReminderPopup("Food") }
        binding.vitamin.setOnClickListener { showSetReminderPopup("Vitamin") }
        binding.appointment.setOnClickListener { showSetReminderPopup("Appointment") }
        binding.play.setOnClickListener { showSetReminderPopup("Play") }
    }

    private fun showSetReminderPopup(reminderType: String) {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_setreminder, null)

        val popupWindow = PopupWindow(
            popupView,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            true
        )
        popupWindow.showAtLocation(binding.root, Gravity.CENTER, 0, 0)

        val reminderTitle = popupView.findViewById<EditText>(R.id.editTitle)
        val timePicker = popupView.findViewById<TimePicker>(R.id.timePicker)
        val btnSaveReminder = popupView.findViewById<Button>(R.id.saveReminder)

        btnSaveReminder.setOnClickListener {
            val title = reminderTitle.text.toString()
            val hour = timePicker.hour
            val minute = timePicker.minute

            if (title.isNotEmpty()) {
                saveReminder(reminderType, title, hour, minute)
                popupWindow.dismiss()
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        dimBackground(popupWindow)
    }

    private fun saveReminder(reminderType: String, title: String, hour: Int, minute: Int) {
        val reminder = Reminder(title = title, category = reminderType, hour = hour, minute = minute)
        Thread {
            try {
                reminderDatabase.reminderDao().insert(reminder)
                runOnUiThread {
                    Toast.makeText(
                        this,
                        "$reminderType reminder set: $title at ${formatTime(hour, minute)}",
                        Toast.LENGTH_SHORT
                    ).show()
                    setReminderNotification(title, hour, minute)
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "Failed to set reminder: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    private fun formatTime(hour: Int, minute: Int): String {
        val formattedMinute = if (minute < 10) "0$minute" else minute.toString()
        val amPm = if (hour < 12) "AM" else "PM"
        val formattedHour = if (hour == 0 || hour == 12) 12 else hour % 12
        return "$formattedHour:$formattedMinute $amPm"
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun setReminderNotification(title: String, hour: Int, minute: Int) {
        val intent = Intent(this, ReminderBroadcastReceiver::class.java).apply {
            putExtra("title", title)
        }
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Reminder Channel"
            val descriptionText = "Channel for reminder notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("REMINDER_CHANNEL", name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setupBottomNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.menuBar)
        bottomNavigationView.selectedItemId = R.id.reminder // Make sure to set the correct id for the menu item

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.reminder -> true // Current Activity
                R.id.home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.scan -> {
                    startActivity(Intent(this, ScanActivity::class.java))
                    true
                }
                R.id.account -> {
                    startActivity(Intent(this, AccountActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun dimBackground(popupWindow: PopupWindow) {
        val container = popupWindow.contentView.parent as View
        val wm = getSystemService(WINDOW_SERVICE) as WindowManager
        val p = container.layoutParams as WindowManager.LayoutParams
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        p.dimAmount = 0.5f
        wm.updateViewLayout(container, p)
    }
}