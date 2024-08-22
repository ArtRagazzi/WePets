package com.example.wepets.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.wepets.R
import com.example.wepets.databinding.ActivityCustomerBinding
import com.example.wepets.db.dao.PetDao
import com.example.wepets.db.database.PetDatabase
import com.example.wepets.model.Pet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CustomerActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCustomerBinding.inflate(layoutInflater)
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

        val customer:Pet? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("customer", Pet::class.java)
        } else {
            intent.getParcelableExtra("customer")
        }

        if(customer!=null){
            setupItens(customer)

            binding.btnDelete.setOnClickListener {
                deleteCustomerDialog(customer)

            }
            binding.btnUpdate.setOnClickListener {
                val intent = Intent(this, UpdatePetActivity::class.java).apply {
                    putExtra("customer", customer)
                }
                startActivity(intent)
            }

        }else{
            Toast.makeText(this,"Error, not possible access data",Toast.LENGTH_SHORT).show()
            finish()
        }







    }


    fun setupItens(customer:Pet){
        Glide.with(this)
            .load(customer.photoUrl)
            .error(R.drawable.dogdefault)
            .centerCrop()
            .into(binding.ivProfile)
        binding.txtPetName.setText(customer.namePet)
        binding.txtPetSize.setText(customer.size)
        binding.txtPetBreed.setText(customer.breed)
        binding.txtOwnerName.setText(customer.ownerName)
        binding.txtOwnerPhone.setText(customer.phoneNumber)

        Glide.with(this)
            .load(getGender(customer.sexPet))
            .error(R.drawable.ic_female)
            .centerCrop()
            .into(binding.ivPetSex)
    }


    fun setupToolbar(){
        // Configurar a Toolbar como a ActionBar da Activity
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Habilitar a seta para voltar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Definir o título da Activity na Toolbar
        supportActionBar?.title = "Customer Information"
    }
    fun getGender(gender:String): Int {
        if(gender == "Macho"){
            return R.drawable.ic_male
        }else{
            return R.drawable.ic_female
        }
    }

    private fun deleteCustomerDialog(customer: Pet){
        AlertDialog.Builder(this)
            .setTitle("Delete Customer")
            .setMessage("Do you really want to delete this custumer?")
            .setPositiveButton("Yes"){dialog, position->
                deleteCustomer(customer)
            }
            .setNegativeButton("No"){dialog,position->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun deleteCustomer(customer: Pet){
        CoroutineScope(Dispatchers.IO).launch {
            petDao.delete(customer)
        }
        finish()
    }


}