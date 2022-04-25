package com.apollo.currencyinfo.data.currency

import com.apollo.currencyinfo.data.currency.local.CurrencyDao
import com.apollo.currencyinfo.data.currency.local.CurrencyEntity
import com.apollo.currencyinfo.data.currency.remote.CurrencyApi
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepository @Inject constructor(
    private val localDataSource: CurrencyDao,
    private val remoteDataSource: CurrencyApi
) {

    val currencies = localDataSource.getAllFlow().map { entities -> entities.map { it.toDomain() } }

    val favoriteCurrencies = localDataSource.getFavoritesFlow().map { entities ->
        entities.map { it.toDomain() }
    }

    suspend fun refreshCurrencies() {
        val getCurrenciesResponse = remoteDataSource.getCurrencies()
        if (getCurrenciesResponse.result) {
            val currencies = getCurrenciesResponse.currencies.map { it.toDomain() }
            localDataSource.insertAll(currencies.map { CurrencyEntity.fromDomain(it) })
        } else throw Exception("Failed to refresh currencies")
    }

    suspend fun setCurrencyIsFavorite(code: String) = localDataSource.setIsFavorite(code)
}