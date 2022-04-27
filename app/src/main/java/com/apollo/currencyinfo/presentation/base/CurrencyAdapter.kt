package com.apollo.currencyinfo.presentation.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apollo.currencyinfo.databinding.ItemCurrencyBinding
import com.apollo.currencyinfo.domain.model.Currency

class CurrencyAdapter(private val onClick: (Currency) -> Unit) :
    ListAdapter<Currency, CurrencyAdapter.ViewHolder>(CurrencyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onClick)
    }

    class ViewHolder private constructor(private val binding: ItemCurrencyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemCurrencyBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(item: Currency, onClick: (Currency) -> Unit) {
            binding.textName.text = item.name
            binding.textCode.text = item.code
            binding.root.setOnClickListener { onClick(item) }
        }
    }

    class CurrencyDiffCallback : DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency) =
            oldItem.code == newItem.code

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency) = oldItem == newItem
    }
}