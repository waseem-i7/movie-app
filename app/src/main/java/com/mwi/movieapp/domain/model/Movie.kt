package com.mwi.movieapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey
    val id: Int,
    val overview: String,
    val poster_path: String,
    val title: String
)