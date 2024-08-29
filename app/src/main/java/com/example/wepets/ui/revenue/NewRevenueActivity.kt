package com.example.wepets.ui.revenue

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.wepets.R
import com.example.wepets.databinding.ActivityNewRevenueBinding
import com.example.wepets.db.dao.PetDao
import com.example.wepets.db.dao.RevenueDao
import com.example.wepets.db.database.WePetsDatabase
import com.example.wepets.model.Pet
import com.example.wepets.model.Revenue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class NewRevenueActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityNewRevenueBinding.inflate(layoutInflater)
    }

    // Instancias DB
    private val db by lazy {
        WePetsDatabase.getDatabase(applicationContext)
    }
    private val revenueDao: RevenueDao by lazy {
        db.getRevenueDao
    }

    private lateinit var itemName:String
    private lateinit var itemValue:String
    private lateinit var itemType:String
    private lateinit var itemDate: String
    private lateinit var itemImage: String





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

        binding.btnInsert.setOnClickListener {
            if(checkFields()){
                insertRevenueItem()
            }
        }


    }

    fun checkFields():Boolean{

        itemName = binding.tiNameItem.text.toString()
        itemValue = binding.tiItemValue.text.toString()
        if (binding.rbIncrease.isChecked) {
            itemType = "Increase"
            itemImage = R.drawable.ic_increase.toString()
        } else if (binding.rbDecrease.isChecked) {
            itemType = "Decrease"
            itemImage = R.drawable.ic_decrease.toString()
        } else {
            itemType = "Increase"
            itemImage = R.drawable.ic_increase.toString()
        }
        if(intent.getStringExtra("date") != null){
            itemDate = intent.getStringExtra("date").toString()
        }else{
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("MM/yyyy", Locale.getDefault())
            itemDate = dateFormat.format(calendar.time)
        }
        if (itemName.isNotEmpty() && itemValue.isNotEmpty()) {
            return true
        }else{
            //Para tirar a mensagem de erro quando digitar novamente
            binding.tfNameItem.error = null
            binding.tfItemValue.error = null

            if (itemName.isEmpty()) {
                binding.tfNameItem.error = "Name Cannot be Empty"
            }
            if (itemValue.isEmpty()) {
                binding.tfItemValue.error = "Value Cannot be Empty"
            }
            return false
        }
    }

    private fun insertRevenueItem() {
        val newItem = Revenue(
            null,
            name = itemName,
            value = itemValue.toDouble(),
            date = itemDate,
            type = itemType,
            image = itemImage.toInt()
        )
        CoroutineScope(Dispatchers.IO).launch {
            revenueDao.insert(newItem)
        }
        Toast.makeText(this, "Insert OK", Toast.LENGTH_SHORT).show()
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
        supportActionBar?.title = "Add New Item Revenue"
    }
}