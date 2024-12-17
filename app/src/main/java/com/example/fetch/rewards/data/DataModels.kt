package com.example.fetch.rewards.data

data class ProductItemResponse(val id: Long, val listId: Long, val name: String?)

data class ProductItem(val id: Long, val listId: Long, val name: String)

fun ProductItemResponse.toProductItem(): ProductItem {
    return ProductItem(id, listId, name ?: "")
}