package com.apollo.currencyinfo.presentation.base

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.apollo.currencyinfo.domain.sorting.SortingState
import com.apollo.currencyinfo.presentation.util.collectOnLifecycle

abstract class BaseFragment : Fragment() {
    protected abstract val viewModel: BaseViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectOnLifecycle(viewModel.events) {
            when (it) {
                is Event.ShowRatesSortingMenu -> showRatesSortingMenu(it.state)
                is Event.ShowToast -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                is Event.HideKeyboard -> hideKeyboard()
            }
        }
    }

    protected abstract fun showRatesSortingMenu(sortingState: SortingState)

    private fun hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
}