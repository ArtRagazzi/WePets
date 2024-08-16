package com.example.wepets.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.wepets.R
import com.example.wepets.databinding.ActivityNewPetBinding

class NewPetActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityNewPetBinding.inflate(layoutInflater)
    }

    private lateinit var urlImage:String?
    private lateinit var petName:String
    private lateinit var petBreed:String
    private lateinit var petSize:String
    private lateinit var petSex:String
    private lateinit var ownerName:String
    private lateinit var ownerPhone:String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }






    private fun checkFields(): Boolean {
        petName = binding.tiPetName.text.toString()
        petBreed = binding.tiPetBreed.text.toString()


        if(binding.rbSmall.isChecked){
            petSize = "Pequeno"
        }else if(binding.rbMedium.isChecked){
            petSize = "Medio"
        }else if(binding.rbBig.isChecked){
            petSize = "Grande"
        }else{
            petSize = ""
        }

        if(binding.rbFemale.isChecked){
            petSex = "Femea"
        }else if(binding.rbMale.isChecked){
            petSex = "Macho"
        }else{
            petSex = ""
        }

        ownerName = binding.tiOwnerName.toString()
        ownerPhone = binding.tiOwnerPhone.toString()




        if(petName.isNotEmpty() && petBreed.isNotEmpty() && ownerName.isNotEmpty() && ownerPhone.isNotEmpty() && petSize != "" && petSex != ""){
            return true
        }else{
            //Para tirar a mensagem de erro quando digitar novamente
            binding.tfNome.error=null
            binding.tfEmail.error=null
            binding.tfSenha.error=null
            if(nome.isEmpty()){
                binding.tfNome.error="Preencha seu Nome!"
            }
            if(email.isEmpty()){
                binding.tfEmail.error="Preencha seu Email!"
            }
            if(senha.isEmpty()){
                binding.tfSenha.error="Preencha sua Senha!"
            }
            return false
        }
    }
}