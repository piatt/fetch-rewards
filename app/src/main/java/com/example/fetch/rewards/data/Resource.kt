package com.example.fetch.rewards.data

/**
 * Wrapper class representing the result of the fetch
 * of a resource from a data source, for example a network request.
 * Consumers can apply situational logic, depending on the subclass received.
 */
sealed class Resource<out T : Any> {
    data class Success<T: Any>(val data: T) : Resource<T>()
    data class Error<T: Any>(val code: Int, val message: String?) : Resource<T>()
    data class Exception<T : Any>(val e: Throwable) : Resource<T>()
}