package com.apollo.currencyinfo.domain.model

data class CurrencyRatePair(
    val currency: Currency,
    val rate: CurrencyRate
)