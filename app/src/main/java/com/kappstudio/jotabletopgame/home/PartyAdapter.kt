package com.kappstudio.jotabletopgame.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kappstudio.jotabletopgame.data.Party
import com.kappstudio.jotabletopgame.data.UserObject
import com.kappstudio.jotabletopgame.databinding.ItemPartyBinding

class PartyAdapter(private val viewModel: HomeViewModel) : ListAdapter<Party, PartyAdapter.PartyViewHolder>(DiffCallback) {

    class PartyViewHolder(private var binding: ItemPartyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(party: Party, viewModel: HomeViewModel) {

            binding.apply {
                tvTitle.text = party.title
                tvLocation.text = party.location
                tvTime.text = party.partyTime.toString()
                tvHost.text = party.hostName
                tvPlayerQty.text = "${party.playerIdList.size}/${party.requirePlayerQty}"
                party.gameNameList.forEach {
                    tvGame.text = "${tvGame.text}$it, "
                }
                if (UserObject.mUserId in party.playerIdList){
                    tvAlreadyJoin.visibility = View.VISIBLE
                }

                clParty.setOnClickListener {
                    viewModel.navToPartyDetail(party.id)
                }

            }

        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Party>() {
        override fun areItemsTheSame(oldItem: Party, newItem: Party): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Party, newItem: Party): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartyViewHolder {
        return PartyViewHolder(
            ItemPartyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PartyViewHolder, position: Int) {
        holder.bind(getItem(position),viewModel )
    }
}
