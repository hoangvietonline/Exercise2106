package com.example.exercise2106.model

import android.os.Parcel
import android.os.Parcelable

data class Product(
    var id: Int = 0,
    var name: String?= null,
    var kilogram: Float = 0F,
    var price: Long = 0L,
    var address: String?= null
) : Parcelable {

    // Create table SQL query

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readFloat(),
        parcel.readLong(),
        parcel.readString()
    )


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeFloat(kilogram)
        parcel.writeLong(price)
        parcel.writeString(address)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
        const val TABLE_NAME = "notes"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "note"
        const val COLUMN_KILOGRAM = "kilogram"
        const val COLUMN_PRICE = "price"
        const val COLUMN_ADDRESS = "address"
        const val CREATE_TABLE = ("CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_KILOGRAM + " REAL,"
                + COLUMN_PRICE + " REAL,"
                + COLUMN_ADDRESS + " TEXT"
                + ")")
    }

}
