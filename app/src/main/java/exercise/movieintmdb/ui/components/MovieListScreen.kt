package exercise.movieintmdb.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import exercise.movieintmdb.model.Movie

@Composable
fun MovieScreen(
    movieList: List<Movie>,
    onMovieClick: (Movie) -> Unit,
    onFavoriteClick: (Movie, Boolean) -> Unit,
    favoriteMovies: List<Movie>
    ) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Hot Movies")
            LazyColumn {
                items(movieList) { movie ->
                    val isFavorite = favoriteMovies.any { it.id == movie.id }
                    MovieItem(
                        movie = movie,
                        isFavorite = isFavorite,
                        onClick = { onMovieClick(movie) },
                        onFavoriteClick = { onFavoriteClick(movie, !isFavorite) }
                    )
                }
            }
        }
    }
}

@Composable
fun MovieItem(
    movie: Movie,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Row (
        modifier = Modifier
            .padding(16.dp)
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500/${movie.posterPath}",
            contentDescription = null
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = movie.title,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 18.sp,
                modifier = Modifier.padding(10.dp),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
            IconButton(onClick = { onFavoriteClick() }) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite"
                )
            }
        }
    }
}
