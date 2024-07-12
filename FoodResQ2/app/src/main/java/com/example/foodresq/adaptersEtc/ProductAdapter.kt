package com.example.foodresq.adaptersEtc

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodresq.R
import com.example.foodresq.classes.Product

class ProductAdapter(private val foods: List<Product>, private val context: Context) : RecyclerView.Adapter<ProductAdapter.MyViewHolder>() {

    private lateinit var mListener: OnItemClickListener
    private var cListener: AddToCartClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(id: Int)
    }

    interface AddToCartClickListener {
        fun addToCart(id: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    fun setOnAddToCartClickListener(listener: AddToCartClickListener) {
        cListener = listener
    }

    class MyViewHolder(view: View, listener: OnItemClickListener, listenerCart: AddToCartClickListener?) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.productPic)
        val name: TextView = view.findViewById(R.id.productName)
        val price: TextView = view.findViewById(R.id.productPrice)
        val toCartButton: Button = view.findViewById(R.id.toCart)
        var id: Int = 0

        init {
            view.setOnClickListener {
                listener.onItemClick(id)
            }

            toCartButton.setOnClickListener {
                listenerCart?.addToCart(id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_in_list, parent, false)
        return MyViewHolder(view, mListener, cListener)
    }

    override fun getItemCount(): Int {
        return foods.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val product = foods[position]
        holder.name.text = product.name
        holder.price.text = product.price.toString()
        val imageId = context.resources.getIdentifier(product.image, "drawable", context.packageName)
        holder.img.setImageResource(imageId)
        holder.id = product.id
    }
}
