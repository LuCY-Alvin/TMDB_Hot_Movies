package exercise.movieintmdb.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import exercise.movieintmdb.model.Movie
import exercise.movieintmdb.viewmodel.MovieViewModel

@Composable
fun InformationScreen(
    viewModel: MovieViewModel,
    movie: Movie,
    isFavorite: Boolean,
    onFavoriteClick: (Movie, Boolean) -> Unit
) {
    val errorMessage by viewModel.error.collectAsState()
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500/${movie.posterPath}",
                contentDescription = null
            )
            Text(text = movie.title, color = MaterialTheme.colorScheme.primary)
            Text(text = movie.overview, color = MaterialTheme.colorScheme.primary)

            IconButton(onClick = { onFavoriteClick(movie, !isFavorite) }) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite"
                )
            }
        }

        if (errorMessage != null) {
            ErrorDialog(
                title = errorMessage?.first ?: "",
                message = errorMessage?.second ?: "",
                onDismiss = { viewModel.clearError(context) }
            )
        }
    }
}