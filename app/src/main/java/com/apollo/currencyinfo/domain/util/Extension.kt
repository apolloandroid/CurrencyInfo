package com.apollo.currencyinfo.domain.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

fun <R> Flow<Result<R>>.successes() = this.map { it.getOrThrow() }.catch { }

