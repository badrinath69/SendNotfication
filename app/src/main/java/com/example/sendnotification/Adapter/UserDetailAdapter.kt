package com.example.sendnotification.Adapter

// UserDetailAdapter.kt
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sendnotification.Modal.User
import com.example.sendnotification.Modal.UserDetail
import com.example.sendnotification.R


class UserDetailAdapter(
    private val userDetails: List<UserDetail>,
    private val onItemClicked: (UserDetail) -> Unit
) : RecyclerView.Adapter<UserDetailAdapter.UserDetailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserDetailViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_detail, parent, false)
        return UserDetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserDetailViewHolder, position: Int) {
        holder.bind(userDetails[position], onItemClicked)
    }

    override fun getItemCount(): Int = userDetails.size

    class UserDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val tokenTextView: TextView = itemView.findViewById(R.id.tokenTextView)

        fun bind(userDetail: UserDetail, onItemClicked: (UserDetail) -> Unit) {
            nameTextView.text = userDetail.name
            tokenTextView.text = userDetail.deviceToken

            itemView.setOnClickListener {
                onItemClicked(userDetail)
            }
        }
    }
}



