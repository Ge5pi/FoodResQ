package com.example.foodresq.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodresq.R
import com.example.foodresq.adaptersEtc.ProductAdapter
import com.example.foodresq.classes.DbHelper
import com.example.foodresq.classes.Product

class DetailedActivityRestaurants : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_restaurants)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val db = DbHelper(this, null)
        val restLogo: ImageView = findViewById(R.id.restaurant)
        val restName: TextView = findViewById(R.id.restName)
        val restDesc: TextView = findViewById(R.id.restDesc)

        val bundle: Bundle? = intent.extras

        val logoImage = bundle!!.getString("restaurant")
        val name = bundle.getString("restName")
        val desc = bundle.getString("restDesc")
        val randId = bundle.getInt("id")

        restDesc.text = desc
        restName.text = name
        val logoID = resources.getIdentifier(logoImage, "drawable", packageName)
        restLogo.setImageResource(logoID)

        val foodListResources = arrayListOf<Product>()
        for (prod in db.getAllPositions()) {
            if (prod.restId == randId) {
                foodListResources.add(prod)
            }
        }

        val foodList: RecyclerView = findViewById(R.id.foodList)
        foodList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val adapter1 = ProductAdapter(foodListResources, this)
        foodList.adapter = adapter1
        adapter1.setOnItemClickListener(object : ProductAdapter.OnItemClickListener {
            override fun onItemClick(id: Int) {
                val product = db.getPosition(id)
                if (product != null) {
                    val intent = Intent(this@DetailedActivityRestaurants, DetailedActivityFood::class.java)
                    intent.putExtra("prodId", id)
                    intent.putExtra("product", product.image)
                    intent.putExtra("name", product.name)
                    intent.putExtra("price", product.price)
                    intent.putExtra("desc", product.desc)
                    intent.putExtra("rest", product.restId.let { db.getRest(it) }?.logo)
                    intent.putExtra("restId",
                        db.getPosition(id)?.restId?.let { db.getRest(it) }?.id
                    )
                    startActivity(intent)
                }
            }
        })

        val backButton: ImageView = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }
    }
}
