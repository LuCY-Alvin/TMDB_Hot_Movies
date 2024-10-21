package exercise.movieintmdb.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import exercise.movieintmdb.model.Movie

@Composable
fun InformationScreen(
    movie: Movie,
    isFavorite: Boolean,
    onFavoriteClick: (Movie, Boolean) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500/${movie.posterPath}",
            contentDescription = null
        )
        Text(text = movie.title, color = Color.Black)
        Text(text = movie.overview, color = Color.Black)

        IconButton(onClick = { onFavoriteClick(movie, !isFavorite) }) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favorite"
            )
        }
    }
}