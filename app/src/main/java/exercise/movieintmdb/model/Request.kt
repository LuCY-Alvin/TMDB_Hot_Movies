package exercise.movieintmdb.model

data class RequestTokenResponse(
    val success: Boolean,
    val expires_at: String,
    val request_token: String
)

data class SessionResponse(
    val success: Boolean,
    val session_id: String
)

data class AccountResponse(
    val id: Int,
    val name: String,
    val username: String,
)
