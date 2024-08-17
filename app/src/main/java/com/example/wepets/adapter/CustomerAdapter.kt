package com.example.wepets.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wepets.databinding.CustomerItemBinding
import com.example.wepets.model.Pet

class CustomerAdapter(private val customers:List<Pet>, private val onItemClickListener: (Pet) -> Unit):RecyclerView.Adapter<CustomerAdapter.ViewHolder>() {


    inner class ViewHolder(private val customerItemBinding:CustomerItemBinding) : RecyclerView.ViewHolder(customerItemBinding.root){

        fun bind(customer:Pet){
            customerItemBinding.root.setOnClickListener {
                onItemClickListener(customer)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

}