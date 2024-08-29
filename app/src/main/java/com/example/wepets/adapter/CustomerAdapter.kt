package com.example.wepets.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wepets.R
import com.example.wepets.databinding.CustomerItemBinding
import com.example.wepets.model.Pet

class CustomerAdapter(private val onItemClickListener: (Pet) -> Unit):RecyclerView.Adapter<CustomerAdapter.ViewHolder>() {

    private var customers = emptyList<Pet>()
    // Método para atualizar a lista

    fun addOnList(newCustomers: List<Pet>) {
        customers = newCustomers
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val customerItemBinding:CustomerItemBinding) : RecyclerView.ViewHolder(customerItemBinding.root){

        fun bind(customer:Pet){


            Glide.with(customerItemBinding.root.context)
                .load(customer.photoUrl)
                .circleCrop()
                .error(R.drawable.dogdefault)
                .into(customerItemBinding.ivProfile)

            customerItemBinding.txtPetName.setText(customer.namePet.replaceFirstChar { it.uppercase() })
            customerItemBinding.txtPhone.setText(customer.phoneNumber)
            customerItemBinding.txtPetBreed.setText(customer.breed.replaceFirstChar { it.uppercase() })
            customerItemBinding.txtOwnerName.setText(customer.ownerName.replaceFirstChar { it.uppercase() })

            Glide.with(customerItemBinding.root.context)
                .load(getGender(customer.sexPet))
                .error(R.drawable.ic_female)
                .centerCrop()
                .into(customerItemBinding.ivPetSex)


            customerItemBinding.root.setOnClickListener {
                onItemClickListener(customer)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = CustomerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return customers.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val customer = customers[position]
        holder.bind(customer)
    }


    fun getGender(gender:String): Int {
        if(gender == "Macho"){
            return R.drawable.ic_male
        }else{
            return R.drawable.ic_female
        }
    }




}