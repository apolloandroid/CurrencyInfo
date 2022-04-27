package com.apollo.currencyinfo.presentation.common

import com.apollo.currencyinfo.domain.sorting.SortingState

sealed class Event {
    class ShowRatesSortingMenu(val state: SortingState) : Event()
    class ShowToast(val message: String) : Event()
}
