package com.example.getnewsapp.dataclass

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)