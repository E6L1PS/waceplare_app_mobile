package com.itacademy.common.model

data class User(
    val favoriteAds: List<Any>,
    val id: Int,
    val name: String,
    val number: String,
    val rating: Int
)