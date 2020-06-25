package com.example.exercise2106.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.exercise2106.model.Product
import java.util.*

class DatabaseManager private constructor(context: Context) {
    private var databaseHelper: DatabaseHelper = DatabaseHelper(context)

    companion object {
        private var INSTANCE: DatabaseManager? = null
        fun getInstance(context: Context): DatabaseManager {
            if (INSTANCE == null)
                INSTANCE = DatabaseManager(context)
            return INSTANCE!!
        }
    }

    fun insertProduct(product: Product): Long {
        val database = databaseHelper.writableDatabase
        val contentValue = ContentValues()
        contentValue.put(Product.COLUMN_NAME, product.name)
        contentValue.put(Product.COLUMN_KILOGRAM, product.kilogram)
        contentValue.put(Product.COLUMN_PRICE, product.price)
        contentValue.put(Product.COLUMN_ADDRESS, product.address)
        val id = database.insert(Product.TABLE_NAME, null, contentValue)
        database.close()
        return id
    }

    fun update(product: Product): Int {
        val database = databaseHelper.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(Product.COLUMN_NAME, product.name)
        contentValues.put(Product.COLUMN_KILOGRAM, product.kilogram)
        contentValues.put(Product.COLUMN_PRICE, product.price)
        contentValues.put(Product.COLUMN_ADDRESS, product.address)
        val id = database.update(
            Product.TABLE_NAME,
            contentValues,
            Product.COLUMN_ID + " = " + product.id,
            null
        )
        database.close()
        return id
    }

    fun getAllProduct(): List<Product>? {
        val database = databaseHelper.writableDatabase
        val products = ArrayList<Product>()

        // Select All Query
        val selectQuery =
            "SELECT  * FROM " + Product.TABLE_NAME
        val cursor = database?.rawQuery(selectQuery, null)

        // looping through all rows and adding to list
        if (cursor!!.moveToFirst()) {
            do {
                val product = Product()
                product.id = cursor.getInt(cursor.getColumnIndex(Product.COLUMN_ID))
                product.name = cursor.getString(cursor.getColumnIndex(Product.COLUMN_NAME))
                product.price = cursor.getLong(cursor.getColumnIndex(Product.COLUMN_PRICE))
                product.kilogram =
                    cursor.getFloat(cursor.getColumnIndex(Product.COLUMN_KILOGRAM))
                product.address =
                    cursor.getString(cursor.getColumnIndex(Product.COLUMN_ADDRESS))
                products.add(product)
            } while (cursor.moveToNext())
        }
        database.close()
        // return notes list
        return products
    }

    fun getProductByID(id: Long): Product? {
        // get readable database as we are not inserting anything
        val db: SQLiteDatabase = databaseHelper.readableDatabase
        val cursor = db.query(
            Product.TABLE_NAME,
            arrayOf(
                Product.COLUMN_ID,
                Product.COLUMN_NAME,
                Product.COLUMN_KILOGRAM,
                Product.COLUMN_PRICE,
                Product.COLUMN_ADDRESS
            ),
            Product.COLUMN_ID + "=?",
            arrayOf(id.toString()),
            null,
            null,
            null,
            null
        )
        cursor?.moveToFirst()
        // prepare note object
        val product = Product(
            cursor!!.getInt(cursor.getColumnIndex(Product.COLUMN_ID)),
            cursor.getString(cursor.getColumnIndex(Product.COLUMN_NAME)),
            cursor.getFloat(cursor.getColumnIndex(Product.COLUMN_KILOGRAM)),
            cursor.getLong(cursor.getColumnIndex(Product.COLUMN_PRICE)),
            cursor.getString(cursor.getColumnIndex(Product.COLUMN_ADDRESS))
        )
        // close the db connection
        cursor.close()
        return product
    }

    fun delete(id: Long) {
        val database = databaseHelper.writableDatabase
        database.delete(Product.TABLE_NAME, Product.COLUMN_ID + "=" + id, null)
        database.close()
    }
}
