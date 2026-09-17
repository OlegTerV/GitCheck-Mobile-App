package com.example.gitcheckmobileapp.data.model

sealed class DataSource{
    object Db: DataSource()
    object Api: DataSource()
}

sealed interface Resource<out T> {
    data class Success<out T> (
        val data: T,
        val source: DataSource = DataSource.Api
    ): Resource<T>
    data class Error(val error: Throwable?, val message: String): Resource<Nothing>
}