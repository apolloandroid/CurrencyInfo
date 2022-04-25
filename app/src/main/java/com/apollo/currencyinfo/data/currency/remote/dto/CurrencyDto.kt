package com.apollo.currencyinfo.data.currency.remote.dto

import com.apollo.currencyinfo.domain.model.Currency

data class CurrencyDto(
    val code: String,
    val name: String
) {

    fun toDomain(): Currency = Currency(
        code = this.code,
        name = this.name
    )
}

