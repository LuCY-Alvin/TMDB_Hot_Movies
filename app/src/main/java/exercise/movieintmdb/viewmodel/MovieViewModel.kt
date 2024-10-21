package exercise.movieintmdb.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import exercise.movieintmdb.model.Movie
import exercise.movieintmdb.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository
): ViewModel() {

    private val _sessionState = MutableStateFlow<Pair<String?, String?>?>(null)
    val sessionState: StateFlow<Pair<String?, String?>?> = _sessionState

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    private val _favoriteMovies = MutableStateFlow<List<Movie>>(emptyList())
    val favoriteMovies: StateFlow<List<Movie>> = _favoriteMovies

    private var error by mutableStateOf("")

    var authUrl: String? by mutableStateOf(null)

    fun authenticateUser() {
        viewModelScope.launch {
            try {
                val requestToken = repository.getRequestToken()
                if (requestToken != null) {
                    authUrl = "https://www.themoviedb.org/authenticate/$requestToken?redirect_to=myapp://auth"
                } else {
                    error = "無法取得授權Token"
                }
            } catch (e: Exception) {
                error = "授權過程失敗：${e.message}"
            }
        }
    }

    fun completeAuthorization(requestToken: String) {
        viewModelScope.launch {
            try {
                val accountAndSession = repository.getAccountAndSessionID(requestToken)
                _sessionState.value = accountAndSession
                showFavoriteMovies()
                showPopularMovies()
            } catch (e: Exception) {
                error = "授權失敗：${e.message}"
            }
        }
    }

    private fun showPopularMovies() {
        viewModelScope.launch {
            try {
                _movies.value = repository.getPopularMovies()
            } catch (e:Exception) {
                error = "無法取得電影資料：${e.message}"
            }
        }
    }

    private fun showFavoriteMovies() {
        viewModelScope.launch {
            try {
                _favoriteMovies.value = repository.getFavoriteMovies()
            } catch (e:Exception) {
                error = "無法取得喜愛電影資料：${e.message}"
            }
        }
    }

    fun onMovieClicked(navController: NavController, movieId: Int){
        navController.navigate("detail/$movieId")
    }

    fun getMovieById(movieId: Int): Movie? {
        return _movies.value.find { it.id == movieId }
    }

    fun onFavoriteClick(movie: Movie, isFavorite: Boolean){
        updateFavoriteStatus(movie, isFavorite)
        viewModelScope.launch {
            try {
                repository.addAsFavorite(movie.id, isFavorite)
                showFavoriteMovies()
            } catch (e:Exception) {
                error = "無法更新喜愛電影：${e.message}"
            }
        }
    }

    fun isFavorite(movieId: Int): Boolean{
        return _favoriteMovies.value.any { it.id == movieId }
    }

    private fun updateFavoriteStatus(movie: Movie, isFavorite: Boolean) {
        _favoriteMovies.value = if (isFavorite) {
            _favoriteMovies.value + movie
        } else {
            _favoriteMovies.value.filter { it.id != movie.id }
        }
    }
}
