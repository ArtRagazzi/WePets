package com.example.wepets.ui.scheduling

import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.wepets.R
import com.example.wepets.databinding.ActivityNewSchedulePt2Binding
import com.example.wepets.db.dao.PetDao
import com.example.wepets.db.dao.RevenueDao
import com.example.wepets.db.dao.ScheduleDao
import com.example.wepets.db.database.WePetsDatabase
import com.example.wepets.model.Pet
import com.example.wepets.model.Revenue
import com.example.wepets.model.Schedule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class NewSchedulePt2Activity : AppCompatActivity() {
    private val binding by lazy {
        ActivityNewSchedulePt2Binding.inflate(layoutInflater)
    }
    private lateinit var typeWork:String
    private lateinit var time:String
    private lateinit var imgTypeWork:String

    // Instancias DB
    private val db by lazy {
        WePetsDatabase.getDatabase(applicationContext)
    }

    private val petDao: PetDao by lazy {
        db.getPetDao
    }
    private val scheduleDao: ScheduleDao by lazy {
        db.getScheduleDao
    }
    private val revenueDao: RevenueDao by lazy {
        db.getRevenueDao
    }







    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupToolbar()


        binding.btnSave.setOnClickListener {

            if(checkFields()){
                val idCustomer = intent.getStringExtra("customerId")
                val date = intent.getStringExtra("date")
                val value = binding.tiItemValue.text.toString()
                val newSchedule = Schedule(null,idCustomer!!.toInt(),date!!,time,typeWork,imgTypeWork.toInt(),value.toDouble())

                insertRevenue(idCustomer.toInt(), date, value)
                insertSchedule(newSchedule)
            }
        }

        binding.btnSelectTime.setOnClickListener {
            showTimePicker()
        }



    }


    private fun showTimePicker(){
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener{timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY,hour)
            cal.set(Calendar.MINUTE,minute)
            binding.tvTime.text = SimpleDateFormat("HH:mm").format(cal.time)

        }
        TimePickerDialog(this,
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun checkFields(): Boolean {

        val price = binding.tiItemValue.text.toString()

        time = binding.tvTime.text.toString()
        if(time.isEmpty()){
            time = "12:00"
        }


        if (binding.rbBath.isChecked) {
            typeWork = "Banho"
            imgTypeWork = R.drawable.bath.toString()
        } else if (binding.rbCut.isChecked) {
            typeWork = "Tosa"
            imgTypeWork = R.drawable.scissor.toString()
        } else if (binding.rbSame.isChecked) {
            typeWork = "Banho e Tosa"
            imgTypeWork=R.drawable.bathcut.toString()
        }else{
            typeWork = "Banho"
            imgTypeWork = R.drawable.bath.toString()
        }

        if (price.isNotEmpty()) {
            return true
        } else {
            //Para tirar a mensagem de erro quando digitar novamente
            binding.tfItemValue.error = null

            if (price.isEmpty()) {
                binding.tfItemValue.error = "Valor não pode estar vazio"
            }
            return false
        }
    }
    fun convertDate(dateString: String): String {
        // Define o formato de entrada e saída
        val inputFormat = SimpleDateFormat("dd/MM/yyyy")
        val outputFormat = SimpleDateFormat("MM/yyyy")

        // Converte a string para uma data
        val date = inputFormat.parse(dateString)

        // Retorna a data no formato desejado
        return outputFormat.format(date!!)
    }

    private fun insertRevenue(customerId: Int, date: String, value: String) {
        CoroutineScope(Dispatchers.IO).launch {
            // Busca o cliente na thread IO
            val customer = petDao.findById(customerId)
            val image = R.drawable.ic_increase.toString()

            // Cria o novo item de receita
            val newRevenueItem = Revenue(
                null,
                name = "$typeWork ${customer.namePet}",
                type = "Increase",
                date = convertDate(date),
                value = value.toDouble(),
                image = image.toInt()
            )

            // Insere a nova receita no banco de dados
            revenueDao.insert(newRevenueItem)

            // Mostra um Toast na thread principal
            runOnUiThread {
                Toast.makeText(this@NewSchedulePt2Activity, "Item adicionado com Sucesso", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun insertSchedule(schedule: Schedule) {

        CoroutineScope(Dispatchers.IO).launch {
            scheduleDao.insert(schedule)
        }
        Toast.makeText(this, "Agendado", Toast.LENGTH_SHORT).show()
        finish()
    }

    fun setupToolbar(){
        // Configurar a Toolbar como a ActionBar da Activity
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Habilitar a seta para voltar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Definir o título da Activity na Toolbar
        supportActionBar?.title = "Informação do Agendamento"
    }
}