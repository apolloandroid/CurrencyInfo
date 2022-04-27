package com.apollo.currencyinfo.domain.sorting

import com.apollo.currencyinfo.domain.model.CurrencyRatePair

object CurrencyRateSorter {

    fun sort(
        parameter: SortingParameter,
        order: SortingOrder,
        pairs: List<CurrencyRatePair>
    ): List<CurrencyRatePair> {
        val comparator = getComparator(parameter)
        return if (order == SortingOrder.DESCENDING) pairs.sortedWith(comparator).asReversed()
        else pairs.sortedWith(comparator)
    }

    private fun getComparator(parameter: SortingParameter): Comparator<CurrencyRatePair> {
        return Comparator { a: CurrencyRatePair, b: CurrencyRatePair ->
            if (parameter == SortingParameter.RATE) a.rate.value.compareTo(b.rate.value)
            else a.currency.code.compareTo(b.currency.code)
        }
    }
}