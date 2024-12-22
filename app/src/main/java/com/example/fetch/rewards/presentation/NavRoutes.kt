package com.example.fetch.rewards.presentation

import kotlinx.serialization.Serializable

@Serializable
data object ProductList

@Serializable
data class ProductDetail(val id: Long)