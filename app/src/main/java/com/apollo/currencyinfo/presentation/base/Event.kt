package com.apollo.currencyinfo.presentation.base

import com.apollo.currencyinfo.domain.sorting.SortingState

sealed class Event {
    class ShowRatesSortingMenu(val state: SortingState) : Event()
    class ShowMessage(val message: String) : Event()
    object HideKeyboard : Event()
}
