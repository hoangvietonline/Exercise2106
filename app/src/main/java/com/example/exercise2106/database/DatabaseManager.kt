package com.example.exercise2106.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.exercise2106.model.Product
import java.util.*

class DatabaseManager( context: Context) {
    private var databaseHelper = DatabaseHelper(context)
    private lateinit var  database : SQLiteDatabase

    fun insertProduct(product: Product): Long {
        val database = databaseHelper.writableDatabase
        val contentValue = ContentValues()
        contentValue.put(DatabaseHelper.COLUMN_NAME, product.name)
        contentValue.put(DatabaseHelper.COLUMN_KILOGRAM, product.kilogram)
        contentValue.put(DatabaseHelper.COLUMN_PRICE, product.price)
        contentValue.put(DatabaseHelper.COLUMN_ADDRESS, product.address)
        return database.insert(DatabaseHelper.TABLE_NAME, null, contentValue)
    }

    fun update(product: Product): Int {
        database = databaseHelper.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DatabaseHelper.COLUMN_NAME, product.name)
        contentValues.put(DatabaseHelper.COLUMN_KILOGRAM, product.kilogram)
        contentValues.put(DatabaseHelper.COLUMN_PRICE, product.price)
        contentValues.put(DatabaseHelper.COLUMN_ADDRESS, product.address)
        return database.update(
            DatabaseHelper.TABLE_NAME,
            contentValues,
            DatabaseHelper.COLUMN_ID + " = " + product.id,
            null
        )
    }

    fun getAllProduct(): List<Product>? {
        database = databaseHelper.writableDatabase
        val products = ArrayList<Product>()

        // Select All Query
        val selectQuery =
            "SELECT  * FROM " + DatabaseHelper.TABLE_NAME
        val cursor = database?.rawQuery(selectQuery, null)

        // looping through all rows and adding to list
        if (cursor!!.moveToFirst()) {
            do {
                val product = Product()
                product.id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID))
                product.name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME))
                product.price = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRICE))
                product.kilogram =
                    cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.COLUMN_KILOGRAM))
                product.address =
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ADDRESS))
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
            DatabaseHelper.TABLE_NAME,
            arrayOf(DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_KILOGRAM,
                DatabaseHelper.COLUMN_PRICE,DatabaseHelper.COLUMN_ADDRESS),
            DatabaseHelper.COLUMN_ID + "=?",
            arrayOf(id.toString()),
            null,
            null,
            null,
            null
        )
        cursor?.moveToFirst()

        // prepare note object
        val product = Product(
            cursor!!.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)),
            cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)),
            cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.COLUMN_KILOGRAM)),
            cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRICE)),
            cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ADDRESS))
        )

        // close the db connection
        cursor.close()
        return product
    }

    fun delete(_id: Long) {
        database = databaseHelper?.writableDatabase
        database!!.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.COLUMN_ID + "=" + _id, null)
    }
}
