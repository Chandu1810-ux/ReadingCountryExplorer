package com.example.readingcountryexplorer

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText

    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var logoutButton: Button

    private lateinit var homePanel: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()

        // Connect XML elements
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)

        loginButton = findViewById(R.id.loginButton)
        registerButton = findViewById(R.id.registerButton)
        logoutButton = findViewById(R.id.logoutButton)

        homePanel = findViewById(R.id.homePanel)

        // Login button
        loginButton.setOnClickListener {
            loginUser()
        }

        // Register button
        registerButton.setOnClickListener {
            registerUser()
        }

        // Logout button
        logoutButton.setOnClickListener {
            logoutUser()
        }

        updateScreen()
    }

    // USER REGISTRATION
    private fun registerUser() {

        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString()

        if (email.isEmpty() || password.length < 6) {
            showMessage(
                "Enter a valid email and password of at least 6 characters"
            )
            return
        }

        registerButton.isEnabled = false

        auth.createUserWithEmailAndPassword(
            email,
            password
        ).addOnCompleteListener { task ->

            registerButton.isEnabled = true

            if (task.isSuccessful) {

                showMessage("Registration successful!")

                updateScreen()

            } else {

                showMessage(
                    task.exception?.localizedMessage
                        ?: "Registration failed"
                )
            }
        }
    }

    // USER LOGIN
    private fun loginUser() {

        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            showMessage("Please enter email and password")
            return
        }

        loginButton.isEnabled = false

        auth.signInWithEmailAndPassword(
            email,
            password
        ).addOnCompleteListener { task ->

            loginButton.isEnabled = true

            if (task.isSuccessful) {

                showMessage("Login successful!")

                updateScreen()

            } else {

                showMessage(
                    task.exception?.localizedMessage
                        ?: "Login failed"
                )
            }
        }
    }

    // USER LOGOUT
    private fun logoutUser() {

        auth.signOut()

        showMessage("Logged out successfully")

        passwordInput.text.clear()

        updateScreen()
    }

    // UPDATE SCREEN
    private fun updateScreen() {

        val loggedIn = auth.currentUser != null

        emailInput.visibility =
            if (loggedIn) View.GONE else View.VISIBLE

        passwordInput.visibility =
            if (loggedIn) View.GONE else View.VISIBLE

        loginButton.visibility =
            if (loggedIn) View.GONE else View.VISIBLE

        registerButton.visibility =
            if (loggedIn) View.GONE else View.VISIBLE

        logoutButton.visibility =
            if (loggedIn) View.VISIBLE else View.GONE

        homePanel.visibility =
            if (loggedIn) View.VISIBLE else View.GONE
    }

    private fun showMessage(message: String) {

        Toast.makeText(
            this,
            message,
            Toast.LENGTH_LONG
        ).show()
    }
}
