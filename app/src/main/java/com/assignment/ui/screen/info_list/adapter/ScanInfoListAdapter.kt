package com.assignment.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.assignment.R
import com.assignment.databinding.ItemDeviceTitleBinding
import com.assignment.ui.base.BaseViewHolder
import javax.inject.Inject

class ScanInfoListAdapter @Inject constructor() :
    RecyclerView.Adapter<ScanInfoListAdapter.ArticleItemViewHolder>() {

    var infoItemList = listOf(R.string.device, R.string.system, R.string.storage, R.string.cpu, R.string.battery, R.string.screen)


    var callback: ((Int) -> Unit)? =null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleItemViewHolder {
        return ArticleItemViewHolder(
            ItemDeviceTitleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ArticleItemViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return infoItemList.size
    }

    fun setItemClickListener(function: (Int) -> Unit) {
        callback = function
    }

    inner class ArticleItemViewHolder(private val binding: ItemDeviceTitleBinding) :
        BaseViewHolder(binding.root) {
        override fun onBind(position: Int) {
            binding.title = binding.root.context.getString(infoItemList [position])
            binding.root.setOnClickListener {
                callback?.invoke(position)
            }

            binding.executePendingBindings()
        }

    }
}