package com.example.firebaseauthenication

import com.google.firebase.auth.FirebaseAuth

class Global {

    companion object{
        @JvmStatic
        lateinit var auth: FirebaseAuth

    }
}