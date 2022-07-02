package com.azamjon.chatkotlin.registerlogin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.azamjon.chatkotlin.R
import com.azamjon.chatkotlin.messages.LatestMessagesActivity
import com.azamjon.chatkotlin.models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        register_button_register.setOnClickListener {
            performRegister()
        }
        already_have_an_account_text_view.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        selectPhoto_button_register.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    var selectPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectPhotoUri)

            selectPhoto_imageview_register.setImageBitmap(bitmap)
            selectPhoto_button_register.alpha = 0f

//            val bitmapDrawable = BitmapDrawable(bitmap)
//            selectPhoto_button_register.setBackgroundDrawable(bitmapDrawable)
        }
    }

    private fun performRegister() {
        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter text in email/password", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                // uploadImageToFirebaseStore()
                saveUserToFirebaseDatabase()
            }
            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun uploadImageToFirebaseStore() {
        if (selectPhotoUri != null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")


        ref.putFile(selectPhotoUri!!)
            .addOnCompleteListener {
                ref.downloadUrl.addOnSuccessListener {
                    saveUserToFirebaseDatabase(/*it.toString()*/)
                    Toast.makeText(this, "Photo saved", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveUserToFirebaseDatabase(/*profileImageUrl: String*/) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = Users(username_edittext_register.text.toString(), ""/*profileImageUrl*/)
        ref.setValue(user)
            .addOnSuccessListener {
                val intent = Intent(this, LatestMessagesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                Toast.makeText(this, "user saved", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {

            }
    }
}
