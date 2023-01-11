package com.example.shifttestingproject.data.api

import com.example.shifttestingproject.model.HistoryItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("{bin}")
    suspend fun getBinData(@Path("bin") bin: Int): Response<HistoryItem>
}