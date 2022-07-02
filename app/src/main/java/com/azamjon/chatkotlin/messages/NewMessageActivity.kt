package com.azamjon.chatkotlin.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.azamjon.chatkotlin.R
import com.azamjon.chatkotlin.models.Users
import com.azamjon.chatkotlin.models.UsersAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_new_message.*

class NewMessageActivity : AppCompatActivity() {
    private val adapter = UsersAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Select user"
        fetchUser()
    }


    private fun fetchUser() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                recyclerview_new_message.adapter = adapter
                snapshot.children.forEach {
                    val user = it.getValue(Users::class.java)
                    if (user != null) {
                        adapter.addUser(user)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}