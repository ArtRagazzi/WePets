package com.example.wepets.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wepets.R
import com.example.wepets.databinding.CustomerItemBinding
import com.example.wepets.model.Pet

class CustomerAdapter(private var customers:List<Pet>, private val onItemClickListener: (Pet) -> Unit):RecyclerView.Adapter<CustomerAdapter.ViewHolder>() {


    inner class ViewHolder(private val customerItemBinding:CustomerItemBinding) : RecyclerView.ViewHolder(customerItemBinding.root){

        fun bind(customer:Pet){


            Glide.with(customerItemBinding.root.context)
                .load(customer.photoUrl)
                .error(R.drawable.dogdefault)
                .into(customerItemBinding.ivProfile)

            customerItemBinding.txtPetName.setText(customer.namePet)
            customerItemBinding.txtPhone.setText(customer.phoneNumber)
            customerItemBinding.txtPetBreed.setText(customer.breed)
            customerItemBinding.txtOwnerName.setText(customer.ownerName)

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

    // Método para atualizar a lista
    fun updateCustomers(newCustomers: List<Pet>) {
        customers = newCustomers
        notifyDataSetChanged() // Notifica que os dados foram atualizados
    }


}