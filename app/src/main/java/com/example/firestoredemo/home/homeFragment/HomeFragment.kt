package com.example.firestoredemo.home.homeFragment

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.firestoredemo.databinding.FragmentHomeBinding
import com.example.firestoredemo.details.DetailsActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment(),AdapterView.OnItemSelectedListener {

    private var _binding : FragmentHomeBinding?=null
    private val binding get() = _binding!!
    private var classItem = arrayOf(1,2,3,4,5,6)
    var db = Firebase.firestore
    private var homeDataList:ArrayList<ArrayList<HashMap<String,String>>> = ArrayList()
    private var classDataList:ArrayList<HashMap<String,String>> = ArrayList()
    private lateinit var homeAdapter:HomeAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.spinner.onItemSelectedListener  =  this
        //Creating the ArrayAdapter instance having the country list
        val aa: ArrayAdapter<*> =
            ArrayAdapter<Any?>(requireContext(), android.R.layout.simple_spinner_item, classItem)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //Setting the ArrayAdapter data on the Spinner
        binding.spinner.setAdapter(aa)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        //requireActivity().showToasts(classItem[p2].toString())
        db.collection("subjects").document("cbse")
            .get()
            .addOnSuccessListener { result ->
               var ff =  result.data!!.get("english")
                Log.i("d",ff.toString())
                Log.i("f",(result.data!!.get("english") as HashMap<String, ArrayList<HashMap<String,String>>>).getValue("5").toString())
                homeDataList.clear()
                homeDataList.addAll((result.data!!.get("english") as HashMap<String, ArrayList<HashMap<String,String>>>).values)
                classDataList.clear()
                classDataList.addAll(homeDataList[p2])
                Log.i("fg",homeDataList.toString())
                homeAdapter = HomeAdapter(requireContext(),classDataList){ pos->
                    onItemClick(pos)
                }
                binding.rvHome.adapter = homeAdapter
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    private fun onItemClick(pos: Int) {
        startActivity(Intent(requireActivity(),DetailsActivity::class.java))
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }
}