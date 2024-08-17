package com.example.wepets.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.wepets.R
import com.example.wepets.databinding.ActivityNewPetBinding
import com.example.wepets.db.dao.PetDao
import com.example.wepets.db.database.PetDatabase
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
        PetDatabase.getDatabase(applicationContext)
    }
    private val petDao: PetDao by lazy {
        db.getPetDao
    }

    private lateinit var urlImage: String
    private lateinit var petName: String
    private lateinit var petBreed: String
    private lateinit var petSize: String
    private lateinit var petSex: String
    private lateinit var ownerName: String
    private lateinit var ownerPhone: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnInsert.setOnClickListener {
            if(checkFields()){
                insertPet()
            }

        }

        binding.fabBack.setOnClickListener {
            finish()
        }
    }



    private fun insertPet() {
        val newPet = Pet(
            null,
            petName,
            petSize,
            petBreed,
            petSex,
            ownerName,
            ownerPhone,
            binding.tiImageUrl.toString()
        )
        CoroutineScope(Dispatchers.IO).launch {
            petDao.insert(newPet)
        }
        Toast.makeText(this, "Insert OK", Toast.LENGTH_SHORT).show()
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


        if (petName.isNotEmpty() && petBreed.isNotEmpty() && ownerName.isNotEmpty() && ownerPhone.isNotEmpty()) {
            return true
        } else {
            //Para tirar a mensagem de erro quando digitar novamente
            binding.tfPetName.error = null
            binding.tfPetBreed.error = null
            binding.tfOwnerName.error = null
            binding.tfOwnerPhone.error = null

            if (petName.isEmpty()) {
                binding.tfPetName.error = "Name Cannot be Empty"
            }
            if (petBreed.isEmpty()) {
                binding.tfPetBreed.error = "Pet Breed Cannot be Empty"
            }
            if (ownerName.isEmpty()) {
                binding.tfOwnerName.error = "Owner Name Cannot be Empty"
            }
            if (ownerPhone.isEmpty()) {
                binding.tfOwnerPhone.error = "Owner Phone Cannot be Empty"
            }
            return false
        }
    }
}