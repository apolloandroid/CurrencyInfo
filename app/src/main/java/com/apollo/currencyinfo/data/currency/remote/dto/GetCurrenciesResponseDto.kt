package com.apollo.currencyinfo.data.currency.remote.dto

import com.apollo.currencyinfo.data.util.ResponseDto
import com.google.gson.annotations.SerializedName

data class GetCurrenciesResponseDto(
    @SerializedName("success")
    val result: Boolean,

    @SerializedName("symbols")
    val currencies: List<CurrencyDto>
): ResponseDto