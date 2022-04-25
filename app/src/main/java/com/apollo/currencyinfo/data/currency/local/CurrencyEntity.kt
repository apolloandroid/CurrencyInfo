package com.apollo.currencyinfo.data.currency.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.apollo.currencyinfo.domain.model.Currency

@Entity(tableName = "currencies")
data class CurrencyEntity(
    @PrimaryKey
    @ColumnInfo(name = "code")
    val code: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean
) {
    companion object {
        fun fromDomain(currency: Currency): CurrencyEntity = CurrencyEntity(
            code = currency.code,
            name = currency.name,
            isFavorite = currency.isFavorite,
        )
    }

    fun toDomain(): Currency = Currency(
        code = this.code,
        name = this.name,
        isFavorite = this.isFavorite
    )
}