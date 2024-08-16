package com.example.wepets.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.wepets.databinding.FragmentSchedulingBinding

class SchedulingFragment : Fragment() {

    private lateinit var binding: FragmentSchedulingBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        binding = FragmentSchedulingBinding.inflate(inflater, container,false)
        return binding.root
    }
}
