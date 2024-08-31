package com.example.wepets.ui.contact

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.wepets.R
import com.example.wepets.databinding.ActivityUpdatePetBinding
import com.example.wepets.db.dao.PetDao
import com.example.wepets.db.database.WePetsDatabase
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
        WePetsDatabase.getDatabase(applicationContext)
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

        if (customer != null) {
            setupItens(customer)

            binding.btnUpdate.setOnClickListener {
                updateCustomer(customer.id!!)
                Toast.makeText(this, "Atualizado com Sucesso", Toast.LENGTH_SHORT).show()

                finish()
            }
        } else {
            finish()
        }
    }

    private fun setupItens(customer: Pet) {

        binding.tiImageUrl.setText(customer.photoUrl)
        binding.tiPetName.setText(customer.namePet)
        binding.tiPetBreed.setText(customer.breed)
        when (customer.size) {
            "Pequeno" -> binding.rbSmall.isChecked = true
            "Medio" -> binding.rbMedium.isChecked = true
            "Grande" -> binding.rbBig.isChecked = true
            else -> customer.size
        }
        when (customer.sexPet) {
            "Femea" -> binding.rbFemale.isChecked = true
            "Macho" -> binding.rbMale.isChecked = true
            else -> customer.sexPet
        }
        binding.tiOwnerName.setText(customer.ownerName)
        binding.tiOwnerPhone.setText(customer.phoneNumber)
        binding.tiNotes.setText(customer.notes)

    }

    fun updateCustomer(oldCustomerId:Int) {

        //Criando Dados para Update
        val petName = binding.tiPetName.text.toString().lowercase()
        val petBreed = binding.tiPetBreed.text.toString()
        val ownerName = binding.tiOwnerName.text.toString().lowercase()
        val ownerPhone = binding.tiOwnerPhone.text.toString()
        val petUrl = binding.tiImageUrl.text.toString()
        val petSize:String
        if (binding.rbSmall.isChecked) {
            petSize = "Pequeno"
        } else if (binding.rbMedium.isChecked) {
            petSize = "Medio"
        } else {
            petSize = "Grande"
        }
        val petSex:String
        if (binding.rbFemale.isChecked) {
            petSex = "Femea"
        } else {
            petSex = "Macho"
        }
        val petNote = binding.tiNotes.text.toString()

        val customerToUpdate = Pet(oldCustomerId,petName,petSize,petBreed,petSex,ownerName,ownerPhone,petUrl,petNote)

        CoroutineScope(Dispatchers.IO).launch {
            petDao.update(customerToUpdate)
        }

    }


    fun setupToolbar() {
        // Configurar a Toolbar como a ActionBar da Activity
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Habilitar a seta para voltar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Definir o título da Activity na Toolbar
        supportActionBar?.title = "Atualizar Cliente"
    }
}