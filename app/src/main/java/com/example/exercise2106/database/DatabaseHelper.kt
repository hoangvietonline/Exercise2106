package com.example.exercise2106.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.exercise2106.model.Product

class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE)
        onCreate(db)
    }
    companion object{
        const val TABLE_NAME = "notes"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME= "note"
        const val COLUMN_KILOGRAM = "kilogram"
        const val COLUMN_PRICE = "price"
        const val COLUMN_ADDRESS = "address"
        val CREATE_TABLE = ("CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_KILOGRAM + " REAL,"
                + COLUMN_PRICE + " REAL,"
                + COLUMN_ADDRESS + " TEXT"
                + ")")
    }
}

const val DATABASE_VERSION = 1
const val DATABASE_NAME = "products_db"
