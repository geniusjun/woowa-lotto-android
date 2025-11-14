package com.geniusjun.lotto.data.network

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * Access Token과 Refresh Token을 관리하는 클래스
 */
class TokenProvider(context: Context) {
    private val prefs: SharedPreferences = 
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    
    private val KEY_ACCESS_TOKEN = "access_token"
    private val KEY_REFRESH_TOKEN = "refresh_token"
    
    fun getAccessToken(): String? {
        return prefs.getString(KEY_ACCESS_TOKEN, null)
    }
    
    fun saveTokens(accessToken: String, refreshToken: String) {
        prefs.edit {
            putString(KEY_ACCESS_TOKEN, accessToken)
            putString(KEY_REFRESH_TOKEN, refreshToken)
        }
    }
    
    fun clearTokens() {
        prefs.edit { clear() }
    }
}

