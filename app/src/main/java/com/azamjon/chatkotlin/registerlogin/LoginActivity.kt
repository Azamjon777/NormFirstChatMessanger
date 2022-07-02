package com.azamjon.chatkotlin.registerlogin

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.azamjon.chatkotlin.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_login)

        login_button_login.setOnClickListener {
            val email = email_edittext_register.text.toString()
            val password = password_edittext_register.text.toString()
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            //.addOnCompleteListener {  }
        }

        back_to_register_textview.setOnClickListener {
            finish()
        }
    }
}