package com.example.gitcheckmobileapp.data.model

sealed interface Resource<out T> {
    data class Success<out T> (val apiData: T): Resource<T>
    data class Error(val error: Throwable?, val message: String): Resource<Nothing>
}