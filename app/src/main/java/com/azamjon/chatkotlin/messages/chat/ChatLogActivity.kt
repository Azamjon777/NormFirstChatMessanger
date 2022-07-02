package com.azamjon.chatkotlin.messages.chat

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.azamjon.chatkotlin.R
import com.azamjon.chatkotlin.models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity() {
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: Button
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var messageList: ArrayList<Messages>
    private lateinit var mDbRef: DatabaseReference

    var receiverRoom: String? = null
    private var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        setupDummyData()

    }

    private fun setupDummyData() {
        val user = intent.getParcelableExtra<Users>("name")
        val receiverUid = intent.getStringExtra("uid")

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        mDbRef = FirebaseDatabase.getInstance().reference

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        supportActionBar?.title = user?.name

        messageList = ArrayList()
        chatAdapter = ChatAdapter(this, messageList)
        recyclerview_chat_log.adapter = chatAdapter

        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnapshot in snapshot.children) {
                        val messages = postSnapshot.getValue(Messages::class.java)
                        messageList.add(messages!!)
                    }
                    chatAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        chatRecyclerView = findViewById(R.id.recyclerview_chat_log)
        messageBox = findViewById(R.id.edittext_chat)
        sendButton = findViewById(R.id.send_button_chat_log)

        sendButton.setOnClickListener {
            val message = messageBox.text.toString()
            val messageObject = Messages(message, senderUid)
            mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            messageBox.setText("")
        }
    }

}