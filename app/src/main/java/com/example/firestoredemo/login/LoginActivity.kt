package com.example.firestoredemo.login

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.firestoredemo.R
import com.example.firestoredemo.databinding.ActivityLoginBinding
import com.example.firestoredemo.home.HomeActivity
import com.example.firestoredemo.utils.ConstantUtils
import com.example.firestoredemo.utils.PrefHelper
import com.example.firestoredemo.utils.showToasts
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity(),View.OnClickListener {


    private lateinit var binding:ActivityLoginBinding

    // Access a Cloud Firestore instance from your Activity
    var db = Firebase.firestore
    private var userStatus:Int=0 //0-> new user, 1-> already registered,2-> incorrectPassword
    private lateinit var pref:PrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        pref= PrefHelper(this)

        binding.tvLoginBtn.setOnClickListener(this)
        binding.tvSignUp.setOnClickListener(this)
    }

    private fun validateCredential() {
        if(binding.etEmailLogin.text.toString().trim().isEmpty()){
            showToasts(resources.getString(R.string.email_empty))
        }else if (!(Patterns.EMAIL_ADDRESS.matcher(binding.etEmailLogin.text.toString().trim()).matches())){
            showToasts(resources.getString(R.string.invalid_email))
        }else if(binding.etPasswordLogin.text.toString().trim().isEmpty()){
            showToasts(resources.getString(R.string.password_empty))
        }else{
            validateUser(binding.etEmailLogin.text.toString().trim(),
                binding.etPasswordLogin.text.toString().trim())
        }
    }

    private fun validateUser(email: String, password: String) {
       db.collection("users")
        .whereEqualTo("email",email)
           .get()
           .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data.get("email")}")
                    if(password!=document.data.get("password")){
                        userStatus=2
                    }else{
                        userStatus=1
                    }
                }
           }
           .addOnFailureListener { exception->
               Log.w(TAG, "Error getting documents: ", exception)
           }
        when(userStatus){
            0->{
                showToasts("No user registered with this email")
            }
            1->{
                startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                pref.saveBoolean(ConstantUtils.IS_LOGIN,true)
                finish()
            }
            2->{
                showToasts("Incorrect password")
            }
        }
    }

    override fun onClick(v: View?) {
        when(v){
            binding.tvLoginBtn->{
                validateCredential()
            }
            binding.tvSignUp->{
                startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
            }
        }
    }
}