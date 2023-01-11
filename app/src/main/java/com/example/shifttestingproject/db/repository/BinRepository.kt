package com.example.shifttestingproject.db.repository

import androidx.lifecycle.LiveData
import com.example.shifttestingproject.db.BinDbModel

interface BinRepository {
    val allBins: LiveData<List<BinDbModel>>

    // Добавляет в базу 1 элемент
    suspend fun insertBin(binDbModel: BinDbModel, onSuccess: () -> Unit)

    // Удаляет из базы 1 элемент
    suspend fun deleteBin(binDbModel: BinDbModel, onSuccess: () -> Unit)
}