package com.apollo.currencyinfo.domain.usecase

import com.apollo.currencyinfo.data.currencyRate.CurrencyRateRepository
import com.apollo.currencyinfo.domain.model.Currency
import com.apollo.currencyinfo.domain.model.CurrencyRate
import com.apollo.currencyinfo.domain.util.UseCase
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetCurrencyRatesForFavoritesUseCase @Inject constructor(
    private val repository: CurrencyRateRepository
) : UseCase<GetCurrencyRatesForFavoritesUseCase.Parameters, List<CurrencyRate>>(Dispatchers.IO) {

    override suspend fun execute(parameters: Parameters): List<CurrencyRate> {
        return repository.getRatesForFavorites(
            parameters.baseCurrency,
            parameters.favoriteCurrencies
        )
    }

    data class Parameters(
        val baseCurrency: Currency,
        val favoriteCurrencies: List<Currency>
    )
}