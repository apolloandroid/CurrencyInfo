package com.apollo.currencyinfo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.apollo.currencyinfo.data.currency.local.CurrencyDao
import com.apollo.currencyinfo.data.currency.local.CurrencyEntity

@Database(
    entities = [CurrencyEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun currencyDao(): CurrencyDao

    companion object {
        const val NAME = "currency_info_database"
    }
}
