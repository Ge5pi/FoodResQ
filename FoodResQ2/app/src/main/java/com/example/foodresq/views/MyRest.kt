package com.example.foodresq.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodresq.R
import com.example.foodresq.R.id.addPositionButton
import com.example.foodresq.adaptersEtc.ProductAdapter
import com.example.foodresq.adaptersEtc.SessionManager
import com.example.foodresq.classes.DbHelper
import com.example.foodresq.classes.Restaurant

class MyRest : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_rest)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db = DbHelper(this, null)

        val sessionManager = SessionManager(this)
        val userEmail = sessionManager.getUserEmail()
        val current = userEmail?.let { db.getUserByEmail(it) }
        val rest: Restaurant? = current?.let { db.getRest(it.rest_id) }

        val restLogo: ImageView = findViewById(R.id.restLogo)
        val image = rest?.logo
        val imageId = resources.getIdentifier(image, "drawable", packageName)
        restLogo.setImageResource(imageId)

        val restName: TextView = findViewById(R.id.restName)
        val name = rest?.name
        restName.text = name

        val restDesc: TextView = findViewById(R.id.restDesc)
        val desc = rest?.desc
        restDesc.text = desc

        val allFoodList = db.getAllPositions()
        val foodListResources = allFoodList.filter { it.restId == rest?.id }


        val foodList: RecyclerView = findViewById(R.id.foodList)
        foodList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        var adapter1 = ProductAdapter(foodListResources, this)
        foodList.adapter = adapter1
        adapter1.setOnItemClickListener(object: ProductAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
//                Toast.makeText(this@Home, "Number: $position", Toast.LENGTH_LONG).show()
                val intent = Intent(this@MyRest, DetailedActivityFood::class.java)
                intent.putExtra("product", db.getPosition(position)?.image)
                intent.putExtra("name", db.getPosition(position)?.name)
                intent.putExtra("price", db.getPosition(position)?.price)
                intent.putExtra("desc", db.getPosition(position)?.desc)
                intent.putExtra("rest",
                    db.getPosition(position)?.restId?.let { db.getRest(it) }?.logo
                )

                startActivity(intent)
            }
        })
        val addPos: Button = findViewById(addPositionButton)
        addPos.setOnClickListener {
            val intent = Intent(this, AddPosition::class.java)
            startActivity(intent)
        }
    }
}