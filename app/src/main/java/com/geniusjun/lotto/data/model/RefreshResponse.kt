package com.geniusjun.lotto.data.model

data class RefreshResponse(
    val accessToken: String,
    val accessTokenExpiresIn: Long,
    val tokenType: String
)

