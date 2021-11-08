package com.kappstudio.joboardgame.party

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kappstudio.joboardgame.bindImage
import com.kappstudio.joboardgame.bindTextViewDate
import com.kappstudio.joboardgame.data.Party
import com.kappstudio.joboardgame.data.UserManager
import com.kappstudio.joboardgame.databinding.ItemPartyBinding
import com.kappstudio.joboardgame.partydetail.NavToPartyDetailInterface
import com.kappstudio.joboardgame.util.setBlurView

class PartyAdapter(private val viewModel: ViewModel) :
    ListAdapter<Party, PartyAdapter.PartyViewHolder>(DiffCallback) {

    inner class PartyViewHolder(private var binding: ItemPartyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(party: Party) {

            binding.apply {
                bindImage(ivCover, party.cover)
                bindImage(ivHost, party.host.image)
                tvTitle.text = party.title
                tvLocation.text = party.location
                tvTime.text = party.partyTime.toString()
                tvHost.text = party.host.name
                tvGame.text = ""
                bindTextViewDate(tvTime, party.partyTime)
                tvPlayerQty.text = "${party.playerIdList.size}/${party.requirePlayerQty}"
                party.gameList.forEach {
                    tvGame.text = "${tvGame.text}${it.name}, "
                }
                if (UserManager.user["id"] in party.playerIdList) {
                    tvAlreadyJoin.visibility = View.VISIBLE
                } else {
                    tvAlreadyJoin.visibility = View.GONE
                }

                clParty.setOnClickListener {
                    when (viewModel) {
                        is NavToPartyDetailInterface -> viewModel.navToPartyDetail(party.id)
                    }
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
        holder.bind(getItem(position))
    }
}
