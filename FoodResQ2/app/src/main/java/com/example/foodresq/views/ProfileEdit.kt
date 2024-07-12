package com.example.foodresq.views

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodresq.R
import com.example.foodresq.adaptersEtc.SessionManager
import com.example.foodresq.classes.DbHelper

class ProfileEdit : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userMail: EditText = findViewById(R.id.userMailEnter)
        val userLogin: EditText = findViewById(R.id.userLoginEnter)
        val avatar: ImageView = findViewById(R.id.profileAvatar)
        val db = DbHelper(this, null)
        val sessionManager = SessionManager(this)
        val userEmail = sessionManager.getUserEmail()

        userMail.setText(userEmail)

        val current = userEmail?.let { db.getUserByEmail(it) }
        if (current != null) {
            userLogin.setText(current.login)
        } else {
            userLogin.setText("No login found")
        }

        if (current != null) {
            val imageID = resources.getIdentifier(current.avatar, "drawable", packageName)
            avatar.setImageResource(imageID)
        } else {
            val imageID = resources.getIdentifier("empty_avatar", "drawable", packageName)
            avatar.setImageResource(imageID)
        }

        val backButton: ImageView = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        val buttonSubmit: Button = findViewById(R.id.buttonSubmit)
        buttonSubmit.setOnClickListener {
            val newLogin = userLogin.text.toString().trim()
            val newEmail = userMail.text.toString().trim()  // Get the updated email from TextView

            if (newLogin.isEmpty() || newEmail.isEmpty()) {
                Toast.makeText(this, "Заполните поле логина и email", Toast.LENGTH_LONG).show()
            } else {
                if (current != null) {
                    db.updateUser(current, newLogin, newEmail)

                    // Update email in SharedPreferences
                    val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    with(sharedPreferences.edit()) {
                        putBoolean("is_logged_in", true)  // Ensure the user is still logged in
                        putString("user_email", newEmail)  // Update the email to the new value
                        apply()
                    }

                    // Update session manager
                    sessionManager.saveLoginState(true, newLogin)

                    Toast.makeText(this, "Данные успешно обновлены", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, Profile::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Ошибка обновления данных", Toast.LENGTH_LONG).show()
                }
            }
        }


    }
}
