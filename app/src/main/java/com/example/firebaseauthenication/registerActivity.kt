package com.example.firebaseauthenication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseauthenication.databinding.ActivityRegisterBinding

class registerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
       // auth= FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)

        binding=ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnloginn.setOnClickListener {


            Intent(this,loginActivity::class.java).also {
            startActivity(it)
            finish()

        }
        }


        binding.btncreate.setOnClickListener {
            val email=binding.etemail.text.toString()
            val password=binding.etpass.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()){
                MainActivity.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful){
                        Intent(this,loginActivity::class.java).also {
                            startActivity(it)
                            finish()}
                    }
                }.addOnFailureListener {
                    Toast.makeText(this,"Gmail already exist",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}