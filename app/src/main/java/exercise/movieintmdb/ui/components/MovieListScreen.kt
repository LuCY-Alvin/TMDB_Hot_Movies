package exercise.movieintmdb.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import exercise.movieintmdb.model.Movie
import exercise.movieintmdb.viewmodel.MovieViewModel

@Composable
fun MovieScreen(
    viewModel: MovieViewModel,
    movieList: List<Movie>,
    onMovieClick: (Movie) -> Unit,
    onFavoriteClick: (Movie, Boolean) -> Unit,
    favoriteMovies: List<Movie>
) {
    val errorMessage by viewModel.error.collectAsState()
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Hot Movies",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 45.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(16.dp).background(MaterialTheme.colorScheme.primary),
                )
            LazyRow {
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

        if (errorMessage != null) {
            ErrorDialog(
                title = errorMessage?.first ?: "",
                message = errorMessage?.second ?: "",
                onDismiss = { viewModel.clearError(context) }
            )
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
    Column (
        modifier = Modifier
            .padding(16.dp)
            .width(200.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = movie.title,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 18.sp,
            modifier = Modifier.fillMaxWidth().heightIn(min = 50.dp).padding(bottom = 8.dp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 2
        )

        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500/${movie.posterPath}",
            contentDescription = null,
            modifier = Modifier
                    .height(250.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )

        IconButton(
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
            onClick = { onFavoriteClick() }
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favorite"
            )
        }
    }
}
