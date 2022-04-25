package com.apollo.currencyinfo.di

import android.content.Context
import androidx.room.Room
import com.apollo.currencyinfo.data.AppDatabase
import com.apollo.currencyinfo.data.currency.local.CurrencyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room
            .databaseBuilder(context, AppDatabase::class.java, AppDatabase.NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideRecordingDao(database: AppDatabase): CurrencyDao = database.currencyDao()
}