package com.example.foodresq.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodresq.R
import com.example.foodresq.adaptersEtc.SessionManager
import com.example.foodresq.classes.DbHelper

class Profile : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val userMail: TextView = findViewById(R.id.userMail)
        val userLogin: TextView = findViewById(R.id.userLogin)
        val avatar: ImageView = findViewById(R.id.profileAvatar)
        val db = DbHelper(this, null)
        val sessionManager = SessionManager(this)
        val userEmail = sessionManager.getUserEmail()
        userMail.text = userEmail

        val current = userEmail?.let { db.getUserByEmail(it) }
        val userId = current?.let { db.getUserIdByEmail(it.email) }
        if (current != null) {
            userLogin.text = current.login
        }
        else{
            userLogin.text = "AAAAAAAAAAAAAAAAAAAA"
        }

        if(current!=null){
            val imageID = resources.getIdentifier(current.avatar, "drawable", packageName)
            avatar.setImageResource(imageID)
        }
        else{
            val imageID = resources.getIdentifier("empty_avatar", "drawable", packageName)
            avatar.setImageResource(imageID)
        }


        val logoutButton: TextView = findViewById(R.id.buttonLogout)
        logoutButton.setOnClickListener {
            sessionManager.clearLoginState()
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }


        var string = "aaa"
        string += userId?.let { db.readCart(it) }?.forEach { it.name }
        Toast.makeText(this, userId?.let { db.readCart(it) }.toString(), Toast.LENGTH_LONG).show()

        val backButton: ImageView = findViewById(R.id.backButton)
        backButton.setOnClickListener {

            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish()
        }

        val editTop: ImageView = findViewById(R.id.editTop)
        editTop.setOnClickListener {
            val intent = Intent(this, ProfileEdit::class.java)
            startActivity(intent)
            finish()
        }

        val myRestButton: Button = findViewById(R.id.myRest)
//        if (current != null) {
//            if(current.is_owner != 1){
//                myRestButton.visibility = View.GONE
//            }
              //else{
                  //myRestButton.visibility = View.VISIBLE
        //}
//        }
        myRestButton.setOnClickListener {
            val intent = Intent(this@Profile, MyRest::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        val userMail: TextView = findViewById(R.id.userMail)
        val userLogin: TextView = findViewById(R.id.userLogin)
        val avatar: ImageView = findViewById(R.id.profileAvatar)
        val db = DbHelper(this, null)
        val sessionManager = SessionManager(this)

        val userEmail = sessionManager.getUserEmail()
        userMail.text = userEmail

        val current = userEmail?.let { db.getUserByEmail(it) }
        if (current != null) {
            userLogin.text = current.login
        }
        else{
            userLogin.text = "AAAAAAAAAAAAAAAAAAAA"
        }

        if(current!=null){
            val imageID = resources.getIdentifier(current.avatar, "drawable", packageName)
            avatar.setImageResource(imageID)
        }
        else{
            val imageID = resources.getIdentifier("empty_avatar", "drawable", packageName)
            avatar.setImageResource(imageID)
        }

    }

}