package com.azamjon.chatkotlin.messages.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azamjon.chatkotlin.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.receive.view.*
import kotlinx.android.synthetic.main.send.view.*

class ChatAdapter(private val context: Context, private val messagesList: ArrayList<Messages>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM_RECEIVE = 1
    private val ITEM_SEND = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            val view: View = LayoutInflater.from(context).inflate(R.layout.receive, parent, false)
            return ReceiveViewHolder(view)
        } else {
            val view: View = LayoutInflater.from(context).inflate(R.layout.send, parent, false)
            return SendViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messagesList[position]

        if (holder.javaClass == SendViewHolder::class.java) {
            val viewHolder = holder as SendViewHolder
            viewHolder.sendMessage = currentMessage.message
        } else {
            val viewHolder = holder as ReceiveViewHolder
            viewHolder.receiveMessage = currentMessage.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messagesList[position]
        if (FirebaseAuth.getInstance().currentUser?.uid!! == currentMessage.senderId) {
            return ITEM_SEND
        } else {
            return ITEM_RECEIVE
        }
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }


    class SendViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var sendMessage = view.text_send_message.text
    }

    class ReceiveViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var receiveMessage = view.text_receive_message.text
    }
}