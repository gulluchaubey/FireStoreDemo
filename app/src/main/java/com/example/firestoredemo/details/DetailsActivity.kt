package com.example.firestoredemo.details

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.firestoredemo.R
import com.example.firestoredemo.databinding.ActivityDetailsBinding
import com.example.firestoredemo.home.homeFragment.HomeAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding:ActivityDetailsBinding
    var db = Firebase.firestore
    val itemArray = arrayOf(
        "Video",
        "NoteBook")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        db.collection("topics").document("cbse")
            .get()
            .addOnSuccessListener { result ->
                var ff =  result.data!!.get("english")
                Log.i("d",ff.toString())
                Log.i("f",(result.data!!.get("english") as HashMap<String, ArrayList<HashMap<String,HashMap<String,HashMap<String,ArrayList<HashMap<HashMap<String,ArrayList<HashMap<String,String>>>,HashMap<String,String>>>>>>>>).toString())

            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }

        val adapter = ViewPagerAdapter(supportFragmentManager,lifecycle)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tablayout,binding.viewPager){ tab,position->
            tab.text = itemArray[position]
        }.attach()
    }
}