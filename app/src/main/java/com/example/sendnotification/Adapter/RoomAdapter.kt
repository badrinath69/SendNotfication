package com.example.sendnotification.Adapter

import com.example.sendnotification.Modal.Room


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sendnotification.R

class RoomAdapter(
    private val rooms: List<Room>,
    private val isSubscriber: Boolean,
    private val onJoinClick: (roomId: String) -> Unit = {}
) : RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_room, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = rooms[position]
        holder.bind(room)

        if (isSubscriber) {
            holder.joinButton.visibility = View.VISIBLE
            holder.joinButton.setOnClickListener {
                onJoinClick(room.id)
            }
        } else {
            holder.joinButton.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = rooms.size

    inner class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val roomTitle: TextView = itemView.findViewById(R.id.roomTitleTextView)
        private val roomDateTime: TextView = itemView.findViewById(R.id.roomDateTimeTextView)
        val joinButton: Button = itemView.findViewById(R.id.joinRoomButton)

        fun bind(room: Room) {
            roomTitle.text = room.title
            roomDateTime.text = room.dateTime
        }
    }
}
