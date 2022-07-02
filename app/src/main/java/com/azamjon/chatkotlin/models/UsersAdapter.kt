package com.azamjon.chatkotlin.models

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azamjon.chatkotlin.R
import com.azamjon.chatkotlin.databinding.UserItemBinding
import com.azamjon.chatkotlin.messages.chat.ChatLogActivity
import com.google.firebase.auth.FirebaseAuth

class UsersAdapter(private val context: Context) : RecyclerView.Adapter<UsersAdapter.Holder>() {
    private val usersList = ArrayList<Users>()

    class Holder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = UserItemBinding.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val currentUser = usersList[position]
        with(holder.binding) {
            usernameTextviewNewMessage.text = usersList[position].name
            messageItem.text = usersList[position].message
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatLogActivity::class.java)
            intent.putExtra("name", currentUser.name)
            intent.putExtra("uid", FirebaseAuth.getInstance().uid)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addUser(users: Users) {
        usersList.add(users)
        notifyDataSetChanged()
    }
}