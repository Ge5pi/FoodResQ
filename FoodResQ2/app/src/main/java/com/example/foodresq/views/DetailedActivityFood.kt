package com.example.foodresq.views

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodresq.R
import com.example.foodresq.adaptersEtc.SessionManager
import com.example.foodresq.classes.DbHelper

class DetailedActivityFood : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_food)

        val db = DbHelper(this, null)

        val sessionManager = SessionManager(this)
        val userEmail = sessionManager.getUserEmail()
        val current = userEmail?.let { db.getUserByEmail(it) }


        val product: ImageView = findViewById(R.id.product)
        val name: TextView = findViewById(R.id.name)
        val price: TextView = findViewById(R.id.price)
        val desc: TextView = findViewById(R.id.desc)
        val rest: ImageView = findViewById(R.id.rest)
        val priceFooter: TextView = findViewById(R.id.priceFooter)


        val bundle: Bundle? = intent.extras

        val imageProduct = bundle!!.getString("product")
        val prodId = bundle.getInt("prodId")
        val nameProduct = bundle.getString("name")
        val priceProduct = bundle.getInt("price")
        val descProduct = bundle.getString("desc")
        val restProduct = bundle.getString("rest")
        val realRestId = bundle.getInt("restId")
        val realRestaurant = db.getRest(realRestId)//ресторан на который перешли
        if (realRestaurant != null) {
            println("realRest: ${realRestaurant.id}")
        }
        if (current != null) {
            println("current: ${current.rest_id}")
        }

        val imageId = resources.getIdentifier(imageProduct, "drawable", packageName)
        product.setImageResource(imageId)
        name.text = nameProduct
        price.text = priceProduct.toString()
        desc.text = descProduct
        val imageRestId = resources.getIdentifier(restProduct, "drawable", packageName)
        rest.setImageResource(imageRestId)
        priceFooter.text = priceProduct.toString()

        val delete: ImageView = findViewById(R.id.delete)
        //if(current?.is_owner == 1){
            if(current?.rest_id == realRestaurant!!.id){
                delete.setVisibility(View.VISIBLE)
            }
        //}

        delete.setOnClickListener {
            val dialogBinding = layoutInflater.inflate(R.layout.dialog, null)
            val myDialog = Dialog(this)
            myDialog.setContentView(dialogBinding)
            myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDialog.findViewById<Button>(R.id.yesButton).setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDialog.findViewById<Button>(R.id.noButton).setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDialog.show()
            val yesButton: Button = dialogBinding.findViewById(R.id.yesButton)
            val noButton: Button = dialogBinding.findViewById(R.id.noButton)
            noButton.setOnClickListener {
                myDialog.dismiss()
            }
            yesButton.setOnClickListener {
                try {
                    db.deletePositionById(prodId)
                    Toast.makeText(this, "Товар успешно удален", Toast.LENGTH_SHORT).show()
                    myDialog.dismiss()
                    val intent = Intent(this, Home::class.java)
                    startActivity(intent)
                }
                catch (e: Exception){
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val backButton: ImageView = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}