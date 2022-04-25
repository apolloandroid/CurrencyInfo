package com.apollo.currencyinfo.data.currencyRate.remote.dto

import com.apollo.currencyinfo.domain.model.CurrencyRate

data class CurrencyRateDto(
    val currencyCode: String,
    val value: Double
) {

    fun toDomain(): CurrencyRate = CurrencyRate(
        currencyCode = this.currencyCode,
        value = this.value
    )
}
