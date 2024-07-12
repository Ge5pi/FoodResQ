package com.example.foodresq.views

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodresq.R
import com.example.foodresq.adaptersEtc.SessionManager
import com.example.foodresq.classes.DbHelper
import com.example.foodresq.classes.Product
import com.example.foodresq.classes.Restaurant

class AddPosition : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_position)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val name: TextView = findViewById(R.id.name)
        val price: TextView = findViewById(R.id.price)
        val desc: TextView = findViewById(R.id.desc)
        val button: Button = findViewById(R.id.addPositionButton)
        val db = DbHelper(this, null)
        val sessionManager = SessionManager(this)
        val userEmail = sessionManager.getUserEmail()
        val current = userEmail?.let { db.getUserByEmail(it) }
        val rest: Restaurant? = current?.let { db.getRest(it.rest_id) }

        button.setOnClickListener {
            val factName = name.text.trim()
            val factPrice = price.text.trim()
            val factDesc = desc.text.trim()
            if(factName!="" && factPrice!= "" && factDesc != ""){
                if (rest != null) {
                    db.addPosition(Product(
                        db.getAllPositions().last().id+1,
                        factName.toString(),
                        factDesc.toString(),
                        "",
                        Integer.parseInt(factPrice.toString()),
                        rest.id
                    ))
                }
                else{
                    Toast.makeText(this, "Заполните все поля", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}