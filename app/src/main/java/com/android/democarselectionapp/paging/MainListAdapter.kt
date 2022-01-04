package com.android.democarselectionapp.paging

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.democarselectionapp.databinding.CarListBinding
import com.android.democarselectionapp.model.wkd


class MainListAdapter :
    PagingDataAdapter<wkd, MainListAdapter.CarViewHolder>(CarComparator) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CarViewHolder {
        return CarViewHolder(
            CarListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val item = getItem(position)
        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor("#635958"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#7a6d6c"));
        }
        item?.let { holder.bindPagedData(it) }
    }

    inner class CarViewHolder(private val binding: CarListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindPagedData(carObject: wkd) = with(binding) {
            manufacturerNameTextView.text = carObject.value
            containerView.setOnClickListener {
                onItemClickListener.onItemClicked(carObject.key, carObject.value)
            }
        }
    }

    object CarComparator : DiffUtil.ItemCallback<wkd>() {
        override fun areItemsTheSame(oldItem: wkd, newItem: wkd): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: wkd, newItem: wkd): Boolean {
            return oldItem == newItem
        }
    }

    lateinit var onItemClickListener: OnClickListener

    fun setOnClickListener(onItemClickListener: OnClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnClickListener {
        fun onItemClicked(carId: String, carName: String)
    }

}
