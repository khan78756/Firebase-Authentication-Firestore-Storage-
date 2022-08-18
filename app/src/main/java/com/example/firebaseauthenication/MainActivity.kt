package com.example.firebaseauthenication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseauthenication.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    companion object{
        lateinit var auth:FirebaseAuth
    }

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth=FirebaseAuth.getInstance()
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title="Sign In page"

        if (auth.currentUser == null){
            Intent(this,loginActivity::class.java).also {
                startActivity(it)}}





        binding.btnlogout.setOnClickListener {
           auth.signOut()
            binding.textView.text=update()
            Intent(this,loginActivity::class.java).also {
                startActivity(it)
            finish()}
        }
    }

    override fun onResume() {
        super.onResume()
        binding.textView.text=update()
    }

    private fun update():String{
        return " Signed mail: ${auth.currentUser?.email}"
    }
}