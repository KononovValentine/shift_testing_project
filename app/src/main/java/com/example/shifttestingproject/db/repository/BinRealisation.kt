package com.example.shifttestingproject.db.repository

import androidx.lifecycle.LiveData
import com.example.shifttestingproject.db.BinDbModel
import com.example.shifttestingproject.db.dao.BinDao

class BinRealisation(private val binDao: BinDao) : BinRepository {
    override val allBins: LiveData<List<BinDbModel>>
        get() = binDao.getAllBins()

    override suspend fun insertBin(binDbModel: BinDbModel, onSuccess: () -> Unit) {
        binDao.insert(binDbModel)
        onSuccess()
    }

    override suspend fun deleteBin(binDbModel: BinDbModel, onSuccess: () -> Unit) {
        binDao.delete(binDbModel)
        onSuccess()
    }
}