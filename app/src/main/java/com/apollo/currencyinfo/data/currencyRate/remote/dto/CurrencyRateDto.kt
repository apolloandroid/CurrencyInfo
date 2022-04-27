package com.apollo.currencyinfo.data.currencyRate.remote.dto

import com.apollo.currencyinfo.domain.model.CurrencyRate
import kotlin.math.roundToInt

data class CurrencyRateDto(
    val currencyCode: String,
    val value: Double
) {

    fun toDomain(): CurrencyRate = CurrencyRate(
        currencyCode = this.currencyCode,
        value = this.value.roundTwoDecimal()
    )

    private fun Double.roundTwoDecimal() = (this * 100).roundToInt() / 100.0

}
