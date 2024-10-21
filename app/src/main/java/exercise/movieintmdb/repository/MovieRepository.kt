package exercise.movieintmdb.repository

import android.util.Log
import com.tencent.mmkv.MMKV
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import exercise.movieintmdb.Constants
import exercise.movieintmdb.model.APIService
import exercise.movieintmdb.model.FavoriteRequest
import exercise.movieintmdb.model.Movie
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

@Module
@InstallIn(SingletonComponent::class)
class MovieRepository(private val apiService: APIService) {

    private var accountId:String? = null
    private var sessionId:String? = null

    suspend fun getPopularMovies(): List<Movie> {
        val response = apiService.getPopularMovies(Constants.API_KEY)
        Log.d("Repository", "Received popular movies: ${response.results}")
        return apiService.getPopularMovies(Constants.API_KEY).results
    }

    suspend fun getFavoriteMovies(): List<Movie> {
        if (accountId == null || sessionId == null) {
            throw IllegalStateException("請先進行帳號授權驗證")
        }
        return apiService.getFavoriteMovies(accountId!!, Constants.API_KEY, sessionId!!).results
    }

    suspend fun addAsFavorite(movieId: Int, isFavorite: Boolean) {
        if (accountId == null || sessionId == null) {
            throw IllegalStateException("請先進行帳號授權驗證")
        }
        val request = FavoriteRequest(media_id = movieId, favorite = isFavorite)
        apiService.addFavoriteMovies(accountId!!, Constants.API_KEY, sessionId!!, request)
    }

    suspend fun getAccountAndSessionID(requestToken: String):Pair<String?, String?> {
        val sessionResponse = apiService.createSession(
            Constants.API_KEY,
            """{"request_token": "$requestToken"}""".toRequestBody("application/json".toMediaTypeOrNull())
        )
        val sessionId = sessionResponse.body()?.session_id ?: return null to null

        val accountResponse = apiService.getAccount(Constants.API_KEY, sessionId)
        val accountId = accountResponse.body()?.id.toString()

        val mmkv = MMKV.defaultMMKV()
        mmkv.putString("session_id", sessionId)
        mmkv.putString("account_id", accountId)

        this.sessionId = sessionId
        this.accountId = accountId

        return accountId to sessionId
    }

    suspend fun getRequestToken():String? {
        val tokenResponse = apiService.getRequestToken(Constants.API_KEY)
        val requestToken = tokenResponse.body()?.request_token ?: return null
        return requestToken
    }

}