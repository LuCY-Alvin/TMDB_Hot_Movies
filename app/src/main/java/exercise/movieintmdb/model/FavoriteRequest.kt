package exercise.movieintmdb.model

data class FavoriteRequest(
    val media_type: String = "movie",
    val media_id: Int,
    val favorite: Boolean
)