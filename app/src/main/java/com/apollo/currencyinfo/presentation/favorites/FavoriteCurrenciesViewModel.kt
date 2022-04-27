package com.apollo.currencyinfo.presentation.favorites

import androidx.lifecycle.viewModelScope
import com.apollo.currencyinfo.domain.model.Currency
import com.apollo.currencyinfo.domain.model.CurrencyRate
import com.apollo.currencyinfo.domain.model.CurrencyRatePair
import com.apollo.currencyinfo.domain.sorting.CurrencyRateSorter
import com.apollo.currencyinfo.domain.sorting.SortingOrder
import com.apollo.currencyinfo.domain.sorting.SortingParameter
import com.apollo.currencyinfo.domain.sorting.SortingState
import com.apollo.currencyinfo.domain.usecase.GetCurrenciesUseCase
import com.apollo.currencyinfo.domain.usecase.GetCurrencyRatesForFavoritesUseCase
import com.apollo.currencyinfo.domain.usecase.SetCurrencyIsFavoriteUseCase
import com.apollo.currencyinfo.domain.util.successes
import com.apollo.currencyinfo.presentation.base.BaseViewModel
import com.apollo.currencyinfo.presentation.base.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteCurrenciesViewModel @Inject constructor(
    private val getCurrenciesUseCase: GetCurrenciesUseCase,
    private val getCurrencyRatesForFavoritesUseCase: GetCurrencyRatesForFavoritesUseCase,
    private val setCurrencyIsFavoriteUseCase: SetCurrencyIsFavoriteUseCase
) : BaseViewModel() {

    private val _baseCurrency = MutableStateFlow<Currency>(Currency())
    val baseCurrency = _baseCurrency.asStateFlow()

    private val _isSearchLayoutVisible = MutableStateFlow<Boolean>(false)
    val isSearchLayoutVisible = _isSearchLayoutVisible.asStateFlow()

    private val currencies = getCurrenciesUseCase().successes()
    private val favoriteCurrencies = currencies.map { currencies ->
        currencies.filter { it.isFavorite }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, listOf())
    private val currencyNameToSearch = MutableStateFlow<String?>(null)
    val baseCurrencies = getCurrencies()

    val isChangeBaseCurrencyButtonVisible = currencies.map { it.size > 1 }

    private val rates = MutableStateFlow<List<CurrencyRate>>(listOf())
    private val sortingOrder = MutableStateFlow<SortingOrder>(SortingOrder.ASCENDING)
    private val sortingParameter = MutableStateFlow<SortingParameter>(SortingParameter.CODE)
    val currencyRatePairs = getCurrenciesRatesPairs()

    val isSortButtonVisible = currencyRatePairs.map { it.size > 1 }

    private val _isLoaderVisible = MutableStateFlow<Boolean>(false)
    val isLoaderVisible = _isLoaderVisible.asStateFlow()

    val isNoFavoritesTextVisible = currencyRatePairs.map { it.isEmpty() }

    val isRatesVisible = currencyRatePairs.map { it.isNotEmpty() }


    init {
        getRatesForFavoriteCurrencies(baseCurrency.value)
    }

    fun onChangeBaseCurrencyClicked() = _isSearchLayoutVisible.update { !it }

    fun onBaseCurrencySearchChanged(name: String?) = currencyNameToSearch.update { name }

    fun onBaseCurrencyClicked(currency: Currency) = getRatesForFavoriteCurrencies(currency)

    fun onSortButtonClicked() = _events.trySend(
        Event.ShowRatesSortingMenu(SortingState(sortingOrder.value, sortingParameter.value))
    )

    fun onSwipeToRefreshRates() = getRatesForFavoriteCurrencies(baseCurrency.value)

    fun onSwipeToDeleteCurrency(currency: Currency) = viewModelScope.launch {
        setCurrencyIsFavoriteUseCase(SetCurrencyIsFavoriteUseCase.Parameters(currency))
            .onSuccess { }
    }

    fun onSortingParameterSelected(parameter: SortingParameter) {
        sortingParameter.update { parameter }
    }

    fun onSortingOrderSelected(order: SortingOrder) = sortingOrder.update { order }

    private fun getCurrencies(): Flow<List<Currency>> {
        return combine(currencies, currencyNameToSearch) { currencies, name ->
            if (name.isNullOrEmpty()) currencies
            else currencies.filter { it.code.contains(name, true) || it.name.contains(name, true) }
        }
    }

    private fun getCurrenciesRatesPairs() = combine(
        favoriteCurrencies, rates, sortingParameter, sortingOrder
    ) { currencies, rates, parameter, order ->
        val currenciesByCode = currencies.associateBy { it.code }
        val pairs = rates.mapNotNull { rate ->
            currenciesByCode[rate.currencyCode]?.let { currency ->
                CurrencyRatePair(currency, rate)
            }
        }
        CurrencyRateSorter.sort(parameter, order, pairs)
    }

    private fun getRatesForFavoriteCurrencies(baseCurrency: Currency) = viewModelScope.launch {
        _isLoaderVisible.update { true }
        _isSearchLayoutVisible.update { false }
        _events.trySend(Event.HideKeyboard)
        getCurrencyRatesForFavoritesUseCase(
            GetCurrencyRatesForFavoritesUseCase.Parameters(baseCurrency, favoriteCurrencies.value)
        ).onSuccess { newRates ->
            rates.update { newRates }
            _baseCurrency.update { baseCurrency }
        }.onFailure {
            _events.trySend(
                Event.ShowMessage("Failed to get rates for this base currency. Try again  later")
            )
        }
        _isLoaderVisible.update { false }
    }
}