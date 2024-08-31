package com.example.wepets.ui.scheduling

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wepets.adapter.RevenueAdapter
import com.example.wepets.adapter.ScheduleAdapter
import com.example.wepets.databinding.FragmentSchedulingBinding
import com.example.wepets.db.dao.RevenueDao
import com.example.wepets.db.dao.ScheduleDao
import com.example.wepets.db.database.WePetsDatabase
import com.example.wepets.model.Revenue
import com.example.wepets.model.Schedule
import com.example.wepets.ui.contact.CustomerActivity
import com.example.wepets.ui.revenue.NewRevenueActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SchedulingFragment : Fragment() {

    private lateinit var binding: FragmentSchedulingBinding

    private val calendar = Calendar.getInstance()
    private lateinit var adapter: ScheduleAdapter

    // Instancias DB
    private lateinit var db: WePetsDatabase
    private lateinit var scheduleDao: ScheduleDao



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSchedulingBinding.inflate(inflater, container,false)


        val context = requireContext() // Use requireContext() para garantir que o contexto não seja null
        db = WePetsDatabase.getDatabase(context)
        scheduleDao = db.getScheduleDao

        binding.fabAdd.setOnClickListener {
            val intent = Intent(context, NewScheduleActivity::class.java).apply {
                putExtra("date", binding.tvDate.text)
            }
            startActivity(intent)
        }

        binding.tvDate.setOnClickListener {
            showDatePicker()
        }

        // Configurando RecyclerView
        adapter = ScheduleAdapter({ customer ->
            val intent = Intent(context, CustomerActivity::class.java).apply {
                putExtra("customer", customer)
            }
            startActivity(intent)

        },{schedule -> deleteScheduleDialog(schedule) },context)

        binding.rvRevenue.adapter = adapter
        binding.rvRevenue.layoutManager = LinearLayoutManager(context)
        binding.rvRevenue.addItemDecoration(
            DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        )




        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setupTodayDate()
        setupScheduleDate(binding.tvDate.text.toString())
    }

    fun setupTodayDate(): Unit {
        val dateFormat =
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())  // Define o formato desejado
        val date = Date()  // Obtém a data atual
        binding.tvDate.setText( dateFormat.format(date))  // Formata a data e retorna como String
    }

    fun showDatePicker() {
        val datePickerDialog = context?.let {
            DatePickerDialog(
                it,
                { datePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year, monthOfYear, dayOfMonth)
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val formattedDate = dateFormat.format(selectedDate.time)
                    binding.tvDate.text = formattedDate
                    setupScheduleDate(binding.tvDate.text.toString())
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        }
        datePickerDialog?.show()
    }

    fun setupScheduleDate(myDate:String){
        // Define o formato da data
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        // Parse a data fornecida
        val date = dateFormat.parse(myDate) ?: return

        // Cria uma instância de Calendar
        val calendar = java.util.Calendar.getInstance()
        calendar.time = date

        // Atualiza o TextView com a data
        binding.tvDate.setText(myDate)

        // Recyclerview
        CoroutineScope(Dispatchers.IO).launch {
            val scheduleList = scheduleDao.findByDay(
                startDate = myDate,
                endDate = myDate
            )

            withContext(Dispatchers.Main) {
                adapter.addOnList(scheduleList)
            }
        }
    }

    private fun deleteScheduleDialog(schedule: Schedule) {

        context?.let {
            AlertDialog.Builder(it)
                .setTitle("Apagar Item")
                .setMessage("Desejar realmente apagar esse item?")
                .setPositiveButton("Sim") { dialog, position ->
                    deleteScheduleItem(schedule)
                }
                .setNegativeButton("Não") { dialog, position ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }

    private fun deleteScheduleItem(schedule: Schedule) {
        CoroutineScope(Dispatchers.IO).launch {
            scheduleDao.delete(schedule)
            withContext(Dispatchers.Main){
                setupScheduleDate(binding.tvDate.text.toString())
            }
        }

    }


}
