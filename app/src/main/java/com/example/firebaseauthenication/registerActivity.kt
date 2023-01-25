package com.example.firebaseauthenication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseauthenication.Global.Companion.auth
import com.example.firebaseauthenication.databinding.ActivityRegisterBinding
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*
import kotlin.collections.HashMap

class registerActivity : AppCompatActivity() {
    lateinit var fibase: FirebaseFirestore
    lateinit var userID: String
    lateinit var email: String
    private val pickImage = 100
    private var imageUri: Uri? = null
    lateinit var storageReference: StorageReference
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()
        //get data from intent
        val intent = intent
        val name= intent.getStringExtra("name").toString()
        val fName = intent.getStringExtra("fatherName").toString()
        val age = intent.getStringExtra("age").toString()
        val gender = intent.getStringExtra("gender").toString()

        binding.imageButton2.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }


        binding.btnloginn.setOnClickListener {
            Intent(this,loginActivity::class.java).also {
            startActivity(it)
            finish()
            }
        }
        binding.btncreate.setOnClickListener {
             email=binding.etemail.text.toString()
            val password=binding.etpass.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()){
             auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful){
                        userID= auth.currentUser!!.uid
                        fibase=FirebaseFirestore.getInstance()
                        storageReference=FirebaseStorage.getInstance().getReference(userID)
                        storageReference.putFile(imageUri!!).addOnSuccessListener(){
                            Toast.makeText(this,"Created",Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Toast.makeText(this,"Failed to load Image",Toast.LENGTH_SHORT).show()
                        }
                        val documentReference: DocumentReference =fibase.collection("users").document(userID)
                        val user  = HashMap<String,String>()
                        user.put("name",name)
                        user.put("fatherName",fName)
                        user.put("age",age)
                        user.put("gender",gender)
                        documentReference.set(user).addOnSuccessListener {
                            Toast.makeText(this,"Created",Toast.LENGTH_SHORT).show()
                        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            binding.pickImage.setImageURI(imageUri)
        }
    }

}