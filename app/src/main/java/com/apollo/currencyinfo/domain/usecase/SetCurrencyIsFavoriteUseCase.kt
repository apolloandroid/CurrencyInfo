package com.apollo.currencyinfo.domain.usecase

import com.apollo.currencyinfo.data.currency.CurrencyRepository
import com.apollo.currencyinfo.domain.model.Currency
import com.apollo.currencyinfo.domain.util.UseCase
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class SetCurrencyIsFavoriteUseCase @Inject constructor(
    private val repository: CurrencyRepository
) : UseCase<SetCurrencyIsFavoriteUseCase.Parameters, Unit>(Dispatchers.IO) {

    override suspend fun execute(parameters: Parameters) {
        repository.setCurrencyIsFavorite(parameters.currency)
    }

    data class Parameters(val currency: Currency)
}