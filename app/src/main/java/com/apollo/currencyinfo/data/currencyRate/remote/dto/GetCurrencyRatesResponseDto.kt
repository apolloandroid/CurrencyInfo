package com.apollo.currencyinfo.data.currencyRate.remote.dto

import com.apollo.currencyinfo.data.util.ResponseDto
import com.google.gson.annotations.SerializedName

data class GetCurrencyRatesResponseDto(
    @SerializedName("success")
    val result: Boolean,
    @SerializedName("base")
    val baseCurrencyName: String,
    @SerializedName("rates")
    val rates: List<CurrencyRateDto>
) : ResponseDto
