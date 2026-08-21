package com.example.gitcheckmobileapp.data.network.dto

import com.example.gitcheckmobileapp.data.model.Readme
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class ReadmeDTO(
    @SerialName("name")
    val name: String,
    @SerialName("content")
    val content: String,
    @SerialName("encoding")
    val encoding: String
)