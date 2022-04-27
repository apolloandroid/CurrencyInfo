package com.apollo.currencyinfo.domain.usecase

import com.apollo.currencyinfo.data.currencyRate.CurrencyRateRepository
import com.apollo.currencyinfo.domain.model.Currency
import com.apollo.currencyinfo.domain.model.CurrencyRate
import com.apollo.currencyinfo.domain.util.UseCase
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetCurrencyRatesUseCase @Inject constructor(
    private val repository: CurrencyRateRepository
) : UseCase<GetCurrencyRatesUseCase.Parameters, List<CurrencyRate>>(Dispatchers.IO) {

    override suspend fun execute(parameters: Parameters): List<CurrencyRate> {
        return repository.getRates(parameters.baseCurrency)
    }

    data class Parameters(val baseCurrency: Currency)
}