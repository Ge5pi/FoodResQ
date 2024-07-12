package com.example.foodresq.classes

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(val context: Context, val factory: SQLiteDatabase.CursorFactory?):
    SQLiteOpenHelper(context, "app", factory, 1){
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE IF NOT EXISTS users (id INT PRIMARY KEY, login TEXT, email TEXT, password TEXT, avatar TEXT)")
        db.execSQL("CREATE TABLE IF NOT EXISTS food (id INT PRIMARY KEY, name TEXT, description TEXT, image TEXT, price INT, rest_id INT, user_id INT)")
        db.execSQL("CREATE TABLE IF NOT EXISTS rests (id INT PRIMARY KEY, name TEXT, description TEXT, image TEXT, position INT)")
        db.execSQL("CREATE TABLE IF NOT EXISTS cart (nomer INT PRIMARY KEY, user_id INT, position_id INT)")
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS food")
        db.execSQL("DROP TABLE IF EXISTS rests")
        db.execSQL("DROP TABLE IF EXISTS cart")
        onCreate(db)
    }

    fun addUser(user: User) {
        val values = ContentValues().apply {
            put("login", user.login)
            put("email", user.email)
            put("password", user.password)
            put("avatar", user.avatar)
        }
        val db = this.writableDatabase
        db.insert("users", null, values)
        db.close()
    }

    fun getUser(email: String, password: String): Boolean {
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM users WHERE email = '$email' AND password = '$password'", null)
        val exists = result.moveToFirst()
        result.close()
        db.close()
        return exists
    }

    fun getUserIdByEmail(email: String): Int?{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE email = '$email'", null)
        if(cursor.moveToFirst()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            cursor.close()
            db.close()
            println("User: $id")
            return id
        }
        else{
            println("NOOOOOOOO")
            cursor.close()
            db.close()
            return null
        }
    }

    fun getUserByEmail(email: String): User? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE email = '$email'", null)

        if (cursor.moveToFirst()) {

            val login = cursor.getString(cursor.getColumnIndexOrThrow("login"))
            val avatar = cursor.getString(cursor.getColumnIndexOrThrow("avatar"))
            val password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
            cursor.close()
            db.close()
            return User(login, avatar, password)
        } else {
            cursor.close()
            db.close()
            return null
        }
    }

    fun getUserLogin(login: String): Boolean {
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM users WHERE login = '$login'", null)
        val exists = result.moveToFirst()
        result.close()
        db.close()
        return exists
    }

    fun getUserEmail(email: String): Boolean {
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM users WHERE email = '$email'", null)
        val exists = result.moveToFirst()
        result.close()
        db.close()
        return exists
    }

    fun updateUser(user: User, login: String, email: String) {
        val values = ContentValues().apply {
            put("login", login)
            put("email", email)
            put("password", user.password)
            put("avatar", user.avatar)
        }
        val db = this.writableDatabase
        db.update("users", values, "login = ?", arrayOf(user.login))
        db.close()
    }


    fun addPosition(product: Product){
        val values = ContentValues()
        values.put("id", product.id)
        values.put("name", product.name)
        values.put("description", product.desc)
        values.put("image", product.image)
        values.put("price", product.price)
        values.put("rest_id", product.restId)
        values.put("user_id", product.userId)

        val db = this.writableDatabase
        db.insert("food", null, values)
        db.close()
    }

    fun getPosition(id: Int): Product? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM food WHERE id = '$id'", null)

        if (cursor.moveToFirst()) {
            val productId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
            val image = cursor.getString(cursor.getColumnIndexOrThrow("image"))
            val price = cursor.getInt(cursor.getColumnIndexOrThrow("price"))
            val restId = cursor.getInt(cursor.getColumnIndexOrThrow("rest_id"))
            val userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"))

            cursor.close()
            db.close()

            return Product(productId, name, description, image, price, restId, userId)
        } else {
            cursor.close()
            db.close()
            return null
        }
    }

    fun getPositionByName(name: String): Product? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM food WHERE name = '$name'", null)

        if (cursor.moveToFirst()) {
            val productId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
            val image = cursor.getString(cursor.getColumnIndexOrThrow("image"))
            val price = cursor.getInt(cursor.getColumnIndexOrThrow("price"))
            val restId = cursor.getInt(cursor.getColumnIndexOrThrow("rest_id"))
            val userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"))

            cursor.close()
            db.close()

            return Product(productId, name, description, image, price, restId, userId)
        } else {
            cursor.close()
            db.close()
            return null
        }
    }

    fun getAllPositions(): List<Product> {
        val products = mutableListOf<Product>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM food", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
                val image = cursor.getString(cursor.getColumnIndexOrThrow("image"))
                val price = cursor.getInt(cursor.getColumnIndexOrThrow("price"))
                val restId = cursor.getInt(cursor.getColumnIndexOrThrow("rest_id"))
                val userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"))

                val product = Product(id, name, description, image, price, restId, userId)
                products.add(product)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return products
    }

    fun getRest(id: Int): Restaurant? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM rests WHERE id = '$id'", null)

        if (cursor.moveToFirst()) {
            val restId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
            val logo = cursor.getString(cursor.getColumnIndexOrThrow("image"))
            val position = cursor.getInt(cursor.getColumnIndexOrThrow("position"))

            cursor.close()
            db.close()

            return Restaurant(restId, name, description, logo, position)
        } else {
            cursor.close()
            db.close()
            return null
        }
    }

    fun getRestByName(name: String): Restaurant?{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT *FROM rests WHERE name='$name'", null)

        if(cursor.moveToFirst()){
            val restId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val name = name
            val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
            val logo = cursor.getString(cursor.getColumnIndexOrThrow("image"))
            val position = cursor.getInt(cursor.getColumnIndexOrThrow("position"))
            cursor.close()
            db.close()

            return Restaurant(restId, name, description, logo, position)
        }
        else{
            cursor.close()
            db.close()
            return null
        }
    }

    fun getAllRests(): List<Restaurant> {
        val rests = mutableListOf<Restaurant>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM rests", null)

        if (cursor.moveToFirst()) {
            do {
                val restId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
                val logo = cursor.getString(cursor.getColumnIndexOrThrow("image"))
                val position = cursor.getInt(cursor.getColumnIndexOrThrow("position"))

                val product = Restaurant(restId, name, description, logo, position)
                rests.add(product)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return rests
    }

    fun addRest(rest: Restaurant){
        val values = ContentValues()
        values.put("id", rest.id)
        values.put("name", rest.name)
        values.put("description", rest.desc)
        values.put("image", rest.logo)
        values.put("position", rest.position)
        val db = this.writableDatabase
        db.insert("rests", null, values)
        db.close()
    }


    fun deletePositionById(id: Int){
        val db = this.writableDatabase
        db.execSQL("DELETE FROM food WHERE id = '$id'")
        db.close()
    }


    fun addToCart(userId: Int, positionId: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("user_id", userId)
            put("position_id", positionId)
        }
        db.insert("cart", null, values)
        db.close()
    }


    fun deleteFromCart(positionId: Int){
        val db = this.writableDatabase
        db.execSQL("DELETE FROM cart WHERE position_id = '$positionId'")
        db.close()
    }

    fun readCart(userId: Int): List<Product>{
        val db = this.readableDatabase
        var cartList = mutableListOf<Product>()
        val cursor = db.rawQuery("SELECT * FROM cart WHERE user_id='$userId'", null)
        if(cursor.moveToFirst()) {
            do {
                val position = getPosition(cursor.getInt(cursor.getColumnIndexOrThrow("position_id")))
                position?.let { cartList.add(it)}
            }
                while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return cartList
    }

}