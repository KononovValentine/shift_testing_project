package com.example.shifttestingproject.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.shifttestingproject.db.BinDbModel

@Dao
interface BinDao {

    // Добавляет 1 запись в базу, игнорирует при повторах
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(binDbModel: BinDbModel)

    // Удаляет 1 запись из базы
    @Delete
    suspend fun delete(binDbModel: BinDbModel)

    // Отдает все записи из базы
    @Query("SELECT * from bin_table")
    fun getAllBins(): LiveData<List<BinDbModel>>
}