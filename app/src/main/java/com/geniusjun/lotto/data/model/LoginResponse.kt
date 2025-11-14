package com.geniusjun.lotto.data.model

data class LoginResponse(
    val memberId: Long,
    val nickname: String,
    val accessToken: String,
    val accessTokenExpiresIn: Long,
    val refreshToken: String,
    val refreshTokenExpiresIn: Long,
    val tokenType: String
)


