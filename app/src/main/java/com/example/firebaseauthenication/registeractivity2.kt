package com.example.firebaseauthenication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.firebaseauthenication.databinding.ActivityRegisterBinding
import com.example.firebaseauthenication.databinding.ActivityRegisteractivity2Binding

class registeractivity2 : AppCompatActivity() {


    private lateinit var binding: ActivityRegisteractivity2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRegisteractivity2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()


        binding.btnNext.setOnClickListener {
            var name = binding.etName.text.toString()
            var fatherName = binding.etFName.text.toString()
            var age = binding.etAge.text.toString()
            var gender = binding.etGender.text.toString()

            if (name == "" || fatherName == "" || age == "" || gender == "")
            {
                Toast.makeText(this, "Some filed is missing", Toast.LENGTH_SHORT).show()
            }
            else
            {
                var intent=Intent(this,registerActivity::class.java)
                intent.putExtra("name",name)
                intent.putExtra("fatherName",fatherName)
                intent.putExtra("age",age)
                intent.putExtra("gender",gender)
                startActivity(intent)
            }
        }
    }
}