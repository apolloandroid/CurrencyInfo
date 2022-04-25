package com.apollo.currencyinfo.domain.model

data class Currency(
    val code: String = defaultCode,
    val name: String = defaultName,
    val isFavorite: Boolean = false
) {

    companion object {
        const val defaultCode = "EUR"
        const val defaultName = "Euro"
    }
}
