package com.example.shifttestingproject.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bin_table")
data class BinDbModel(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo var city: String,
    @ColumnInfo var bankName: String,
    @ColumnInfo var phone: String,
    @ColumnInfo var url: String,
    @ColumnInfo var brand: String,
    @ColumnInfo var alpha2: String,
    @ColumnInfo var currency: String,
    @ColumnInfo var emoji: String,
    @ColumnInfo var latitude: String,
    @ColumnInfo var longitude: String,
    @ColumnInfo var countryName: String,
    @ColumnInfo var numeric: String,
    @ColumnInfo var length: String,
    @ColumnInfo var luhn: String,
    @ColumnInfo var prepaid: String,
    @ColumnInfo var scheme: String,
    @ColumnInfo var type: String
)