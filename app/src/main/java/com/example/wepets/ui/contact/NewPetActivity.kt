package com.example.wepets.ui.contact

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.wepets.R
import com.example.wepets.databinding.ActivityNewPetBinding
import com.example.wepets.db.dao.PetDao
import com.example.wepets.db.database.WePetsDatabase
import com.example.wepets.model.Pet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewPetActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityNewPetBinding.inflate(layoutInflater)
    }

    // Instancias DB
    private val db by lazy {
        WePetsDatabase.getDatabase(applicationContext)
    }
    private val petDao: PetDao by lazy {
        db.getPetDao
    }

    private lateinit var imgUrl:String
    private lateinit var petName: String
    private lateinit var petBreed: String
    private lateinit var petSize: String
    private lateinit var petSex: String
    private lateinit var ownerName: String
    private lateinit var ownerPhone: String
    private lateinit var notes:String



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
                insertPet()
            }

        }


    }



    private fun insertPet() {
        val newPet = Pet(
            null,
            namePet = petName.lowercase(),
            size = petSize,
            breed = petBreed,
            sexPet = petSex,
            ownerName = ownerName.lowercase(),
            phoneNumber = ownerPhone,
            photoUrl = imgUrl,
            notes = notes
        )
        CoroutineScope(Dispatchers.IO).launch {
            petDao.insert(newPet)
        }
        Toast.makeText(this, "Inserido com Sucesso", Toast.LENGTH_SHORT).show()
        finish()
    }
    private fun checkFields(): Boolean {
        petName = binding.tiPetName.text.toString()
        petBreed = binding.tiPetBreed.text.toString()
        ownerName = binding.tiOwnerName.text.toString()
        ownerPhone = binding.tiOwnerPhone.text.toString()


        if (binding.rbSmall.isChecked) {
            petSize = "Pequeno"
        } else if (binding.rbMedium.isChecked) {
            petSize = "Medio"
        } else {
            petSize = "Grande"
        }

        if (binding.rbFemale.isChecked) {
            petSex = "Femea"
        } else {
            petSex = "Macho"
        }
        if(binding.tiNotes.text.isEmpty()){
            notes = ""
        }else{
            notes = binding.tiNotes.text.toString()
        }
        if(binding.tiImageUrl.text.isEmpty()){
            imgUrl = ""
        }else{
            imgUrl = binding.tiImageUrl.text.toString()
        }

        if (petName.isNotEmpty() && petBreed.isNotEmpty() && ownerName.isNotEmpty() && ownerPhone.isNotEmpty()) {
            return true
        } else {
            //Para tirar a mensagem de erro quando digitar novamente
            binding.tfPetName.error = null
            binding.tfPetBreed.error = null
            binding.tfOwnerName.error = null
            binding.tfOwnerPhone.error = null

            if (petName.isEmpty()) {
                binding.tfPetName.error = "Nome não pode estar vazio"
            }
            if (petBreed.isEmpty()) {
                binding.tfPetBreed.error = "Raça não pode estar vazio"
            }
            if (ownerName.isEmpty()) {
                binding.tfOwnerName.error = "Nome do Dono não pode estar vazio"
            }
            if (ownerPhone.isEmpty()) {
                binding.tfOwnerPhone.error = "Telefone não pode estar vazio"
            }
            return false
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
        supportActionBar?.title = "Registrar novo Pet"
    }
}