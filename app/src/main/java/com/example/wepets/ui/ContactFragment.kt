package com.example.wepets.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.wepets.adapter.CustomerAdapter
import com.example.wepets.databinding.FragmentContactBinding
import com.example.wepets.db.dao.PetDao
import com.example.wepets.db.database.PetDatabase
import com.example.wepets.model.Pet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContactFragment : Fragment() {


    private lateinit var binding:FragmentContactBinding

    // Instancias DB
    private lateinit var db:PetDatabase
    private lateinit var petDao: PetDao


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //DB
        binding = FragmentContactBinding.inflate(inflater, container, false)
        //Garente que o contexto Existe(Cuidado)
        db = context?.let { PetDatabase.getDatabase(it) }!!
        petDao = db.getPetDao




        binding.fabAdd.setOnClickListener {
            startActivity(Intent(context,NewPetActivity::class.java))
        }

        //Configurando RecyclerView

        var customerList:List<Pet> = emptyList()

        CoroutineScope(Dispatchers.IO).launch {
            customerList = petDao.findAll()
        }

        val adapter = CustomerAdapter(customerList, onItemClickListener = {
                customer->
                val i:Intent(this, )
        })




        return binding.root
    }


}