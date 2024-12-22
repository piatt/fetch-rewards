package com.example.fetch.rewards.domain

import com.example.fetch.rewards.data.ProductItemResponse
import kotlinx.serialization.Serializable

@Serializable
data class ProductItem(val id: Long, val listId: Long, val name: String)

fun ProductItemResponse.toProductItem(): ProductItem {
    return ProductItem(id, listId, name ?: "")
}