package com.apollo.currencyinfo.presentation.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apollo.currencyinfo.databinding.ItemFavoriteCurrencyRateBinding
import com.apollo.currencyinfo.domain.model.Currency
import com.apollo.currencyinfo.domain.model.CurrencyRatePair

class FavoriteCurrencyRateAdapter(private val onSwipe: (Currency) -> Unit) :
    ListAdapter<CurrencyRatePair, FavoriteCurrencyRateAdapter.ViewHolder>(RateDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    fun onSwipeItem(position: Int) {
        val item = getItem(position)
        onSwipe(item.currency)
    }

    class ViewHolder private constructor(private val binding: ItemFavoriteCurrencyRateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemFavoriteCurrencyRateBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(pair: CurrencyRatePair) {
            binding.textName.text = pair.currency.name
            binding.textCode.text = pair.currency.code
            binding.textRate.text = pair.rate.value.toString()
        }
    }

    class RateDiffCallback : DiffUtil.ItemCallback<CurrencyRatePair>() {
        override fun areItemsTheSame(oldPair: CurrencyRatePair, newPair: CurrencyRatePair) =
            oldPair.currency.code == newPair.currency.code

        override fun areContentsTheSame(oldPair: CurrencyRatePair, newPair: CurrencyRatePair) =
            oldPair == newPair
    }
}