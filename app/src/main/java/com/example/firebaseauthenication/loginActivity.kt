package com.example.firebaseauthenication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.firebaseauthenication.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

class loginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Configure sign in
        val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.Client_id))
            .requestEmail()
            .build()

        val googleSignInClient=GoogleSignIn.getClient(this,gso)

        binding.btnregister.setOnClickListener {

            Intent(this,registerActivity::class.java).also {
                startActivity(it)
                finish()

            }

        }

        binding.btnlogin.setOnClickListener {
            val email=binding.etemail.text.toString()
            val password=binding.etpass.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()){
                MainActivity.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful){
                        Intent(this,MainActivity::class.java).also {
                            startActivity(it)
                            finish()}

                    }


                }.addOnFailureListener{
                    Toast.makeText(this,"Please Sign with Google Email",Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.gmail.setOnClickListener {
            googleSignInClient.signOut()
            startActivityForResult(googleSignInClient.signInIntent,13)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode ==13) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account=task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account.idToken)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val cradential=GoogleAuthProvider.getCredential(idToken,null)
        MainActivity.auth.signInWithCredential(cradential)
            .addOnCompleteListener(this){ task->
                if (task.isSuccessful){
                    Intent(this,MainActivity::class.java).also {
                        startActivity(it)
                        finish()}
                }
            }.addOnFailureListener {
                Toast.makeText(this,"Wrong",Toast.LENGTH_SHORT).show()
            }
    }
}