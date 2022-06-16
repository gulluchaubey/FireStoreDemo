package com.example.firestoredemo.login

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.lifecycleScope
import com.example.firestoredemo.R
import com.example.firestoredemo.databinding.ActivitySignupBinding
import com.example.firestoredemo.home.HomeActivity
import com.example.firestoredemo.utils.ConstantUtils
import com.example.firestoredemo.utils.PrefHelper
import com.example.firestoredemo.utils.showMessageInternet
import com.example.firestoredemo.utils.showToasts
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity()  {

    private lateinit var binding: ActivitySignupBinding
    // Access a Cloud Firestore instance from your Activity
    var db = Firebase.firestore
    private var userStatus:Int=0 //0-> new user, 1-> already registered
    private lateinit var pref: PrefHelper
    lateinit var myHandler: CoroutineExceptionHandler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        pref = PrefHelper(this)
        myHandler = CoroutineExceptionHandler {coroutineContext, throwable ->
            Log.e("CoroutineException","Exception handled: ${throwable.localizedMessage}")
        }

        binding.tvSignupBtn.setOnClickListener {
            lifecycleScope.launch(myHandler){
                try {
                    validateCredential()
                }catch (e:java.lang.Exception){
                    showMessageInternet(e)
                }
            }
        }
    }
    private suspend fun validateCredential() {
        if(binding.etNameSignup.text.toString().trim().isEmpty()){
            showToasts(resources.getString(R.string.name_empty))
        }else if(binding.etEmailSignup.text.toString().trim().isEmpty()){
            showToasts(resources.getString(R.string.email_empty))
        }else if (!(Patterns.EMAIL_ADDRESS.matcher(binding.etEmailSignup.text.toString().trim()).matches())){
            showToasts(resources.getString(R.string.invalid_email))
        }else if(binding.etPasswordSignup.text.toString().trim().isEmpty()){
            showToasts(resources.getString(R.string.password_empty))
        }else if(binding.etClassSignup.text.toString().trim().isEmpty()){
            showToasts(resources.getString(R.string.password_empty))
        }else{
            val  job = lifecycleScope.launch(myHandler){
                try {
                    validateUser(binding.etNameSignup.text.toString().trim())
                } catch (e: Exception) {
                    showMessageInternet(e)
                }
            }
            job.join()
            if(userStatus==0){
                createUser(binding.etNameSignup.text.toString().trim(),
                    binding.etEmailSignup.text.toString().trim(),
                    binding.etPasswordSignup.text.toString().trim(),
                    binding.etClassSignup.text.toString().trim())
            }
        }
    }
    private fun validateUser(email: String) {
        db.collection("users")
            .whereEqualTo("email",email)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(ContentValues.TAG, "${document.id} => ${document.data.get("email")}")
                    showToasts("Email already registered.")
                    userStatus=1
                }
            }
            .addOnFailureListener { exception->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }

    private fun createUser(name: String, email: String, password: String, classes: String) {
        // Create a new user with a first and last name
        val user = hashMapOf(
            "name" to name,
            "email" to email,
            "password" to password,
            "board" to "CBSE",
            "class" to classes)

// Add a new document with a generated ID
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                startActivity(Intent(this@SignupActivity, HomeActivity::class.java))
                pref.saveBoolean(ConstantUtils.IS_LOGIN,true)
                finish()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }
}