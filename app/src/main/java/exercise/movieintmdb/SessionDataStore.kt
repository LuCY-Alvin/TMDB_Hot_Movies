package exercise.movieintmdb

import com.tencent.mmkv.MMKV

object SessionDataStore {
    private const val MMKV_ID = "session_data"

    private val mmkv: MMKV by lazy { MMKV.mmkvWithID(MMKV_ID) }

    fun storeSessionData(sessionId: String, accountId: String) {
        mmkv.encode("session_id", sessionId)
        mmkv.encode("account_id", accountId)
    }

    fun getSessionData(): Pair<String?, String?> {
        val sessionId = mmkv.decodeString("session_id")
        val accountId = mmkv.decodeString("account_id")
        return Pair(sessionId, accountId)
    }
}