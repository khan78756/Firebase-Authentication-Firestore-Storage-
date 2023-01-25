package com.example.firebaseauthenication

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseauthenication.Global.Companion.auth
import com.example.firebaseauthenication.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val dB=Firebase.firestore
    private lateinit var storageReference: StorageReference

    private lateinit var progressDialog: ProgressDialog
   // private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth=FirebaseAuth.getInstance()
        //In case of sign out
        if (auth.currentUser == null){
            Intent(this,loginActivity::class.java).also {
                startActivity(it)
            finish()}}
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title="User Profile"
       progressDialog=ProgressDialog(this,R.style.AppCompatAlertDialogStyle)
       progressDialog.setMessage("Please wait....")
       progressDialog.setCancelable(false)
       progressDialog.show()

        //In case of sign out
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
        val uid= auth.currentUser?.uid
        val imageId=uid
        storageReference= FirebaseStorage.getInstance().getReference(imageId!!)
        try {
            var localFile: File=File.createTempFile("tempFile","jpg")
            storageReference.getFile(localFile).addOnSuccessListener {
                var bitmap: Bitmap=BitmapFactory.decodeFile(localFile.absolutePath)
                progressDialog.dismiss()
                binding.imageViewData.setImageBitmap(bitmap)
            }

        }catch (e: IOException)
        {
            e.printStackTrace()
        }
        val docRef = dB.collection("users").document(uid!!)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val name= document.data!!["name"].toString()
                    val fName= document.data!!["fatherName"].toString()
                    val age= document.data!!["age"].toString()
                    val gender= document.data!!["gender"].toString()

                    binding.name.setText(name)
                    binding.fName.setText(fName)
                    binding.age.setText(age)
                    binding.gender.setText(gender)
                    binding.textView.text=update()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this,"Error occur",Toast.LENGTH_SHORT).show()
            }

    }

    private fun update():String{
        return " Signed mail: ${auth.currentUser?.email}"
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}