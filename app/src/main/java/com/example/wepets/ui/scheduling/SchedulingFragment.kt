package com.example.wepets.ui.scheduling

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.wepets.adapter.RevenueAdapter
import com.example.wepets.databinding.FragmentSchedulingBinding
import com.example.wepets.db.dao.RevenueDao
import com.example.wepets.db.dao.ScheduleDao
import com.example.wepets.db.database.WePetsDatabase
import com.example.wepets.ui.revenue.NewRevenueActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SchedulingFragment : Fragment() {

    private lateinit var binding: FragmentSchedulingBinding

    private val calendar = Calendar.getInstance()


    // Instancias DB
    private lateinit var db: WePetsDatabase
    private lateinit var scheduleDao: ScheduleDao



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSchedulingBinding.inflate(inflater, container,false)

        //Garente que o contexto Existe(Cuidado)
        db = context?.let { WePetsDatabase.getDatabase(it) }!!
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


        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setupTodayDate()
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
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        }
        datePickerDialog?.show()
    }


}
