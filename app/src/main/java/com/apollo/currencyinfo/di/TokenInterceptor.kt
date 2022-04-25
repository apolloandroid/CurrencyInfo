package com.apollo.currencyinfo.di

import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(private val token: String) : Interceptor {

    companion object {
        private const val APY_TOKEN_PARAMETER = "access_key"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.newBuilder()
            .addQueryParameter(APY_TOKEN_PARAMETER, token)
            .build()
        val resultRequest = request.newBuilder().url(url).build()
        return chain.proceed(resultRequest)
    }
}