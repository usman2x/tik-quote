package com.example.composeactivity.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface QuoteApi {
    @GET("json/quotes.json")
    suspend fun getQuotes(): QuotesResponse
}

object RetrofitInstance {
    val api: QuoteApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://usman2x.github.io/public-repo/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuoteApi::class.java)
    }
}