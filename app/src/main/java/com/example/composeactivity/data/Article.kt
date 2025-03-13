package com.example.composeactivity.data

data class Article(val title: String, val link: String, val date: String)

val articleList = List(20) { i ->
    Article(
        title = "Article ${i + 1} Description ...",
        link = "https://example.com/article-${i + 1}",
        date = "Published on: ${2020 + (i % 5)}-${(i % 12) + 1}-10"
    )
}
