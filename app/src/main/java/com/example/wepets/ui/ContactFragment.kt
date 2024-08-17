package com.example.wepets.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.wepets.databinding.FragmentContactBinding

class ContactFragment : Fragment() {


    private lateinit var binding:FragmentContactBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentContactBinding.inflate(inflater, container, false)

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(context,NewPetActivity::class.java))
        }


        return binding.root
    }


}