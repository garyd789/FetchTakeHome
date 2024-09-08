package com.example.fetchtakehome

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Item(
    @Json(name = "id")
    val id: Int,
    @Json(name = "listId")
    val listId: Int,
    @Json(name = "name")
    val name: String?,
)

