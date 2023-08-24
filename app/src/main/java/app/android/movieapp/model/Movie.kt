package app.android.movieapp.model

import java.io.Serializable

data class Movie(
                 val id: Int,
                 val title: String,
                 val release_date: String,
                 val poster_path: String,
                 val overview: String,
                 val vote_average: Double) : Serializable
