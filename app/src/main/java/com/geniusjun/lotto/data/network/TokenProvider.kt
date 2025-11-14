package com.geniusjun.lotto.data.network

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit

class TokenProvider(context: Context) {
    private val prefs: SharedPreferences = 
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    
    private val KEY_ACCESS_TOKEN = "access_token"
    private val KEY_REFRESH_TOKEN = "refresh_token"
    
    fun getAccessToken(): String? {
        val token = prefs.getString(KEY_ACCESS_TOKEN, null)
        if (token != null) {
            Log.d(TAG, "🔑 Access Token 조회: ${token.take(20)}...")
        } else {
            Log.d(TAG, "⚠️ Access Token 없음")
        }
        return token
    }
    
    fun saveTokens(accessToken: String, refreshToken: String) {
        prefs.edit {
            putString(KEY_ACCESS_TOKEN, accessToken)
            putString(KEY_REFRESH_TOKEN, refreshToken)
        }
        Log.d(TAG, "💾 토큰 저장 완료")
        Log.d(TAG, "   Access Token: ${accessToken.take(20)}...")
        Log.d(TAG, "   Refresh Token: ${refreshToken.take(20)}...")
    }
    
    fun clearTokens() {
        prefs.edit { clear() }
        Log.d(TAG, "🗑️ 토큰 삭제 완료")
    }
    
    companion object {
        private const val TAG = "TokenProvider"
    }
}

