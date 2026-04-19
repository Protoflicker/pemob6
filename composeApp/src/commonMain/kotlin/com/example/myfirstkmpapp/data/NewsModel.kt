package com.example.myfirstkmpapp.data

import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)

@Serializable
data class Article(
    val title: String? = null,
    val description: String? = null,
    val urlToImage: String? = null,
    val url: String? = null
)