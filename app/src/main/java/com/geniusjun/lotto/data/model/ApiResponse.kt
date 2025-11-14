package com.geniusjun.lotto.data.model

data class ApiResponse<T>(
    val success: Boolean,
    val data: T?,
    val message: String? = null,
    val error: String? = null
)


