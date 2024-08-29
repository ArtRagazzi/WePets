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
import com.example.wepets.ui.contact.UpdatePetActivity
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RevenueFragment : Fragment() {

    private lateinit var binding: FragmentRevenueBinding
    private lateinit var myDate:String

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
            val intent = Intent(context, NewRevenueActivity::class.java).apply {
                putExtra("date", myDate)
            }
            startActivity(intent)
        }



        binding.ibLeft.setOnClickListener {
            myDate = getDateMinusOneMonth(myDate)
            updateDate()
        }
        binding.ibRight.setOnClickListener {
            myDate = getDatePlusOneMonth(myDate)
            updateDate()
        }




        return binding.root
    }

    override fun onStart() {
        super.onStart()
        myDate = getTodayDate()
        updateDate()
    }


    fun getTodayDate(): String {
        val dateFormat = SimpleDateFormat("MM/yyyy", Locale.getDefault())  // Define o formato desejado
        val date = Date()  // Obtém a data atual
        return dateFormat.format(date)  // Formata a data e retorna como String
    }

    fun getDateMinusOneMonth(dateString: String): String {
        val dateFormat = SimpleDateFormat("MM/yyyy", Locale.getDefault())  // Define o formato esperado da data de entrada

        // Converte a string para um objeto Date
        val date = dateFormat.parse(dateString) ?: throw IllegalArgumentException("Invalid Date")

        val calendar = Calendar.getInstance()  // Obtém uma instância do Calendar
        calendar.time = date  // Define a data do Calendar para a data fornecida

        calendar.add(Calendar.MONTH, -1)  // Diminui um mês da data

        // Retorna a nova data formatada como String
        return dateFormat.format(calendar.time)
    }
    fun getDatePlusOneMonth(dateString: String): String {
        val dateFormat = SimpleDateFormat("MM/yyyy", Locale.getDefault())  // Define o formato esperado da data de entrada

        // Converte a string para um objeto Date
        val date = dateFormat.parse(dateString) ?: throw IllegalArgumentException("Invalid Date")

        val calendar = Calendar.getInstance()  // Obtém uma instância do Calendar
        calendar.time = date  // Define a data do Calendar para a data fornecida

        calendar.add(Calendar.MONTH, +1)  // Diminui um mês da data

        // Retorna a nova data formatada como String
        return dateFormat.format(calendar.time)
    }

    fun updateDate(){
        binding.tvDate.setText(myDate)
        //ReciclerView
    }
}


