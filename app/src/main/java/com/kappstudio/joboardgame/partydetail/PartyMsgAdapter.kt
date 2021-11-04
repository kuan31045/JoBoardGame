package com.kappstudio.joboardgame.partydetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kappstudio.joboardgame.bindImage
import com.kappstudio.joboardgame.data.PartyMsg
import com.kappstudio.joboardgame.databinding.ItemPartyMsgBinding
import com.kappstudio.joboardgame.util.timeUtil
import timber.log.Timber
import java.util.*

class PartyMsgAdapter(private val viewModel: PartyDetailViewModel) :
    ListAdapter<PartyMsg, PartyMsgAdapter.MsgViewHolder>(DiffCallback) {

    inner class MsgViewHolder(private val binding: ItemPartyMsgBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(msg: PartyMsg, viewModel: PartyDetailViewModel) {
            Timber.d("bind")
            binding.apply {
                bindImage(ivUser, msg.user.image)
                tvName.text = msg.user.name
                tvMsg.text = msg.msg

                tvTime.text= timeUtil(msg.createdTime)

                ivUser.setOnClickListener {
                    viewModel.navToUser(msg.user.id)
                }
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
        Timber.d("create")
        return MsgViewHolder(
            ItemPartyMsgBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MsgViewHolder, position: Int) {
        Timber.d("BindViewHolder")

        holder.bind(getItem(position), viewModel)
    }
}