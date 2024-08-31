package com.example.wepets.ui.contact

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wepets.adapter.CustomerAdapter
import com.example.wepets.databinding.FragmentContactBinding
import com.example.wepets.db.dao.PetDao
import com.example.wepets.db.database.WePetsDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactFragment : Fragment() {


    private lateinit var binding:FragmentContactBinding

    private lateinit var adapter: CustomerAdapter

    // Instancias DB
    private lateinit var db:WePetsDatabase
    private lateinit var petDao: PetDao


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentContactBinding.inflate(inflater, container, false)
        //DB
        //Garente que o contexto Existe(Cuidado)
        db = context?.let { WePetsDatabase.getDatabase(it) }!!
        petDao = db.getPetDao



        binding.fabAdd.setOnClickListener {
            startActivity(Intent(context, NewPetActivity::class.java))
        }

        //Configurando RecyclerView
        adapter = CustomerAdapter(onItemClickListener = { customer ->
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

        binding.btnSeach.setOnClickListener{
            searchByName()
        }



        return binding.root

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


}