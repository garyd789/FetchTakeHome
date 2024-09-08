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

fun processItems(items: List<Item>): List<Item> {
    return items
        .filterNot { it.name.isNullOrBlank() } // Filter out items with null or blank names
        .groupBy { it.listId } // Group items by listId
        .toSortedMap() // Ensure the map is sorted using listId as key
        .mapValues { entry ->
            entry.value.sortedBy { it.name } // Sort items within each listId group by name
        }
        .flatMap { it.value } // Flatten the SortedMap back into a list
}