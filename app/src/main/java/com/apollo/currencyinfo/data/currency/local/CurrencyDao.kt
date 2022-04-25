package com.apollo.currencyinfo.data.currency.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currency: CurrencyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(currencies: List<CurrencyEntity>)

    @Query("SELECT * FROM currencies")
    suspend fun getAll(): List<CurrencyEntity>

    @Query("SELECT COUNT(code) FROM currencies")
    fun getSize(): Int

    @Query("SELECT * FROM currencies")
    fun getAllFlow(): Flow<List<CurrencyEntity>>

    @Query("SELECT * FROM currencies WHERE is_favorite = 1 ")
    fun getFavoritesFlow(): Flow<List<CurrencyEntity>>

    @Query("UPDATE currencies SET is_favorite = NOT is_favorite WHERE code = :code")
    fun setIsFavorite(code: String)
}