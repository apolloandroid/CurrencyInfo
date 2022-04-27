package com.apollo.currencyinfo.presentation.popular

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apollo.currencyinfo.R
import com.apollo.currencyinfo.databinding.ItemRateBinding
import com.apollo.currencyinfo.domain.model.Currency
import com.apollo.currencyinfo.domain.model.CurrencyRatePair

class CurrencyRateAdapter(private val onClick: (Currency) -> Unit) :
    ListAdapter<CurrencyRatePair, CurrencyRateAdapter.ViewHolder>(RateDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onClick)
    }

    class ViewHolder private constructor(private val binding: ItemRateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemRateBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(pair: CurrencyRatePair, onClick: (Currency) -> Unit) {
            val favoriteBackgroundRes = getFavoriteBackgroundRes(pair.currency.isFavorite)

            binding.textName.text = pair.currency.name
            binding.textCode.text = pair.currency.code
            binding.textRate.text = pair.rate.value.toString()
            binding.buttonFavorite.apply {
                background = ContextCompat.getDrawable(binding.root.context, favoriteBackgroundRes)
                setOnClickListener { onClick(pair.currency) }
            }
        }

        @DrawableRes
        private fun getFavoriteBackgroundRes(isFavorite: Boolean): Int {
            return if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_not_favorite
        }
    }

    class RateDiffCallback : DiffUtil.ItemCallback<CurrencyRatePair>() {
        override fun areItemsTheSame(oldPair: CurrencyRatePair, newPair: CurrencyRatePair) =
            oldPair.currency.code == newPair.currency.code

        override fun areContentsTheSame(oldPair: CurrencyRatePair, newPair: CurrencyRatePair) =
            oldPair == newPair
    }
}