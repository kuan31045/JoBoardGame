package com.kappstudio.joboardgame.ui.party

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
import com.kappstudio.joboardgame.ui.login.UserManager
import com.kappstudio.joboardgame.databinding.ItemPartyBinding
import com.kappstudio.joboardgame.ui.partydetail.NavToPartyDetailInterface
import java.util.*

class PartyAdapter(private val viewModel: ViewModel) :
    ListAdapter<Party, PartyAdapter.PartyViewHolder>(DiffCallback) {

    inner class PartyViewHolder(private var binding: ItemPartyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(party: Party) {
            viewModel as NavToPartyDetailInterface
            binding.apply {

                viewModel.hosts.value?.first { it.id == party.hostId }?.let { host ->
                    tvHost.text = host.name
                    bindImage(ivHost, host.image)
                }

                tvIsOver.visibility =
                    if (party.partyTime + 3600000 < Calendar.getInstance().timeInMillis) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }

                bindImage(ivCover, party.cover)
                tvTitle.text = party.title
                tvLocation.text = party.location.address
                tvTime.text = party.partyTime.toString()
                tvGame.text = ""
                bindTextViewDate(tvTime, party.partyTime)
                tvPlayerQty.text = "${party.playerIdList.size}/${party.requirePlayerQty}"

                party.gameNameList.forEach {
                    tvGame.text = "${tvGame.text}$it"
                    if (it != party.gameNameList.last()) {
                        tvGame.text = "${tvGame.text}, "
                    }
                }

                if ((UserManager.user.value?.id ?: "") in party.playerIdList) {
                    tvAlreadyJoin.visibility = View.VISIBLE
                } else {
                    tvAlreadyJoin.visibility = View.GONE
                }

                clParty.setOnClickListener {
                    viewModel.navToPartyDetail(party.id)
                }
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Party>() {
        override fun areItemsTheSame(oldItem: Party, newItem: Party): Boolean {
            return oldItem.id == newItem.id && oldItem.playerIdList == newItem.playerIdList
        }

        override fun areContentsTheSame(oldItem: Party, newItem: Party): Boolean {
            return oldItem.id == newItem.id && oldItem.playerIdList == newItem.playerIdList
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
