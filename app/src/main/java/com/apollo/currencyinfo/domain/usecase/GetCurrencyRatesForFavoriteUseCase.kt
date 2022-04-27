package com.apollo.currencyinfo.domain.usecase

import com.apollo.currencyinfo.data.currencyRate.CurrencyRateRepository
import com.apollo.currencyinfo.domain.model.Currency
import com.apollo.currencyinfo.domain.util.UseCase
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetCurrencyRatesForFavoriteUseCase @Inject constructor(
    private val repository: CurrencyRateRepository
) : UseCase<GetCurrencyRatesForFavoriteUseCase.Parameters, Unit>(Dispatchers.IO) {

    override suspend fun execute(parameters: Parameters) {
        repository.getRatesForFavorites(parameters.baseCurrency, parameters.favoriteCurrencies)
    }

    data class Parameters(
        val baseCurrency: Currency,
        val favoriteCurrencies: List<Currency>
    )
}