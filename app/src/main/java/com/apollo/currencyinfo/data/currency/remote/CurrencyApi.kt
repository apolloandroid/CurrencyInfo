package com.apollo.currencyinfo.data.currency.remote

import com.apollo.currencyinfo.data.currency.remote.dto.GetCurrenciesResponseDto
import retrofit2.http.GET

interface CurrencyApi {

    @GET("/symbols")
   suspend fun getCurrencies(): GetCurrenciesResponseDto
}