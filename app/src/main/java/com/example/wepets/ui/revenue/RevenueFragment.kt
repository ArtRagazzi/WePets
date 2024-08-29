package com.example.wepets.ui.revenue

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.wepets.databinding.FragmentRevenueBinding
import com.example.wepets.db.dao.PetDao
import com.example.wepets.db.dao.RevenueDao
import com.example.wepets.db.database.WePetsDatabase
import com.example.wepets.ui.contact.NewPetActivity
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class RevenueFragment : Fragment() {

    private lateinit var binding: FragmentRevenueBinding
    private lateinit var todayDate:String

    // Instancias DB
    private lateinit var db: WePetsDatabase
    private lateinit var revenueDao: RevenueDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRevenueBinding.inflate(inflater, container,false)
        //DB
        //Garente que o contexto Existe(Cuidado)
        db = context?.let { WePetsDatabase.getDatabase(it) }!!
        revenueDao = db.getRevenueDao


        binding.fabAdd.setOnClickListener {
            startActivity(Intent(context, NewRevenueActivity::class.java))
        }



        binding.ibLeft.setOnClickListener {
            todayDate
        }
        binding.ibRight.setOnClickListener {

        }




        return binding.root
    }

    override fun onStart() {
        super.onStart()
        todayDate = getTodayDate()
    }


    fun getTodayDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())  // Define o formato desejado
        val date = Date()  // Obtém a data atual
        return dateFormat.format(date)  // Formata a data e retorna como String
    }
}


