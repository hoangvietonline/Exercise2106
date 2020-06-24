package com.example.exercise2106.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exercise2106.R
import com.example.exercise2106.model.Product
import kotlinx.android.synthetic.main.item_product.view.*

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    var products = mutableListOf<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.name.text = product.name
        holder.kilo.text = product.kilogram.toString() + " Kg"
        holder.price.text = product.price.toString() +" VND"
        holder.address.text = product.address
    }
        fun addProduct(product : Product){
            products.add(product)
            notifyDataSetChanged()
        }
    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.tvNameItem!!
        val kilo = itemView.tvKiloItem!!
        val price = itemView.tvPriceItem!!
        val address = itemView.tvAddressItem!!
    }
}
