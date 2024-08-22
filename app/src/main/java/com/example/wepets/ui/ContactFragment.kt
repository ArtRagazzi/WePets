package com.example.wepets.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wepets.adapter.CustomerAdapter
import com.example.wepets.databinding.FragmentContactBinding
import com.example.wepets.db.dao.PetDao
import com.example.wepets.db.database.PetDatabase
import com.example.wepets.model.Pet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        CoroutineScope(Dispatchers.IO).launch {
            val customerList = petDao.findAll()

            // Atualiza o RecyclerView na thread principal
            withContext(Dispatchers.Main) {
                val adapter = CustomerAdapter(customerList, onItemClickListener = { customer ->
                    val intent = Intent(context, CustomerActivity::class.java).apply {
                        putExtra("customer", customer)
                    }
                    startActivity(intent)
                })
                binding.rvCustomer.adapter = adapter
                binding.rvCustomer.layoutManager = LinearLayoutManager(context)
                binding.rvCustomer.addItemDecoration(
                    DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
                )
            }
        }




        return binding.root
    }


}