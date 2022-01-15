package com.kappstudio.joboardgame.ui.partydetail

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.allUsers
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.bindImage
import com.kappstudio.joboardgame.data.PartyMsg
import com.kappstudio.joboardgame.databinding.ItemPartyMsgBinding
import com.kappstudio.joboardgame.util.timeUtil

class PartyMsgAdapter(private val viewModel: PartyDetailViewModel) :
    ListAdapter<PartyMsg, PartyMsgAdapter.MsgViewHolder>(DiffCallback) {

    inner class MsgViewHolder(private val binding: ItemPartyMsgBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(msg: PartyMsg, viewModel: PartyDetailViewModel) {
            binding.apply {

                allUsers.value?.first { it.id == msg.userId }?.let { user ->
                    bindImage(ivUser, user.image)
                    tvName.text = user.name
                    ivUser.setOnClickListener {
                        viewModel.navToUser(user.id)
                    }
                }
                btnMore.setOnClickListener {
                    val popMenu = PopupMenu(appInstance, btnMore)
                    popMenu.menuInflater.inflate(R.menu.pop_msg_menu, popMenu.menu)
                    popMenu.setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.report_msg -> viewModel.reportMsg(msg)
                        }
                        true
                    }
                    popMenu.show()
                }
                tvMsg.text = msg.msg
                tvTime.text = timeUtil(msg.createdTime)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<PartyMsg>() {
        override fun areItemsTheSame(oldItem: PartyMsg, newItem: PartyMsg): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PartyMsg, newItem: PartyMsg): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MsgViewHolder {
        return MsgViewHolder(
            ItemPartyMsgBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MsgViewHolder, position: Int) {

        holder.bind(getItem(position), viewModel)
    }
}