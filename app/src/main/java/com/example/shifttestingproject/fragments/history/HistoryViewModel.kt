package com.example.shifttestingproject.fragments.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.shifttestingproject.REPOSITORY
import com.example.shifttestingproject.db.BinDatabase
import com.example.shifttestingproject.db.BinDbModel
import com.example.shifttestingproject.db.repository.BinRealisation

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    val context = application

    // инициализирует БД
    fun initDatabase() {
        val daoBin = BinDatabase.getInstance(context).getBinDao()
        REPOSITORY = BinRealisation(daoBin)
    }

    // получает все записи из БД
    fun getAllBins(): LiveData<List<BinDbModel>> {
        return REPOSITORY.allBins
    }
}