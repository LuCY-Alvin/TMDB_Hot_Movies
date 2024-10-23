package exercise.movieintmdb.viewmodel

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import exercise.movieintmdb.helper.NetworkMonitor
import exercise.movieintmdb.model.Movie
import exercise.movieintmdb.repository.MovieRepository
import exercise.movieintmdb.storage.SessionDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val sessionDataStore: SessionDataStore,
    private val networkMonitor: NetworkMonitor
): ViewModel() {

    private val _sessionState = MutableStateFlow<Pair<String?, String?>?>(null)
    val sessionState: StateFlow<Pair<String?, String?>?> = _sessionState

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    private val _favoriteMovies = MutableStateFlow<List<Movie>>(emptyList())
    val favoriteMovies: StateFlow<List<Movie>> = _favoriteMovies

    private val _error = MutableStateFlow<Pair<String?, String?>?>(null)
    val error: StateFlow<Pair<String?, String?>?> = _error

    var authUrl: String? by mutableStateOf(null)

    init {
        viewModelScope.launch {
            val (sessionId, accountId) = sessionDataStore.getSessionData()
            if (sessionId != null && accountId != null) {
                _sessionState.value = Pair(sessionId, accountId)
                showFavoriteMovies()
                showPopularMovies()
            }
        }
    }

    private fun openNetworkSettings(context: Context) {
        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
        context.startActivity(intent)
    }

    fun authenticateUser() {
        viewModelScope.launch {
            try {
                val requestToken = repository.getRequestToken()
                if (requestToken != null) {
                    authUrl = "https://www.themoviedb.org/authenticate/$requestToken?redirect_to=myapp://auth"
                } else {
                    _error.value =  "無法取得授權" to ""
                }
            } catch (e: Exception) {
                _error.value = "授權過程出錯" to e.message
            }
        }
    }

    fun completeAuthorization(requestToken: String) {
        viewModelScope.launch {
            try {
                val accountAndSession = repository.getAccountAndSessionID(requestToken)
                _sessionState.value = accountAndSession
                val sessionData = _sessionState.value
                sessionDataStore.storeSessionData(sessionData?.second ?:"", sessionData?.first ?: "")
                showFavoriteMovies()
                showPopularMovies()
            } catch (e: Exception) {
                _error.value = "授權失敗" to e.message
            }
        }
    }

    private fun showPopularMovies() {
        viewModelScope.launch {
            try {
                _movies.value = repository.getPopularMovies()
            } catch (e:Exception) {
                _error.value = "無法取得電影資料" to e.message
            }
        }
    }

    private fun showFavoriteMovies() {
        viewModelScope.launch {
            try {
                _favoriteMovies.value = repository.getFavoriteMovies()
            } catch (e:Exception) {
                _error.value = "無法取得電影喜愛資料" to e.message
            }
        }
    }

    private fun reloadMovies() {
        showFavoriteMovies()
        showPopularMovies()
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
                Log.d("MovieViewModel", "Favorite movies updated: ${_favoriteMovies.value.lastIndex}")
            } catch (e:Exception) {
                _error.value = "無法更新喜愛電影" to e.message
            }
        }
    }

    fun isFavorite(movieId: Int): Boolean{
        return _favoriteMovies.value.any { it.id == movieId }
    }


    private fun updateFavoriteStatus(movie: Movie, isFavorite: Boolean) {
        _favoriteMovies.value = if (isFavorite) {
            _favoriteMovies.value.distinctBy { it.id }
        } else {
            _favoriteMovies.value.filter { it.id != movie.id }
        }
    }

    fun clearError(context: Context) {
        if (!networkMonitor.isConnected.value){
            openNetworkSettings(context)
        } else {
            _error.value = null
            reloadMovies()
        }
    }
}
