package exercise.movieintmdb.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import exercise.movieintmdb.ui.components.InformationScreen
import exercise.movieintmdb.ui.components.LoginScreen
import exercise.movieintmdb.ui.components.MovieScreen
import exercise.movieintmdb.ui.components.WebViewScreen
import exercise.movieintmdb.ui.theme.MovieInTMDBTheme
import exercise.movieintmdb.viewmodel.MovieViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val movieViewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieInTMDBTheme {
                Surface {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "authentic"){
                        composable("authentic") {
                            val sessionState by movieViewModel.sessionState.collectAsState()
                            LoginScreen(
                                onLoginClick = {
                                    if (sessionState == null) navController.navigate("webView")
                                    else navController.navigate("movieList"){
                                        popUpTo("authentic") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("webView") {
                            Log.d("MainActivity", "Login button clicked")
                            movieViewModel.authenticateUser()
                            movieViewModel.authUrl?.let { url ->
                                WebViewScreen(url) { token ->
                                    movieViewModel.completeAuthorization(applicationContext, token)
                                    navController.navigate("movieList") {
                                        popUpTo("authentic") { inclusive = true }
                                    }
                                }
                            }
                        }
                        composable("movieList") {
                            val movieList by movieViewModel.movies.collectAsState()
                            val favoriteMovies by movieViewModel.favoriteMovies.collectAsState()
                            MovieScreen(
                                movieList = movieList,
                                onMovieClick = { movie ->
                                    movieViewModel.onMovieClicked(navController,movie.id) },
                                onFavoriteClick = { movie, isFavorite ->
                                    movieViewModel.onFavoriteClick(movie, isFavorite) },
                                favoriteMovies = favoriteMovies
                            )
                        }

                        composable("detail/{movieId}"){ navBackStackEntry ->
                            val movieId = navBackStackEntry.arguments?.getString("movieId")?.toIntOrNull()
                            movieId?.let { id ->
                                val movie = movieViewModel.getMovieById(id)
                                val isFavorite = movieViewModel.isFavorite(id)
                                movie?.let {
                                    InformationScreen(
                                        movie = it,
                                        isFavorite = isFavorite,
                                        onFavoriteClick = { movie, isFavorite ->
                                            movieViewModel.onFavoriteClick(movie, isFavorite)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

