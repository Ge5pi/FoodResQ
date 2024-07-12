package com.example.foodresq.views

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodresq.R
import com.example.foodresq.classes.DbHelper

class AuthActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val email = findViewById<EditText>(R.id.emailAuth)
        val password = findViewById<EditText>(R.id.passwordAuth)
        val buttonAuth = findViewById<Button>(R.id.buttonAuth)
        val db = DbHelper(this, null)
        val hint = findViewById<TextView>(R.id.hint4reg)
        val backButton = findViewById<ImageView>(R.id.backButton)

        hint.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        buttonAuth.setOnClickListener {
            val userEmail = email.text.toString().trim()
            val userPassword = password.text.toString().trim()
            if (userPassword == "" || userEmail == "") {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_LONG).show()
            } else {
                val isAuth = db.getUser(userEmail, userPassword)
                if (isAuth) {
                    Toast.makeText(this, "Вы вошли в аккаунт", Toast.LENGTH_LONG).show()
                    email.text.clear()
                    password.text.clear()

                    // Save login state and user email
                    val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    with(sharedPreferences.edit()) {
                        putBoolean("is_logged_in", true)
                        putString("user_email", userEmail)
                        apply()
                    }

                    val intent = Intent(this, Home::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Неверное имя пользователя или пароль", Toast.LENGTH_LONG).show()
                }
            }
        }

        backButton.setOnClickListener {
            // Handle back button functionality
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
