package com.example.data.remote.utils

import retrofit2.HttpException
import retrofit2.Response
import com.example.common.base.Result

suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): Result<T> {
    try {
        val response = call.invoke()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return Result.Success(body)
            }
        }

        return Result.Error(HttpException(response), response.code())
    } catch (e: Exception) {
        return Result.Error(e, -1)
    }
}