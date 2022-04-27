package com.apollo.currencyinfo.domain.usecase

import com.apollo.currencyinfo.data.currency.CurrencyRepository
import com.apollo.currencyinfo.domain.util.UseCase
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class RefreshCurrenciesUseCase @Inject constructor(
    private val repository: CurrencyRepository
) : UseCase<Unit, Unit>(Dispatchers.IO) {

    override suspend fun execute(parameters: Unit) {
        if (repository.getLocalCurrencies().isEmpty()) repository.refreshCurrencies()
    }
}