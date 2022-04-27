package com.apollo.currencyinfo.domain.usecase

import com.apollo.currencyinfo.data.currency.CurrencyRepository
import com.apollo.currencyinfo.domain.model.Currency
import com.apollo.currencyinfo.domain.util.FlowUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllCurrenciesUseCase @Inject constructor(
    private val repository: CurrencyRepository
) : FlowUseCase<List<Currency>>(Dispatchers.IO) {

    override fun execute(): Flow<Result<List<Currency>>> {
        return repository.currencies.map { Result.success(it) }
    }
}