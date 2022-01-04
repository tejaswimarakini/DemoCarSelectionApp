package com.android.democarselectionapp.paging

import android.graphics.Color
import android.graphics.ColorSpace
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.democarselectionapp.databinding.ModelListviewBinding
import com.android.democarselectionapp.model.wkd
import com.android.democarselectionapp.paging.CarModelListAdapter.CarModelViewHolder

class CarModelListAdapter :
    PagingDataAdapter<wkd, CarModelViewHolder>(CarComparator) {

    inner class CarModelViewHolder(private val binding: ModelListviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindPagedData(carObject: wkd) = with(binding) {
            carModelTextView.text = carObject.value
            containerLayout.setOnClickListener {
                onItemClickListener.onItemClicked(carObject.key, carObject.value)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CarModelViewHolder {
        return CarModelViewHolder(
            ModelListviewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CarModelViewHolder, position: Int) {
        val item = getItem(position)
        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor("#635958"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#7a6d6c"));
        }
        item?.let { holder.bindPagedData(it) }
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