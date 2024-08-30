package com.example.wepets.ui.scheduling

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wepets.R
import com.example.wepets.adapter.CustomerAdapter
import com.example.wepets.databinding.ActivityNewScheduleBinding
import com.example.wepets.db.dao.PetDao
import com.example.wepets.db.database.WePetsDatabase
import com.example.wepets.ui.contact.CustomerActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewScheduleActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityNewScheduleBinding.inflate(layoutInflater)
    }
    private lateinit var adapter: CustomerAdapter

    // Instancias DB
    private lateinit var db: WePetsDatabase
    private lateinit var petDao: PetDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        db = this?.let { WePetsDatabase.getDatabase(it) }!!
        petDao = db.getPetDao

        setupToolbar()


        // Configurando RecyclerView
        adapter = CustomerAdapter(onItemClickListener = { customer ->
            val date = intent.getStringExtra("date") ?: "" // Obtém a data, se disponível
            val intent = Intent(this, NewSchedulePt2Activity::class.java).apply {
                putExtra("customerId", customer.id.toString())
                putExtra("date", date) // Passa a data para a próxima Activity
            }
            startActivity(intent)
            finish()
        })
        binding.rvCustomer.adapter = adapter
        binding.rvCustomer.layoutManager = LinearLayoutManager(this)
        binding.rvCustomer.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )

        binding.btnSeach.setOnClickListener{
            searchByName()
        }


    }


    override fun onStart() {
        super.onStart()
        loadCustomerList()
    }
    private fun loadCustomerList(){
        CoroutineScope(Dispatchers.IO).launch {
            val customerList = petDao.findAll()

            withContext(Dispatchers.Main) {
                adapter.addOnList(customerList)
            }
        }
    }

    private fun searchByName(){
        val name = binding.tiCustomerSearch.text.toString().lowercase()
        CoroutineScope(Dispatchers.IO).launch {
            val customerListFilterByName = petDao.findByName(name)

            withContext(Dispatchers.Main) {
                adapter.addOnList(customerListFilterByName)
            }
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
        supportActionBar?.title = "Select the Customer"
    }
}