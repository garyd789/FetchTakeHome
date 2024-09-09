package com.example.fetchtakehome

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.fetchtakehome.databinding.ItemLayoutBinding
import java.util.Locale

class ItemAdapter(private var items: List<Item>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>(),
    Filterable {
    private var itemsFull: List<Item> = ArrayList(items)  // A copy of items to filter from

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.binding.listIDTextView.text = item.listId.toString()
        holder.binding.nameTextView.text = item.name ?: ""
    }

    override fun getItemCount() = items.size

    class ItemViewHolder(val binding: ItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)


    // Function used to filter list based on search results
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList: MutableList<Item> = mutableListOf()
                if (constraint.isNullOrEmpty()) {
                    filteredList.addAll(itemsFull)
                } else {
                    val filterPattern = constraint.toString().lowercase(Locale.ROOT).trim()
                    for (item in itemsFull) {
                        if (item.name?.lowercase(Locale.ROOT)?.contains(filterPattern) == true) {
                            filteredList.add(item)
                        }
                    }
                }

                return FilterResults().apply { values = filteredList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                items = results?.values as List<Item>
                notifyDataSetChanged()
            }
        }
    }
}