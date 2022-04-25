package com.apollo.currencyinfo.di

import com.apollo.currencyinfo.BuildConfig
import com.apollo.currencyinfo.data.currency.remote.CurrencyApi
import com.apollo.currencyinfo.data.currency.remote.dto.GetCurrenciesResponseDto
import com.apollo.currencyinfo.data.currencyRate.remote.CurrencyRateApi
import com.apollo.currencyinfo.data.currencyRate.remote.dto.GetCurrencyRatesResponseDto
import com.apollo.currencyinfo.data.util.RsponseDeserializer
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CurrencyEndpoint

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CurrencyRateEndpoint

    @Provides
    @Singleton
    fun provideCurrencyApi(@CurrencyEndpoint retrofit: Retrofit): CurrencyApi =
        retrofit.create(CurrencyApi::class.java)

    @Provides
    @Singleton
    fun provideCurrencyRateApi(@CurrencyRateEndpoint retrofit: Retrofit): CurrencyRateApi =
        retrofit.create(CurrencyRateApi::class.java)

    @Provides
    @Singleton
    @CurrencyEndpoint
    fun provideCurrencyRetrofit(
        httpClient: OkHttpClient,
        @CurrencyEndpoint converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(converterFactory)
            .client(httpClient)
            .build()
    }

    @Provides
    @Singleton
    @CurrencyRateEndpoint
    fun provideCurrencyRateRetrofit(
        httpClient: OkHttpClient,
        @CurrencyRateEndpoint converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(converterFactory)
            .client(httpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor(BuildConfig.API_TOKEN))
            .build()
    }

    @Provides
    @Singleton
    @CurrencyEndpoint
    fun provideCurrencyGsonConverter(): Converter.Factory = GsonConverterFactory.create(
        GsonBuilder().registerTypeAdapter(
            GetCurrenciesResponseDto::class.java,
            RsponseDeserializer()
        ).create()
    )

    @Provides
    @Singleton
    @CurrencyEndpoint
    fun provideCurrencyRateGsonConverter(): Converter.Factory = GsonConverterFactory.create(
        GsonBuilder().registerTypeAdapter(
            GetCurrencyRatesResponseDto::class.java,
            RsponseDeserializer()
        ).create()
    )
}