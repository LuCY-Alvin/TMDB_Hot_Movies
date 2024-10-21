package exercise.movieintmdb.model

import com.google.gson.annotations.SerializedName

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    @SerializedName("poster_path") val posterPath: String
)

data class MovieResponse(
    val results: List<Movie>
)
