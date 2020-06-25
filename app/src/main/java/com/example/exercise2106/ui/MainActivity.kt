package com.example.exercise2106.ui

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.exercise2106.R
import com.example.exercise2106.common.Contacts
import com.example.exercise2106.database.DatabaseManager
import com.example.exercise2106.model.Product
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var adapter: ProductAdapter? = null
    private var mMediaPayer = MediaPlayer()
    private var mProductList: MutableList<Product>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbarMain)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        mProductList = mutableListOf()
        adapter = ProductAdapter()
        rcProduct.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rcProduct.adapter = adapter
        getListProduct()
        fabAddProduct.setOnClickListener {
            val intent = Intent(this@MainActivity, AddProductActivity::class.java)
            startActivityForResult(intent, Contacts.REQUEST_CODE_ADD)
        }
        val afd = assets.openFd("bac_phan.mp3")
        mMediaPayer.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        mMediaPayer.prepare()
        adapter?.setOnClickItemListener(object : ProductAdapter.OnClickItemListener {
            override fun onClickItem(position: Int) {
                mMediaPayer.start()
                showDialogListener(mProductList!![position])
            }
        })
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                if (mProductList != null && position < mProductList!!.size) {
                    val product = mProductList!![position]
                    DatabaseManager.getInstance(this@MainActivity).delete(product.id.toLong())
                    getListProduct()
                }
            }
        }).attachToRecyclerView(rcProduct)
    }

    private fun getListProduct() {
        mProductList =
            DatabaseManager.getInstance(this@MainActivity).getAllProduct() as MutableList<Product>
        adapter?.addListProduct(mProductList)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Contacts.REQUEST_CODE_ADD && resultCode == Activity.RESULT_OK && data != null) {
            val product = data.getParcelableExtra<Product>(Contacts.PRODUCT_KEY)
            if (product != null) {
                val id = DatabaseManager.getInstance(this@MainActivity).insertProduct(product)
                val productNew = DatabaseManager.getInstance(this@MainActivity).getProductByID(id)
                if (productNew != null)
                    adapter?.addProduct(product)
            }
        }
    }

    fun showDialogListener(product: Product) {
        val dialog = Dialog(this@MainActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_update_product)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        val edtName = dialog.findViewById<EditText>(R.id.edtNameUpdate)
        val edtKilo = dialog.findViewById<EditText>(R.id.edtKiloUpdate)
        val edtPrice = dialog.findViewById<EditText>(R.id.edtPriceUpdate)
        val edtAddress = dialog.findViewById<EditText>(R.id.edtAddressUpdate)
        val btnUpdate = dialog.findViewById<Button>(R.id.btnSubmitUpdate)
        btnUpdate.setOnClickListener {
            product.name = edtName.text.toString()
            product.kilogram = edtKilo.text.toString().toFloat()
            product.price = edtPrice.text.toString().toLong()
            product.address = edtAddress.text.toString()
            DatabaseManager.getInstance(this).update(product)
            getListProduct()
            dialog.dismiss()
            if (mMediaPayer.isPlaying) {
                mMediaPayer.pause()
            }
        }
        dialog.show()
    }

}
