package com.apollo.currencyinfo.presentation.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

open class BaseViewModel : ViewModel() {
    protected val _events = Channel<Event>(Channel.UNLIMITED)
    val events = _events.receiveAsFlow()
}