package com.apollo.currencyinfo.data.util

import com.apollo.currencyinfo.data.currency.remote.dto.CurrencyDto
import com.apollo.currencyinfo.data.currency.remote.dto.GetCurrenciesResponseDto
import com.apollo.currencyinfo.data.currencyRate.remote.dto.CurrencyRateDto
import com.apollo.currencyinfo.data.currencyRate.remote.dto.GetCurrencyRatesResponseDto
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type

/**
 * Custom Deserializer. As we have different server response Json structure with a lot of types,
 * we must parse each type by hands.
 */
class ResponseDeserializer : JsonDeserializer<ResponseDto> {

    companion object {
        private const val KEY_SUCCESS = "success"
        private const val KEY_SYMBOLS = "symbols"
        private const val KEY_BASE = "base"
        private const val KEY_RATES = "rates"
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ResponseDto {
        return json?.asJsonObject?.let {
            when {
                it.has(KEY_SYMBOLS) -> parseToGetCurrenciesDto(it)
                it.has(KEY_RATES) -> parseToGetCurrencyRatesDto(it)
                else -> throw NullPointerException()
            }
        } ?: throw NullPointerException()
    }

    private fun parseToGetCurrenciesDto(jsonObject: JsonObject): GetCurrenciesResponseDto {
        val result = jsonObject.get(KEY_SUCCESS).asBoolean
        val symbols = jsonObject.get(KEY_SYMBOLS).asJsonObject
        val currencies = symbols.entrySet().iterator().asSequence().map { entry ->
            CurrencyDto(entry.key, entry.value.asString)
        }
        return GetCurrenciesResponseDto(result, currencies.toList())
    }

    private fun parseToGetCurrencyRatesDto(jsonObject: JsonObject): GetCurrencyRatesResponseDto {
        val result = jsonObject.get(KEY_SUCCESS).asBoolean
        val baseCurrencyName = jsonObject.get(KEY_BASE).asString
        val rates = jsonObject.get(KEY_RATES).asJsonObject
        val currencyRates = rates.entrySet().iterator().asSequence().map { entry ->
            CurrencyRateDto(entry.key, entry.value.asDouble)
        }
        return GetCurrencyRatesResponseDto(result, baseCurrencyName, currencyRates.toList())
    }
}