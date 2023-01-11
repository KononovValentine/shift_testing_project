package com.example.shifttestingproject.fragments.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shifttestingproject.NONE
import com.example.shifttestingproject.R
import com.example.shifttestingproject.db.BinDbModel
import kotlinx.android.synthetic.main.item_bin_layout.view.*

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view)

    var onItemClick: ((BinDbModel) -> Unit)? = null
    var onItemLongClick: ((BinDbModel) -> Unit)? = null
    var listHistory = mutableListOf<BinDbModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bin_layout, parent, false)

        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val event = listHistory[position]
        holder.itemView.tv_item_scheme.text = setNoneIfBlank(listHistory[position].scheme)
        holder.itemView.tv_item_brand.text = setNoneIfBlank(listHistory[position].brand)
        holder.itemView.tv_item_type.text = setNoneIfBlank(listHistory[position].type)
        holder.itemView.tv_item_prepaid.text = setNoneIfBlank(listHistory[position].prepaid)
        holder.itemView.tv_item_length.text = setNoneIfBlank(listHistory[position].length)
        holder.itemView.tv_item_luhn.text = setNoneIfBlank(listHistory[position].luhn)
        holder.itemView.tv_item_alpha2.text = setNoneIfBlank(listHistory[position].alpha2)
        holder.itemView.tv_item_numeric.text = setNoneIfBlank(listHistory[position].numeric)
        holder.itemView.tv_item_emoji.text = setNoneIfBlank(listHistory[position].emoji)
        holder.itemView.tv_item_county_name.text = setNoneIfBlank(listHistory[position].countryName)
        holder.itemView.tv_item_currency.text = setNoneIfBlank(listHistory[position].currency)
        holder.itemView.tv_item_latitude.text = setNoneIfBlank(listHistory[position].latitude)
        holder.itemView.tv_item_longitude.text = setNoneIfBlank(listHistory[position].longitude)
        holder.itemView.tv_item_bank_url.text = setNoneIfBlank(listHistory[position].url)
        holder.itemView.tv_item_bank_name.text = setNoneIfBlank(listHistory[position].bankName)
        holder.itemView.tv_item_bank_city.text = setNoneIfBlank(listHistory[position].city)
        holder.itemView.tv_item_bank_phone.text = setNoneIfBlank(listHistory[position].phone)

        // устанавливает слушатель нажатия на item
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(event)
        }

        // устанавливает слушатель долгого нажатия на item
        holder.itemView.setOnLongClickListener {
            onItemLongClick?.invoke(event)
            true
        }
    }

    override fun getItemCount(): Int {
        return listHistory.size
    }

    // задает список элементов для отрисовки
    fun setList(list: List<BinDbModel>) {
        listHistory = list.toMutableList()
        notifyDataSetChanged()
    }

    // подставляет None если поле пустое
    private fun setNoneIfBlank(text: String): String {
        return if (text == "") NONE else text
    }
}