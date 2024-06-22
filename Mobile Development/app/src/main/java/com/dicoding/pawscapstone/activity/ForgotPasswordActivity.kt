package com.dicoding.pawscapstone.activity

import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.ImageButton
import android.widget.Toast
import com.dicoding.pawscapstone.R

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var progressDialog: ProgressDialog
    lateinit var btnBack: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgotpassword)
        btnBack = findViewById(R.id.back_button)

        // Find views by ID
        val edEmail = findViewById<EditText>(R.id.editTextEmail)
        val sendEmailBtn = findViewById<Button>(R.id.buttonSendCode)
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Loading")
        progressDialog.setMessage("Please wait...")
        progressDialog.setCanceledOnTouchOutside(false)


        // Add text watcher to validate email input
        edEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                // Optionally add validation logic here
            }
        })

        // Set onClick listener for the send email button
        sendEmailBtn.setOnClickListener {
            if (validateEmail(edEmail)) {
                if (isConnected(this)) {
                    progressDialog.show()
                    FirebaseAuth.getInstance()
                        .sendPasswordResetEmail(edEmail.text.toString().trim())
                        .addOnSuccessListener {
                            progressDialog.dismiss()
                            longToastShow("Send Email Successful")
                            finish()
                        }
                        .addOnFailureListener {
                            progressDialog.dismiss()
                            it.message?.let { it1 -> longToastShow(it1) }
                        }
                } else {
                    longToastShow("No Internet Connection!")
                }
            }
        }

        btnBack.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    // Method to validate email
    private fun validateEmail(email: EditText): Boolean {
        val emailInput = email.text.toString().trim()
        return if (emailInput.isEmpty()) {
            email.error = "Email is required."
            false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email.error = "Please enter a valid email address."
            false
        } else {
            email.error = null
            true
        }
    }

    // Method to check internet connection
    private fun isConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }

    // Method to show long toast message
    private fun longToastShow(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}