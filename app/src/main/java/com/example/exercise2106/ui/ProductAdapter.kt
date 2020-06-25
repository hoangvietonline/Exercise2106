package com.example.exercise2106.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exercise2106.R
import com.example.exercise2106.common.Contacts
import com.example.exercise2106.model.Product
import kotlinx.android.synthetic.main.item_product.view.*

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    private var mProducts = mutableListOf<Product>()
    private var mListener: OnClickItemListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mProducts.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = mProducts[position]
        holder.name.text = product.name
        holder.kilo.text = product.kilogram.toString() + Contacts.KILOGRAM
        holder.price.text = product.price.toString() + Contacts.PRICE
        holder.address.text = product.address
        holder.itemView.setOnClickListener {
            mListener?.onClickItem(position)
        }
    }

    fun addProduct(product: Product) {
        mProducts.add(product)
        notifyDataSetChanged()
    }

    fun removeProduct(product: Product) {
        mProducts.remove(product)
        notifyDataSetChanged()
    }

    fun setOnClickItemListener(listener: OnClickItemListener) {
        mListener = listener
    }

    fun addListProduct(products: MutableList<Product>?) {
        if (products == null) return
        if (mProducts.size > 0)
            mProducts.clear()
        mProducts = products
        notifyDataSetChanged()
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.tvNameItem!!
        val kilo = itemView.tvKiloItem!!
        val price = itemView.tvPriceItem!!
        val address = itemView.tvAddressItem!!
    }

    interface OnClickItemListener {
        fun onClickItem(position: Int)
    }
}
