@file:Suppress("DEPRECATION")

package com.dicoding.pawscapstone.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.PopupWindow
import android.widget.Switch
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.dicoding.pawscapstone.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

@Suppress("DEPRECATION")
class AccountActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_account)

        sharedPreferences = getSharedPreferences("theme_pref", MODE_PRIVATE)

        val btnSettings = findViewById<Button>(R.id.btnSettings)
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val currentUser = FirebaseAuth.getInstance().currentUser
        val tvUsername = findViewById<TextView>(R.id.tvUsername)

        // Initialize GoogleSignInClient
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("21220532069-p9v09khf99545ff0mpdarssb8umacnbf.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        btnSettings.setOnClickListener {
            showSettingsPopup(it)
        }

        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            googleSignInClient.signOut().addOnCompleteListener {
                startActivity(Intent(this, LoginActivity::class.java))
                finish() // Optional: Close the current activity
            }
        }

        currentUser?.let {
            val username = it.displayName
            tvUsername.text = username
        }
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.menuBar)
        bottomNavigationView.selectedItemId =
            R.id.account // Make sure to set the correct id for the menu item

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }

                R.id.scan -> {
                    startActivity(Intent(this, ScanActivity::class.java))
                    true
                }

                R.id.reminder -> {
                    startActivity(Intent(this, ReminderActivity::class.java))
                    true
                }

                R.id.account -> true // Current Activity
                else -> false
            }
        }
    }

    private fun showSettingsPopup(view: View) {
        // Inflate the layout for the popup window
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_settings, null)

        // Create the popup window
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        // Load the saved theme preference
        val switchThemeMode = popupView.findViewById<Switch>(R.id.switchThemeMode)
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)
        switchThemeMode.isChecked = isDarkMode

        switchThemeMode.setOnCheckedChangeListener { _, isChecked ->
            setThemeMode(isChecked)
            // Save the preference
            with(sharedPreferences.edit()) {
                putBoolean("dark_mode", isChecked)
                apply()
            }
        }

        // Show the popup window
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

        // Dim background
        dimBackground(popupWindow)
    }

    private fun setThemeMode(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
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