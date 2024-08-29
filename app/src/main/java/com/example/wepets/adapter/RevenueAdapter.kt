package com.example.wepets.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wepets.databinding.RevenueItemBinding
import com.example.wepets.model.Revenue

class RevenueAdapter(private val onItemClickListener: (Revenue)->Unit ):RecyclerView.Adapter<RevenueAdapter.ViewHolder>() {

    private var revenuesItens = emptyList<Revenue>()
    // Método para atualizar a lista

    fun addOnList(newRevenueItem: List<Revenue>) {
        revenuesItens = newRevenueItem
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val revenueItemBinding: RevenueItemBinding):RecyclerView.ViewHolder(revenueItemBinding.root){
        fun bind(itemRevenue: Revenue){
            Glide.with(revenueItemBinding.root.context)
                .load(itemRevenue.image)
                .circleCrop()
                .into(revenueItemBinding.ivType)

            revenueItemBinding.tvName.setText(itemRevenue.name)
            revenueItemBinding.tvValue.setText(itemRevenue.value.toString())

            revenueItemBinding.root.setOnClickListener {
                onItemClickListener(itemRevenue)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = RevenueItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
       return revenuesItens.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemRevenue = revenuesItens[position]
        holder.bind(itemRevenue)
    }
}