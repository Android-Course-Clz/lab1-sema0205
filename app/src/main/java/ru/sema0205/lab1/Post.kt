package ru.sema0205.lab1

data class Post(
    val id: Long,
    val authorName: String,
    val authorNickname: String,
    val authorAvatarUrl: String,
    val text: String,
    val imageUrl: String?,
    val likesCount: Int,
    val commentsCount: Int,
    var isLiked: Boolean = false
)