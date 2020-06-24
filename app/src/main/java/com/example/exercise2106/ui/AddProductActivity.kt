package com.example.exercise2106.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.exercise2106.R
import com.example.exercise2106.common.Contacts
import com.example.exercise2106.model.Product
import kotlinx.android.synthetic.main.activity_add_product.*

class AddProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        setSupportActionBar(toolbarAdd)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        btnSubmit.setOnClickListener {
            val product = setProduct()
            val intent = Intent()
            intent.putExtra(Contacts.PRODUCT_KEY, product)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun setProduct(): Product {
        val product = Product()
        product.name = edtNameAdd.text.toString()
        product.kilogram = edtKiloAdd.text.toString().toFloat()
        product.price = edtPriceAdd.text.toString().toLong()
        product.address = edtAddressAdd.text.toString()
        return product
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home->finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
