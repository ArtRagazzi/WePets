package com.example.wepets.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.wepets.R
import com.example.wepets.databinding.ActivityUpdatePetBinding
import com.example.wepets.db.dao.PetDao
import com.example.wepets.db.database.PetDatabase
import com.example.wepets.model.Pet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdatePetActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityUpdatePetBinding.inflate(layoutInflater)
    }

    // Instancias DB
    private val db by lazy {
        PetDatabase.getDatabase(applicationContext)
    }
    private val petDao: PetDao by lazy {
        db.getPetDao
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

        val customer: Pet? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("customer", Pet::class.java)
        } else {
            intent.getParcelableExtra("customer")
        }

        if(customer!=null){
            Log.i("infoCustomer", "$customer")
            setupItens(customer)
            binding.btnUpdate.setOnClickListener {
                updateCustomer(customer)
                Toast.makeText(this,"Updated customer", Toast.LENGTH_SHORT).show()

                finish()
            }
        }else{
            finish()
        }
    }

    private fun setupItens(customer: Pet) {

        binding.tiImageUrl.setText(customer.photoUrl)
        binding.tiPetName.setText(customer.namePet)
        binding.tiPetBreed.setText(customer.breed)
        when(customer.size){
            "Pequeno" -> binding.rbSmall.isChecked = true
            "Medio" -> binding.rbMedium.isChecked = true
            "Grande" -> binding.rbBig.isChecked = true
        }
        when(customer.sexPet){
            "Femea" -> binding.rbFemale.isChecked = true
            "Macho" -> binding.rbMale.isChecked = true
        }
        binding.tiOwnerName.setText(customer.ownerName)
        binding.tiOwnerPhone.setText(customer.phoneNumber)

    }
    fun updateCustomer(customer: Pet){
        CoroutineScope(Dispatchers.IO).launch {
            petDao.update(customer)
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
        supportActionBar?.title = "Customer Update"
    }
}