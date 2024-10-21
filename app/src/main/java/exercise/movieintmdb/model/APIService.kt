package exercise.movieintmdb.model

import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface APIService {

    // 取得熱門電影
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String
    ): MovieResponse

    // 加入喜愛電影
    @POST("account/{account_id}/favorite")
    suspend fun addFavoriteMovies(
        @Path("account_id") accountId: String,
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String,
        @Body favoriteRequest: FavoriteRequest
    )

    // 取得喜愛電影列表
    @GET("account/{account_id}/favorite/movies")
    suspend fun getFavoriteMovies(
        @Path("account_id") accountId: String,
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String
    ): MovieResponse

    @GET("authentication/token/new")
    suspend fun getRequestToken(
        @Query("api_key") apiKey: String
    ): Response<RequestTokenResponse>

    @POST("authentication/session/new")
    suspend fun createSession(
        @Query("api_key") apiKey: String,
        @Body requestBody: RequestBody
    ): Response<SessionResponse>

    @GET("account")
    suspend fun getAccount(
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String
    ): Response<AccountResponse>
}