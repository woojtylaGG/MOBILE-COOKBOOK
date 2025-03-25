package com.example.mobilecookbook

data class Recipe(
    val name: String,
    val description: String,
    var rating: Float = 0f
)