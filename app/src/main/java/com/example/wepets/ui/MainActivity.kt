package com.example.wepets.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.wepets.R
import com.example.wepets.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
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

        loadBottomNav()





    }

    //Inicia O bottom Navigation com Scheduling Primeiro
    private fun loadBottomNav(){
        loadFragment(SchedulingFragment())
        binding.bottomNavMain.setOnItemSelectedListener { frag->
            when(frag.itemId){
                R.id.item_calendar ->{
                    loadFragment(SchedulingFragment())
                    true
                }
                R.id.item_money ->{
                    loadFragment(RevenueFragment())
                    true
                }
                R.id.item_pet ->{
                    loadFragment(ContactFragment())
                    true
                }
                else->false

            }
        }
    }

    //Carrega o Fragment F:loadBottomNav
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }


}