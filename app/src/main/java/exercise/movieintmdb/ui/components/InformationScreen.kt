package exercise.movieintmdb.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import exercise.movieintmdb.model.Movie
import exercise.movieintmdb.viewmodel.MovieViewModel

@Composable
fun InformationScreen(
    viewModel: MovieViewModel,
    movie: Movie,
    onFavoriteClick: (Movie, Boolean) -> Unit
) {
    val errorMessage by viewModel.error.collectAsState()
    val context = LocalContext.current
    val favoriteMovies by viewModel.favoriteMovies.collectAsState()
    val isFavorite by remember {
        derivedStateOf {
            favoriteMovies.any { it.id == movie.id }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = movie.title,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 40.sp,
                modifier = Modifier.padding(top = 20.dp)
            )

            Spacer(Modifier.height(20.dp))

            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500/${movie.posterPath}",
                contentDescription = null,
                alignment = Alignment.Center
            )

            Spacer(Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = movie.overview,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp
                )
            }

            Spacer(Modifier.height(20.dp))

            IconButton(
                onClick = { onFavoriteClick(movie, !isFavorite) },
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(bottom = 10.dp)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    modifier = Modifier.size(40.dp)
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