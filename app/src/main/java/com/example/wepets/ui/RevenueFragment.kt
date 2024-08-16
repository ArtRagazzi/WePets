package com.example.wepets.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.wepets.databinding.FragmentRevenueBinding

class RevenueFragment : Fragment() {

    private lateinit var binding: FragmentRevenueBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        binding = FragmentRevenueBinding.inflate(inflater, container,false)
        return binding.root
    }
}


