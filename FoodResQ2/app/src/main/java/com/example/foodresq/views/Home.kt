package com.example.foodresq.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodresq.R
import com.example.foodresq.adaptersEtc.ProductAdapter
import com.example.foodresq.adaptersEtc.RestaurantAdapter
import com.example.foodresq.adaptersEtc.SessionManager
import com.example.foodresq.classes.DbHelper
import com.example.foodresq.classes.Product
import com.example.foodresq.classes.Restaurant

class Home : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val foodList = findViewById<RecyclerView>(R.id.upperList)
        val editText: EditText = findViewById(R.id.search)
        editText.clearFocus()
        val db = DbHelper(this, null)
        val sessionManager = SessionManager(this)
        val userEmail = sessionManager.getUserEmail()
        val current = userEmail?.let { db.getUserByEmail(it) }
        val userId = current?.let { db.getUserIdByEmail(it.email) }
        val foods = arrayListOf<Product>()
//        val dbFile = this.getDatabasePath("app")
//        if (dbFile.exists()) {
//            dbFile.delete()
//        }


//        foods.add(Product(0, "Pizza", """Lorem ipsum dolor sit amet, consectetur adipiscing
//            |elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam,
//            |quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor
//            |in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident,
//            |sunt in culpa qui officia deserunt mollit anim id est laborum.""".trimMargin(), "pizza",2500, 0))
//        foods.add(Product(1, "Burger", """Lorem ipsum dolor sit amet, consectetur adipiscing
//            |elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam,
//            |quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.""".trimMargin(), "burger",1500, 1))
//        foods.add(Product(2, "Spaghetti", """Lorem ipsum dolor sit amet, consectetur adipiscing
//            |elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.""".trimMargin(), "spg",3500, 2))


//        db.addPosition(
//            Product(0, "Pizza", """Lorem ipsum dolor sit amet, consectetur adipiscing
//            |elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam,
//            |quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor
//            |in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident,
//            |sunt in culpa qui officia deserunt mollit anim id est laborum.""".trimMargin(), "pizza",2500, 0)
//        )
//        db.addPosition(
//            Product(1, "Burger", """Lorem ipsum dolor sit amet, consectetur adipiscing
//            |elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam,
//            |quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.""".trimMargin(), "burger",1500, 1)
//        )
//        db.addPosition(
//            Product(2, "Spaghetti", """Lorem ipsum dolor sit amet, consectetur adipiscing
//            |elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.""".trimMargin(), "spg",3500, 2)
//        )

        foodList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter1 = ProductAdapter(db.getAllPositions(), this)
        foodList.adapter = adapter1

        adapter1.setOnAddToCartClickListener(object: ProductAdapter.AddToCartClickListener {
            override fun addToCart(id: Int) {
                if (userId != null) {
                    db.addToCart(userId, id)
                    Toast.makeText(this@Home, "Added to cart", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@Home, "User not found", Toast.LENGTH_SHORT).show()
                }
            }
        })
        adapter1.setOnItemClickListener(object: ProductAdapter.OnItemClickListener {
            override fun onItemClick(id: Int) {
                val intent = Intent(this@Home, DetailedActivityFood::class.java)
                intent.putExtra("prodId", id)
                intent.putExtra("product", db.getPosition(id)?.image)
                intent.putExtra("name", db.getPosition(id)?.name)
                intent.putExtra("price", db.getPosition(id)?.price)
                intent.putExtra("desc", db.getPosition(id)?.desc)
                intent.putExtra("rest", db.getPosition(id)?.restId?.let { db.getRest(it) }?.logo)
                intent.putExtra("restId", db.getPosition(id)?.restId?.let { db.getRest(it) }?.id)
                startActivity(intent)
            }
        })



        val restList = findViewById<RecyclerView>(R.id.bottomList)
        val rests = arrayListOf<Restaurant>()

//        db.addRest(
//            Restaurant(0, "Palermo", """Lorem ipsum dolor sit amet, consectetur adipiscing
//            |elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.""".trimMargin(), "palermo")
//        )
//        db.addRest(
//            Restaurant(1, "Turandot", """Lorem ipsum dolor sit amet, consectetur adipiscing
//            |elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.""".trimMargin(), "turandot")
//        )
//        db.addRest(
//            Restaurant(2, "Hani", """Lorem ipsum dolor sit amet, consectetur adipiscing
//            |elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.""".trimMargin(), "hani")
//        )

        restList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        var adapter = RestaurantAdapter(db.getAllRests(),this)
        restList.adapter = adapter
        adapter.setOnItemClickListener(object : RestaurantAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(this@Home, DetailedActivityRestaurants::class.java)
                intent.putExtra("restaurant", db.getRest(position)?.logo)
                intent.putExtra("restName", db.getRest(position)?.name)
                intent.putExtra("restDesc", db.getRest(position)?.desc)
                intent.putExtra("id", db.getRest(position)?.id)
                startActivity(intent)
            }
        })

        val navUser: ImageView = findViewById(R.id.navUser)
        navUser.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}