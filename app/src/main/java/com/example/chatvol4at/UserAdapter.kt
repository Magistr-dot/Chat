package com.example.chatvol4at

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.user_item.view.*
import java.util.*

//
class UserAdapter(user: ArrayList<User>) :

    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var users = user
    private lateinit var listener: OnUserClickListener

    interface OnUserClickListener {

        fun onUserClick(position: Int)


    }

    fun setOnUserClickListener(listener: OnUserClickListener) {
        this.listener = listener

    }

    ////////////////////////
    class UserViewHolder(v: View, private var listener: OnUserClickListener) : RecyclerView.ViewHolder(v),
        View.OnClickListener {


        private var view: View = v

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onUserClick(position)

            }

        }


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return UserViewHolder(view, listener)
    }


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val current = users[position]
        holder.itemView.avatarImage.setImageResource(current.avatarMockUp)
        holder.itemView.userNameText.text = current.nickname
    }

    override fun getItemCount(): Int {
        return users.size
    }


}


