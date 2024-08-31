package com.example.wepets.ui.revenue

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wepets.adapter.CustomerAdapter
import com.example.wepets.adapter.RevenueAdapter
import com.example.wepets.databinding.FragmentRevenueBinding
import com.example.wepets.db.dao.PetDao
import com.example.wepets.db.dao.RevenueDao
import com.example.wepets.db.database.WePetsDatabase
import com.example.wepets.model.Pet
import com.example.wepets.model.Revenue
import com.example.wepets.ui.contact.CustomerActivity
import com.example.wepets.ui.contact.NewPetActivity
import com.example.wepets.ui.contact.UpdatePetActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RevenueFragment : Fragment() {

    private lateinit var binding: FragmentRevenueBinding
    private lateinit var myDate: String
    private lateinit var adapter: RevenueAdapter

    // Instancias DB
    private lateinit var db: WePetsDatabase
    private lateinit var revenueDao: RevenueDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRevenueBinding.inflate(inflater, container, false)
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

        //Configurando RecyclerView
        adapter = RevenueAdapter { itemRevenue ->
            deleteRevenueDialog(itemRevenue)

        }
        binding.rvRevenue.adapter = adapter
        binding.rvRevenue.layoutManager = LinearLayoutManager(context)
        binding.rvRevenue.addItemDecoration(
            DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        )





        return binding.root
    }

    override fun onStart() {
        super.onStart()
        myDate = getTodayDate()
        updateDate()
    }


    fun getTodayDate(): String {
        val dateFormat =
            SimpleDateFormat("MM/yyyy", Locale.getDefault())  // Define o formato desejado
        val date = Date()  // Obtém a data atual
        return dateFormat.format(date)  // Formata a data e retorna como String
    }

    fun getDateMinusOneMonth(dateString: String): String {
        val dateFormat = SimpleDateFormat(
            "MM/yyyy",
            Locale.getDefault()
        )  // Define o formato esperado da data de entrada

        // Converte a string para um objeto Date
        val date = dateFormat.parse(dateString) ?: throw IllegalArgumentException("Invalid Date")

        val calendar = Calendar.getInstance()  // Obtém uma instância do Calendar
        calendar.time = date  // Define a data do Calendar para a data fornecida

        calendar.add(Calendar.MONTH, -1)  // Diminui um mês da data

        // Retorna a nova data formatada como String
        return dateFormat.format(calendar.time)
    }

    fun getDatePlusOneMonth(dateString: String): String {
        val dateFormat = SimpleDateFormat(
            "MM/yyyy",
            Locale.getDefault()
        )  // Define o formato esperado da data de entrada

        // Converte a string para um objeto Date
        val date = dateFormat.parse(dateString) ?: throw IllegalArgumentException("Invalid Date")

        val calendar = Calendar.getInstance()  // Obtém uma instância do Calendar
        calendar.time = date  // Define a data do Calendar para a data fornecida

        calendar.add(Calendar.MONTH, +1)  // Diminui um mês da data

        // Retorna a nova data formatada como String
        return dateFormat.format(calendar.time)
    }

    fun calculateTotalRevenueMonth(dateChoose:String){
        // Define o formato da data
        val dateFormat = SimpleDateFormat("MM/yyyy", Locale.getDefault())
        // Parse a data fornecida
        val date = dateFormat.parse(dateChoose) ?: return

        // Cria uma instância de Calendar
        val calendar = Calendar.getInstance()
        calendar.time = date

        // Recyclerview
        CoroutineScope(Dispatchers.IO).launch {
            val dateList = revenueDao.findByMonth(
                startDate = dateChoose,
                endDate = dateChoose
            )
            var sum = 0.0
            dateList.forEach { cadaRevenue->
                if(cadaRevenue.type == "Increase"){
                    sum+=cadaRevenue.value
                }else{
                    sum-=cadaRevenue.value
                }
            }
            withContext(Dispatchers.Main) {
                binding.tvTotal.setText(sum.toString())
            }
        }

    }

    fun updateDate() {
        // Define o formato da data
        val dateFormat = SimpleDateFormat("MM/yyyy", Locale.getDefault())
        // Parse a data fornecida
        val date = dateFormat.parse(myDate) ?: return

        // Cria uma instância de Calendar
        val calendar = Calendar.getInstance()
        calendar.time = date

        // Define o primeiro dia do mês
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val startDate = dateFormat.format(calendar.time)

        // Define o último dia do mês
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        val endDate = dateFormat.format(calendar.time)

        // Atualiza o TextView com a data
        binding.tvDate.setText(myDate)
        calculateTotalRevenueMonth(myDate)
        // Recyclerview
        CoroutineScope(Dispatchers.IO).launch {
            val revenueList = revenueDao.findByMonth(
                startDate = startDate,
                endDate = endDate
            )

            withContext(Dispatchers.Main) {
                adapter.addOnList(revenueList)
            }
        }
    }


    private fun deleteRevenueDialog(revenue: Revenue) {

        context?.let {
            AlertDialog.Builder(it)
                .setTitle("Delete Item")
                .setMessage("Do you really want to delete this revenue item?")
                .setPositiveButton("Yes") { dialog, position ->
                    deleteRevenueItem(revenue)
                }
                .setNegativeButton("No") { dialog, position ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }

    private fun deleteRevenueItem(revenue: Revenue) {
        CoroutineScope(Dispatchers.IO).launch {
            revenueDao.delete(revenue)
            withContext(Dispatchers.Main){
                updateDate()
            }
        }

    }

}


