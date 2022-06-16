package com.example.firestoredemo.details.pdf

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.firestoredemo.R
import com.example.firestoredemo.databinding.FragmentPdfBinding


class PdfFragment : Fragment() {

    private var _binding:FragmentPdfBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPdfBinding.inflate(layoutInflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tv.setOnClickListener {
            startActivity(Intent(requireActivity(),ViewPdfActivity::class.java))
        }
    }


}