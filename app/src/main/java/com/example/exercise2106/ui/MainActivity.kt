package com.example.exercise2106.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.exercise2106.R
import com.example.exercise2106.RecyclerTouchListener
import com.example.exercise2106.common.Contacts
import com.example.exercise2106.database.DatabaseManager
import com.example.exercise2106.model.Product
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var adapter: ProductAdapter? = null
    private val databaseManager = DatabaseManager(this)
    private var mMediaPayer = MediaPlayer()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbarMain)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        adapter = ProductAdapter()
        rcProduct.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rcProduct.adapter = adapter
        getListProduct()
        fabAddProduct.setOnClickListener {
            val intent = Intent(this@MainActivity, AddProductActivity::class.java)
            startActivityForResult(intent, Contacts.REQUEST_CODE_ADD)
        }
        val afd= assets.openFd("bac_phan.mp3")
        mMediaPayer.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        mMediaPayer.prepare()

        rcProduct.addOnItemTouchListener(
            RecyclerTouchListener(this,
                rcProduct, object : RecyclerTouchListener.ClickListener {
                    override fun onClick(view: View?, position: Int) {}
                    override fun onLongClick(view: View?, position: Int) {
                       showDialogListener(position)
                    }
                })
        )
    }

    private fun getListProduct() {
        adapter?.products = databaseManager.getAllProduct()!! as MutableList<Product>
        adapter?.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Contacts.REQUEST_CODE_ADD && resultCode == Activity.RESULT_OK && data != null) {
            val product = data.getParcelableExtra<Product>(Contacts.PRODUCT_KEY)
            if (product != null) {
                val id = databaseManager.insertProduct(product)
                val productNew = databaseManager.getProductByID(id)
                if (productNew != null)
                    adapter?.addProduct(product)
            }
        }
    }
    fun showDialogListener(id: Int){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Product")
        builder.setMessage("Do you want to delete the product?")

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            databaseManager.delete(id.toLong())
            adapter?.notifyDataSetChanged()
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            dialog.dismiss()
        }

        builder.show()
    }
}
