package com.example.foodresq.views

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.foodresq.R
import com.example.foodresq.adaptersEtc.SessionManager
import com.example.foodresq.classes.DbHelper
import com.example.foodresq.classes.User

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize SessionManager
        val sessionManager = SessionManager(this)

        // Check login state
        if (sessionManager.isLoggedIn()) {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        val username = findViewById<EditText>(R.id.name)
        val userEmail = findViewById<EditText>(R.id.email)
        val userPassword = findViewById<EditText>(R.id.password)
        val buttonReg = findViewById<Button>(R.id.buttonReg)
        val hint = findViewById<TextView>(R.id.hint4auth)
        val backButton = findViewById<ImageView>(R.id.backButton)
        val db = DbHelper(this, null)

        buttonReg.setOnClickListener {
            val login = username.text.toString().trim()
            val email = userEmail.text.toString().trim()
            val password = userPassword.text.toString().trim()
            if (login == "" || email == "" || password == "") {
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
            } else if (db.getUserLogin(login) || db.getUserEmail(email)) {
                Toast.makeText(this, "Пользователь уже зарегистрирован", Toast.LENGTH_LONG).show()
            } else {
                val user = User(login, email, password)
                db.addUser(user)
                Toast.makeText(this, "Пользователь $login успешно добавлен", Toast.LENGTH_LONG).show()
                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        hint.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }

        backButton.setOnClickListener {
            // Handle back button functionality
            finish()
        }
    }
}
