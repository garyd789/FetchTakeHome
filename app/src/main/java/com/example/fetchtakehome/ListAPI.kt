package com.example.fetchtakehome
import retrofit2.http.GET

interface ListAPI {
    @GET("hiring.json")
    suspend fun fetchItems(): List<Item>
}