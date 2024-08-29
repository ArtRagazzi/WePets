package com.example.wepets.ui.revenue

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.wepets.R
import com.example.wepets.databinding.ActivityNewRevenueBinding
import com.example.wepets.db.dao.PetDao
import com.example.wepets.db.dao.RevenueDao
import com.example.wepets.db.database.WePetsDatabase
import java.util.Date

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
    private lateinit var itemDate: Date




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

        }


    }

    fun checkFields():Boolean{

        itemName = binding.tiNameItem.text.toString()
        itemValue = binding.tiItemValue.text.toString()
        if (binding.rbIncrease.isChecked) {
            itemType = "Increase"
        } else if (binding.rbDecrease.isChecked) {
            itemType = "Decrease"
        } else {
            itemType = "Increase"
        }

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