package com.apollo.currencyinfo.data.currencyRate.remote

import com.apollo.currencyinfo.data.currencyRate.remote.dto.GetCurrencyRatesResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyRateApi {

    private val DEFAULT_BASE_CURRENCY_CODE: String
        get() = "EUR"

    @GET("/latest")
    suspend fun getCurrencyRates(
        @Query("base") baseCurrencyCode: String = DEFAULT_BASE_CURRENCY_CODE
    ): GetCurrencyRatesResponseDto

    @GET("/latest")
    suspend fun getCurrencyRatesForFavorites(
        @Query("base") baseCurrencyCode: String = DEFAULT_BASE_CURRENCY_CODE,
        @Query("symbols") favoriteCurrenciesCodes: String
    ): GetCurrencyRatesResponseDto
}