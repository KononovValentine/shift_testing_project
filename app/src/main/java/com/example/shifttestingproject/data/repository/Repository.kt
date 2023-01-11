package com.example.shifttestingproject.data.repository

import com.example.shifttestingproject.data.api.RetrofitInstance
import com.example.shifttestingproject.model.HistoryItem
import retrofit2.Response

class Repository {
    suspend fun getBin(bin: Int): Response<HistoryItem> {
        return RetrofitInstance.api.getBinData(bin)
    }
}