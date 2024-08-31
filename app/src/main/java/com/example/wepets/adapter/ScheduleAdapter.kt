package com.example.wepets.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wepets.R
import com.example.wepets.databinding.ScheduleItemBinding
import com.example.wepets.db.dao.PetDao
import com.example.wepets.db.database.WePetsDatabase
import com.example.wepets.model.Pet
import com.example.wepets.model.Schedule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScheduleAdapter(private val onItemClickListener:(Pet) -> Unit ,context: Context):RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {

    private var schedules = emptyList<Schedule>()
    // Método para atualizar a lista

    fun addOnList(newSchedule: List<Schedule>) {
        schedules = newSchedule
        notifyDataSetChanged()
    }
    private val db by lazy {
        WePetsDatabase.getDatabase(context)
    }

    private val petDao: PetDao by lazy {
        db.getPetDao
    }

    inner class ViewHolder(private val scheduleItemBinding: ScheduleItemBinding):RecyclerView.ViewHolder(scheduleItemBinding.root){
        fun bind(schedule:Schedule){

            CoroutineScope(Dispatchers.Main).launch {
                val customer = getCustomer(schedule.customerId)

                // Atualiza a UI com os dados do cliente
                customer?.let {
                    scheduleItemBinding.tvPetName.setText(it.namePet)
                    scheduleItemBinding.root.setOnClickListener { onItemClickListener(customer) }
                }
            }

            Glide.with(scheduleItemBinding.root.context)
                .load(schedule.img)
                .centerCrop()
                .error(R.drawable.bath)
                .into(scheduleItemBinding.ivTypeWork)

            scheduleItemBinding.tvTime.setText(schedule.time)
            scheduleItemBinding.tvValue.setText(schedule.value.toString())


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = ScheduleItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return schedules.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val schedule = schedules[position]
        holder.bind(schedule)
    }


    suspend fun getCustomer(customerId: Int): Pet? {
        return withContext(Dispatchers.IO) {
            // Busca o cliente na thread IO
            petDao.findById(customerId)
        }
    }

}