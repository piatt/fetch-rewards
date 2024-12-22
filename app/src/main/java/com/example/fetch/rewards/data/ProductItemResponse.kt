package com.example.fetch.rewards.data

import kotlinx.serialization.Serializable

@Serializable
data class ProductItemResponse(val id: Long, val listId: Long, val name: String?)