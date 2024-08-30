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
import com.example.wepets.db.dao.ScheduleDao
import com.example.wepets.db.database.WePetsDatabase
import com.example.wepets.model.Pet
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

    // Instancias DB
    private val db by lazy {
        WePetsDatabase.getDatabase(applicationContext)
    }
    private val scheduleDao: ScheduleDao by lazy {
        db.getScheduleDao
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
                val newSchedule = Schedule(null,idCustomer!!.toInt(),date!!,time,typeWork,value.toDouble())
                insertSchedule(newSchedule)
            }
        }

        binding.tvSelectTime.setOnClickListener {
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
            typeWork = "Bath"
        } else if (binding.rbCut.isChecked) {
            typeWork = "Cut"
        } else if (binding.rbSame.isChecked) {
            typeWork = "Same"
        }else{
            typeWork = "Bath"
        }

        if (price.isNotEmpty()) {
            return true
        } else {
            //Para tirar a mensagem de erro quando digitar novamente
            binding.tfItemValue.error = null

            if (price.isEmpty()) {
                binding.tfItemValue.error = "Value Cannot be Empty"
            }
            return false
        }
    }

    private fun insertSchedule(schedule: Schedule) {

        CoroutineScope(Dispatchers.IO).launch {
            scheduleDao.insert(schedule)
        }
        Toast.makeText(this, "Scheduled", Toast.LENGTH_SHORT).show()
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
        supportActionBar?.title = "Schedule Information"
    }
}