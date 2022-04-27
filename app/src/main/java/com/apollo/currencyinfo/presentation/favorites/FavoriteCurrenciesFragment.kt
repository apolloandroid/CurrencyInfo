package com.apollo.currencyinfo.presentation.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.apollo.currencyinfo.R
import com.apollo.currencyinfo.databinding.FragmentFavoriteCurrenciesBinding
import com.apollo.currencyinfo.databinding.MenuRatesSortBinding
import com.apollo.currencyinfo.domain.sorting.SortingOrder
import com.apollo.currencyinfo.domain.sorting.SortingParameter
import com.apollo.currencyinfo.domain.sorting.SortingState
import com.apollo.currencyinfo.presentation.base.BaseFragment
import com.apollo.currencyinfo.presentation.base.CurrencyAdapter
import com.apollo.currencyinfo.presentation.util.collectOnLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteCurrenciesFragment : BaseFragment() {

    private lateinit var binding: FragmentFavoriteCurrenciesBinding
    override val viewModel: FavoriteCurrenciesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteCurrenciesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBaseCurrency()
        setUpChangeBaseCurrencyButton()
        setUpBaseCurrencySearch()
        setUpCurrenciesRecycler()
        setUpSortButton()
        setUpRatesLayout()
        setUpRatesRecycler()
    }

    override fun showRatesSortingMenu(sortingState: SortingState) {
        val popUpView = MenuRatesSortBinding.inflate(layoutInflater)
        setUpSortingParameterRadioGroup(popUpView, sortingState)
        setUpSortingOrderRadioGroup(popUpView, sortingState)

        val profilesFilterMenu = PopupWindow(
            popUpView.root,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        profilesFilterMenu.apply {
            isOutsideTouchable = true
            elevation = 5f
            showAsDropDown(binding.buttonSortRates, 0, 0, GravityCompat.END)
        }
    }

    private fun setUpBaseCurrency() = collectOnLifecycle(viewModel.baseCurrency) {
        binding.textCurrencyName.text = it.name
        binding.textCurrencyCode.text = it.code
    }

    private fun setUpChangeBaseCurrencyButton() {
        binding.buttonChangeBaseCurrency.setOnClickListener { viewModel.onChangeBaseCurrencyClicked() }
        collectOnLifecycle(viewModel.isChangeBaseCurrencyButtonVisible) {
            binding.buttonChangeBaseCurrency.isVisible = it
        }
    }

    private fun setUpBaseCurrencySearch() {
        binding.editSearch.doAfterTextChanged {
            viewModel.onBaseCurrencySearchChanged(it.toString())
            if (it.isNullOrEmpty()) binding.recyclerCurrencies.scrollToPosition(0)
        }
        collectOnLifecycle(viewModel.isSearchLayoutVisible) {
            binding.layoutSearch.isVisible = it
            binding.buttonChangeBaseCurrency.background =
                if (it) ContextCompat.getDrawable(requireContext(), R.drawable.ic_dropup)
                else ContextCompat.getDrawable(requireContext(), R.drawable.ic_dropdown)
        }
    }

    private fun setUpCurrenciesRecycler() {
        val adapter = CurrencyAdapter(viewModel::onBaseCurrencyClicked)
        binding.recyclerCurrencies.adapter = adapter
        collectOnLifecycle(viewModel.baseCurrencies) { adapter.submitList(it) }
    }

    private fun setUpSortButton() {
        binding.buttonSortRates.setOnClickListener { viewModel.onSortButtonClicked() }
        collectOnLifecycle(viewModel.isSortButtonVisible) {
            binding.buttonSortRates.isVisible = it
        }
    }

    private fun setUpRatesLayout() {
        binding.layoutSwipeToRefresh.setOnRefreshListener(viewModel::onSwipeToRefreshRates)
        collectOnLifecycle(viewModel.isLoaderVisible) {
            binding.layoutSwipeToRefresh.isRefreshing = it
        }
        collectOnLifecycle(viewModel.isRatesVisible) {
            binding.layoutSwipeToRefresh.isVisible = it
        }
        collectOnLifecycle(viewModel.isNoFavoritesTextVisible) {
            binding.textNoFavorites.isVisible = it
        }
    }

    private fun setUpRatesRecycler() {
        val adapter = FavoriteCurrencyRateAdapter(viewModel::onSwipeToDeleteCurrency)
        binding.recyclerRates.adapter = adapter
        val itemDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        AppCompatResources.getDrawable(requireContext(), R.drawable.background_divider)
            ?.let { itemDecoration.setDrawable(it) }

        binding.recyclerRates.addItemDecoration(itemDecoration)

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.onSwipeItem(viewHolder.adapterPosition)
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.recyclerRates)

        collectOnLifecycle(viewModel.currencyRatePairs) { adapter.submitList(it) }
        collectOnLifecycle(viewModel.isNoFavoritesTextVisible) {
            binding.textNoFavorites.isVisible = it
        }
    }

    private fun setUpSortingParameterRadioGroup(
        binding: MenuRatesSortBinding,
        state: SortingState
    ) {
        binding.radioGroupParameter.apply {
            val ids = listOf(
                Pair(binding.radioButtonCode.id, SortingParameter.CODE),
                Pair(binding.radioButtonRate.id, SortingParameter.RATE)
            )
            check(ids.first { it.second == state.parameter }.first)
            setOnCheckedChangeListener { _, id ->
                val parameter = if (id == binding.radioButtonCode.id) SortingParameter.CODE
                else SortingParameter.RATE
                viewModel.onSortingParameterSelected(parameter)
            }
        }
    }

    private fun setUpSortingOrderRadioGroup(binding: MenuRatesSortBinding, state: SortingState) {
        binding.radioGroupOrder.apply {
            val ids = listOf(
                Pair(binding.radioButtonAscending.id, SortingOrder.ASCENDING),
                Pair(binding.radioButtonDescending.id, SortingOrder.DESCENDING),
            )
            check(ids.first { it.second == state.order }.first)
            setOnCheckedChangeListener { _, id ->
                val order = if (id == binding.radioButtonAscending.id) SortingOrder.ASCENDING
                else SortingOrder.DESCENDING
                viewModel.onSortingOrderSelected(order)
            }
        }
    }
}