package com.example.fetch.rewards.data

import kotlinx.serialization.Serializable

@Serializable
data class ProductItemResponse(val id: Long, val listId: Long, val name: String?)

@Serializable
data class ProductItem(val id: Long, val listId: Long, val name: String)

fun ProductItemResponse.toProductItem(): ProductItem {
    return ProductItem(id, listId, name ?: "")
}

sealed class Resource<out T : Any> {
    class Success<T: Any>(val data: T) : Resource<T>()
    class Error<T: Any>(val code: Int, val message: String?) : Resource<T>()
    class Exception<T : Any>(val e: Throwable) : Resource<T>()
}