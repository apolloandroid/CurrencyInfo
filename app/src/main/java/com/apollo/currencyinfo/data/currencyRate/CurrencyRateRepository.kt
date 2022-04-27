package com.apollo.currencyinfo.data.currencyRate

import com.apollo.currencyinfo.data.currencyRate.remote.CurrencyRateApi
import com.apollo.currencyinfo.domain.model.Currency
import com.apollo.currencyinfo.domain.model.CurrencyRate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRateRepository @Inject constructor(
    private val remoteDataSource: CurrencyRateApi
) {

    suspend fun getRates(baseCurrency: Currency): List<CurrencyRate> {
        val response = remoteDataSource.getCurrencyRates(baseCurrency.code)
        return response.rates.map { it.toDomain() }
    }

    suspend fun getRatesForFavorites(
        baseCurrency: Currency,
        favoriteCurrencies: List<Currency>
    ): List<CurrencyRate> {
        val favoritesCodes = favoriteCurrencies.map { it.code }
        val response = remoteDataSource.getCurrencyRatesForFavorites(
            baseCurrency.code,
            favoritesCodes.joinToString(separator = ",")
        )
        return response.rates.map { it.toDomain() }
    }
}