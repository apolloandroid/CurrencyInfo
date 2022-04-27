package com.apollo.currencyinfo.presentation.base

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.apollo.currencyinfo.domain.sorting.SortingState
import com.apollo.currencyinfo.presentation.util.collectOnLifecycle
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment : Fragment() {
    protected abstract val viewModel: BaseViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectOnLifecycle(viewModel.events) {
            when (it) {
                is Event.ShowRatesSortingMenu -> showRatesSortingMenu(it.state)
                is Event.ShowMessage -> showMessage(it.message)
                is Event.HideKeyboard -> hideKeyboard()
            }
        }
    }

    protected abstract fun showRatesSortingMenu(sortingState: SortingState)

    private fun showMessage(message: String) {
        view?.let { Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show() }
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
}